package com.randomeuler.mace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MaceModuleScreen extends Screen {

    private static final int PANEL_W = 280;
    private static final int PANEL_H = 200;

    public MaceModuleScreen() {
        super(Component.translatable("screen.mace.module.title"));
    }

    public static void tryRebuild(Minecraft mc) {
        if (mc.screen instanceof MaceModuleScreen s) {
            s.rebuildWidgets();
        }
    }

    @Override
    protected void init() {
        int px = this.width / 2 - PANEL_W / 2;
        int py = this.height / 2 - PANEL_H / 2;
        int y = py + 36;

        this.addRenderableWidget(
                Button.builder(brokenShieldLabel(), b -> {
                    MaceMod.moduleBrokenShieldEnabled = !MaceMod.moduleBrokenShieldEnabled;
                    b.setMessage(brokenShieldLabel());
                }).bounds(px + 12, y, PANEL_W - 24, 22).build()
        );
        y += 28;

        this.addRenderableWidget(
                Button.builder(Component.translatable("screen.mace.module.broken_shield_settings"), b ->
                        this.minecraft.setScreen(new ConfigScreen(this))
                ).bounds(px + 12, y, PANEL_W - 24, 20).build()
        );
        y += 28;

        this.addRenderableWidget(
                Button.builder(autoBackLabel(), b -> {
                    MaceMod.moduleAutoBackEnabled = !MaceMod.moduleAutoBackEnabled;
                    b.setMessage(autoBackLabel());
                }).bounds(px + 12, y, PANEL_W - 24, 22).build()
        );
        y += 30;

        this.addRenderableWidget(
                Button.builder(Component.translatable("screen.mace.module.close"), b -> this.onClose())
                        .bounds(this.width / 2 - 50, py + PANEL_H - 32, 100, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        int px = this.width / 2 - PANEL_W / 2;
        int py = this.height / 2 - PANEL_H / 2;
        graphics.fill(px - 1, py - 1, px + PANEL_W + 1, py + PANEL_H + 1, 0xFF3BA55C);
        graphics.fill(px, py, px + PANEL_W, py + PANEL_H, 0xE0101010);
        graphics.drawString(this.font, this.title, px + 10, py + 10, 0xFFFFFFFF, false);
        graphics.drawString(
                this.font,
                Component.translatable("screen.mace.module.hint_bracket"),
                px + 10,
                py + PANEL_H - 52,
                0xFFAAAAAA,
                false
        );
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static Component brokenShieldLabel() {
        return Component.translatable(
                MaceMod.moduleBrokenShieldEnabled
                        ? "screen.mace.module.broken_shield.on"
                        : "screen.mace.module.broken_shield.off"
        );
    }

    private static Component autoBackLabel() {
        return Component.translatable(
                MaceMod.moduleAutoBackEnabled
                        ? "screen.mace.module.auto_back.on"
                        : "screen.mace.module.auto_back.off"
        );
    }
}
