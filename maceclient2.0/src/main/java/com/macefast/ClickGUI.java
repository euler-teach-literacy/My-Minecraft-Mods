package com.macefast;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.InputUtil;

public class ClickGUI extends Screen {

    private boolean waitingForKey = false;

    protected ClickGUI() { super(Text.literal("眩晕猛击")); }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 60;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("眩晕猛击: " + Settings.maceFastEnabled),
            b -> { Settings.maceFastEnabled = !Settings.maceFastEnabled;
                   b.setMessage(Text.literal("眩晕猛击: " + Settings.maceFastEnabled)); })
            .dimensions(x, y, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Axe Slot: " + (Settings.axeSlot + 1)),
            b -> { Settings.axeSlot = (Settings.axeSlot + 1) % 9;
                   b.setMessage(Text.literal("Axe Slot: " + (Settings.axeSlot + 1))); })
            .dimensions(x, y + 25, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Mace Slot: " + (Settings.maceSlot + 1)),
            b -> { Settings.maceSlot = (Settings.maceSlot + 1) % 9;
                   b.setMessage(Text.literal("Mace Slot: " + (Settings.maceSlot + 1))); })
            .dimensions(x, y + 50, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Delay: " + Settings.delay),
            b -> { Settings.delay++; if (Settings.delay > 10) Settings.delay = 1;
                   b.setMessage(Text.literal("Delay: " + Settings.delay)); })
            .dimensions(x, y + 75, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Bind GUI Key"),
            b -> waitingForKey = true)
            .dimensions(x, y + 100, 200, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKey) {
            waitingForKey = false;
            Keybinds.guiKey.setBoundKey(InputUtil.Type.KEYSYM.createFromCode(keyCode));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}