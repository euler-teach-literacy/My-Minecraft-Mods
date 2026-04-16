package com.macefast;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class ClientEvents {

    private static int comboStage = 0;
    private static int delay = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Keybinds.guiKey.wasPressed()) client.setScreen(new ClickGUI());
            while (Keybinds.comboKey.wasPressed()) {
                comboStage = 1;
                delay = 0;
            }
            if (comboStage > 0) handleCombo(client);
        });
    }

    private static void handleCombo(MinecraftClient client) {
        if (client.player == null) return;
        delay++;

        switch (comboStage) {
            case 1:
                if (!Settings.maceFastEnabled) { comboStage = 0; return; }
                client.player.getInventory().selectedSlot = Settings.axeSlot;
                attack(client);
                comboStage = 2;
                delay = 0;
                break;
            case 2:
                if (delay > Settings.delay) {
                    client.player.jump();
                    client.player.getInventory().selectedSlot = Settings.maceSlot;
                    attack(client);
                    comboStage = 0;
                }
                break;
        }
    }

    private static void attack(MinecraftClient client) {
        if (client.interactionManager != null && client.targetedEntity != null) {
            client.interactionManager.attackEntity(client.player, client.targetedEntity);
            client.player.swingHand(client.player.getActiveHand());
        }
    }
}