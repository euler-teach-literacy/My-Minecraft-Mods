package com.macefast;

import net.fabricmc.api.ClientModInitializer;

public class MaceFastMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Keybinds.register();
        ClientEvents.register();
        System.out.println("眩晕猛击 已加载!");
    }
}