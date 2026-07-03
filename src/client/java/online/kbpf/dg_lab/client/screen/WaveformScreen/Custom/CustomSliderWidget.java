package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import static online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen.list;

public abstract class CustomSliderWidget extends AbstractSliderButton {

    public CustomSliderWidget(int x, int y, int width, int height, Component text, double value) {
        super(x, y, width, height, text, value);
    }

    public void setValue(int value){

        this.setMessage(Component.literal(String.valueOf(value)));
        this.value = (double) value / 100;
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractWidgetRenderState(context, mouseX, mouseY, delta);
        Minecraft minecraftClient = Minecraft.getInstance();
        int i = this.active ? 16777215 : 10526880;
        context.text(minecraftClient.font, this.getMessage(), this.getX() + this.getWidth(), this.getY(), i | Mth.ceil(this.alpha * 255.0F) << 24);


    }




    @Override
    protected abstract void updateMessage();

    @Override
    protected abstract void applyValue();


}
