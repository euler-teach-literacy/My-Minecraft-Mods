package com.randomeuler.mace;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import org.lwjgl.glfw.GLFW;

import java.util.List;

public class MaceMod implements ClientModInitializer {

    private static KeyMapping menuKey;
    private static KeyMapping autoBackKey;

    /** 破盾重锤功能总开关（由 GUI 或模块列表控制） */
    public static boolean moduleBrokenShieldEnabled = false;

    /**
     * 开启时：主手斧头时近战会重定向到范围内最近实体。
     */
    public static boolean auraRedirectEnabled = true;

    public static double auraRange = 6.0;

    /** 附近有玩家时自动发送 /back（由 ] 或 GUI 开关） */
    public static boolean moduleAutoBackEnabled = false;

    public static double autoBackDetectRange = 48.0;

    private static int autoBackCooldownTicks = 0;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.mace.menu",
                        GLFW.GLFW_KEY_EQUAL,
                        KeyMapping.Category.MISC
                )
        );
        autoBackKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.mace.auto_back",
                        GLFW.GLFW_KEY_RIGHT_BRACKET,
                        KeyMapping.Category.MISC
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();

            while (menuKey.consumeClick()) {
                mc.setScreen(new MaceModuleScreen());
            }
            while (autoBackKey.consumeClick()) {
                moduleAutoBackEnabled = !moduleAutoBackEnabled;
                MaceModuleScreen.tryRebuild(mc);
            }

            tickAutoBack(mc);
        });
    }

    private static void tickAutoBack(Minecraft client) {
        if (!moduleAutoBackEnabled || client.player == null || client.level == null || client.getConnection() == null) {
            return;
        }
        if (autoBackCooldownTicks > 0) {
            autoBackCooldownTicks--;
            return;
        }
        LocalPlayer self = client.player;
        if (!(self.level() instanceof ClientLevel level)) {
            return;
        }
        if (!hasOtherPlayerNearby(self, level, autoBackDetectRange)) {
            return;
        }
        client.getConnection().sendCommand("back");
        autoBackCooldownTicks = 100;
    }

    static boolean hasOtherPlayerNearby(LocalPlayer self, ClientLevel level, double range) {
        AABB box = self.getBoundingBox().inflate(range);
        List<Player> found = level.getEntitiesOfClass(Player.class, box,
                p -> p != self && p.isAlive() && self.distanceTo(p) <= range + 0.5);
        return !found.isEmpty();
    }

    public static void afterAxeAttackEntity(LocalPlayer player, Entity target) {
        if (!moduleBrokenShieldEnabled) {
            return;
        }
        ItemStack main = player.getMainHandItem();
        if (!(main.getItem() instanceof AxeItem)) {
            return;
        }

        int axeSlot = player.getInventory().getSelectedSlot();

        int maceSlot = findMace(player);
        if (maceSlot == -1) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        player.getInventory().setSelectedSlot(maceSlot);
        mc.gameMode.attack(player, target);
        player.swing(InteractionHand.MAIN_HAND);
        player.getInventory().setSelectedSlot(axeSlot);
    }

    static int findMace(LocalPlayer player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i).is(Items.MACE)) {
                return i;
            }
        }
        return -1;
    }

    public static LivingEntity findNearestMeleeTarget(LocalPlayer player) {
        if (!(player.level() instanceof ClientLevel level)) {
            return null;
        }
        double cap = Math.min(auraRange, player.entityInteractionRange());
        AABB box = player.getBoundingBox().inflate(cap);
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box, e ->
                e != player && e.isAlive() && player.distanceTo(e) <= cap + 0.25
        );
        LivingEntity best = null;
        double bestSq = Double.MAX_VALUE;
        for (LivingEntity e : list) {
            double d = player.distanceToSqr(e);
            if (d < bestSq) {
                bestSq = d;
                best = e;
            }
        }
        return best;
    }
}
