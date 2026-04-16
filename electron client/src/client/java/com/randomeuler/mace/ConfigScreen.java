package com.randomeuler.mace;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private static final double RANGE_MIN = 3.0;
    private static final double RANGE_MAX = 16.0;

    private final Screen parent;

    public ConfigScreen() {
        this(null);
    }

    public ConfigScreen(Screen parent) {
        super(Component.translatable("screen.mace.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y0 = this.height / 2 - 55;

        this.addRenderableWidget(
                Button.builder(auraLabel(), b -> {
                    MaceMod.auraRedirectEnabled = !MaceMod.auraRedirectEnabled;
                    b.setMessage(auraLabel());
                }).bounds(cx - 100, y0, 200, 20).build()
        );

        double rangeSpan = RANGE_MAX - RANGE_MIN;
        double initial = (MaceMod.auraRange - RANGE_MIN) / rangeSpan;

        AbstractSliderButton rangeSlider = new AbstractSliderButton(
                cx - 100,
                y0 + 28,
                200,
                20,
                Component.empty(),
                initial
        ) {
            {
                updateMessage();
            }

            @Override
            protected void updateMessage() {
                MaceMod.auraRange = RANGE_MIN + this.value * rangeSpan;
                this.setMessage(Component.translatable("screen.mace.config.aura_range", String.format("%.1f", MaceMod.auraRange)));
            }

            @Override
            protected void applyValue() {
                updateMessage();
            }
        };
        this.addRenderableWidget(rangeSlider);

        this.addRenderableWidget(
                Button.builder(Component.translatable("gui.done"), b -> this.onClose())
                        .bounds(cx - 40, y0 + 80, 80, 20).build()
        );
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private static Component auraLabel() {
        return Component.translatable(
                MaceMod.auraRedirectEnabled ? "screen.mace.config.aura.on" : "screen.mace.config.aura.off"
        );
    }
}
