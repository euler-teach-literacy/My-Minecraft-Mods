package com.macefast;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding comboKey;
    public static KeyBinding guiKey;

    public static void register() {
        comboKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.macefast.combo", GLFW.GLFW_KEY_C, "category.macefast")
        );

        guiKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.macefast.gui", GLFW.GLFW_KEY_RIGHT_SHIFT, "category.macefast")
        );
    }
}