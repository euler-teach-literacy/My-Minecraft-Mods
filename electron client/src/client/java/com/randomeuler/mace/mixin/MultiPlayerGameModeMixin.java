package com.randomeuler.mace.mixin;

import com.randomeuler.mace.MaceMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @ModifyVariable(method = "attack", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private Entity mace_redirectMeleeTarget(Entity target, Player player) {
        if (!(player instanceof LocalPlayer local)) {
            return target;
        }
        if (Minecraft.getInstance().player != local) {
            return target;
        }
        if (!MaceMod.moduleBrokenShieldEnabled || !MaceMod.auraRedirectEnabled) {
            return target;
        }
        if (!(local.getMainHandItem().getItem() instanceof AxeItem)) {
            return target;
        }
        LivingEntity nearest = MaceMod.findNearestMeleeTarget(local);
        return nearest != null ? nearest : target;
    }

    @Inject(method = "attack", at = @At("RETURN"))
    private void mace_afterAttack(Player player, Entity target, CallbackInfo ci) {
        if (!(player instanceof LocalPlayer local)) {
            return;
        }
        if (Minecraft.getInstance().player != local) {
            return;
        }
        MaceMod.afterAxeAttackEntity(local, target);
    }
}
