package online.kbpf.dg_lab.client.screen.WaveformScreen;

import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen;


import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static online.kbpf.dg_lab.client.screen.ConfigScreen.*;
import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class WaveformListWidget extends ContainerObjectSelectionList<WaveformListWidget.Entry> {

    //列表项目内容
    private final int width;


    public WaveformListWidget(Minecraft minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
        this.width = width;
    }


    //修改左右宽度
    @Override
    public int getRowLeft() {
        return this.getX(); // 从屏幕最左侧开始
    }
    @Override
    public int getRowWidth() {
        return this.width; // 宽度设置为屏幕宽度
    }
    @Override
    protected int scrollBarX() {
        return this.getRight() - 6; // 滚动条紧贴右侧
    }


    public void addWaveformEntry(online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformListWidget.Entry entry) {
        this.addEntry(entry);
    }

    public static class Entry extends ContainerObjectSelectionList.Entry<online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformListWidget.Entry> {
        Minecraft client = Minecraft.getInstance();
        private final EditBox waveformDataText; //文字输入框
        private final Button copyButton, pasteButton, testButton, customButton;          //按钮
        private final Font textRenderer;        //文本渲染参数
        private final Component text;                        //文本

        private Waveform waveform = new Waveform();

        public Entry(Font textRenderer, Component text, String key) {
            //设置单个项目相关内容


            if(waveformMap.containsKey(key)) waveform = waveformMap.get(key);


            waveformDataText = new EditBox(textRenderer, 100, ButtonHeight, Component.literal(""));
            waveformDataText.setMaxLength(100000);

            waveformDataText.setValue(waveform.getWaveform());



            waveformDataText.setHint(Component.literal("输入波形代码").withColor(0xaaaaaa));

            waveformDataText.setResponder(inputText -> {
                waveform.setWaveform(inputText);
            });

            customButton = new Button.Builder(Component.literal("✏"), button -> {
                Screen customScreen = new CustomScreen(key);
                client.setScreenAndShow(customScreen);
            }).tooltip(Tooltip.create(Component.literal("点击修改波形"))).build();

            copyButton = new Button.Builder(Component.literal("\uD83D\uDCC4"), button -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(waveformDataText.getValue());
            }).tooltip(Tooltip.create(Component.literal("点击复制波形代码"))).build();

            pasteButton = new Button.Builder(Component.literal("\uD83D\uDCCB"), button -> {
                String clipboardText = Minecraft.getInstance().keyboardHandler.getClipboard();
                waveformDataText.setValue(clipboardText);
            }).tooltip(Tooltip.create(Component.literal("点击粘贴波形代码"))).build();

            testButton = new Button.Builder(Component.literal("\uD83D\uDCE8"), button -> {
                webSocketServer.sendDGWaveForm(waveformDataText.getValue(), 1);
            }).tooltip(Tooltip.create(Component.literal("发送到终端1通道"))).build();


            this.textRenderer = textRenderer;
            this.text = text;

        }


        //确保点击/交互被正确传递
        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(waveformDataText, testButton, customButton);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(waveformDataText, testButton, customButton);
        }


        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            //渲染相关
            //文本框位置宽高

            int x = getContentX();
            int y = getContentY();
            int entryWidth = getContentWidth();

            waveformDataText.setRectangle(x + (int) (entryWidth * 0.3), ButtonHeight, x + (int) (entryWidth * 0.4), y);
            waveformDataText.extractRenderState(context, mouseX, mouseY, tickDelta);

            customButton.setRectangle(15, ButtonHeight, waveformDataText.getX() + waveformDataText.getWidth(), y);
            customButton.extractRenderState(context, mouseX, mouseY, tickDelta);

//            copyButton.setDimensionsAndPosition(15, 20, customButton.getX() + 15, y);
//            copyButton.render(context, mouseX, mouseY, tickDelta);
//
//
//            pasteButton.setDimensionsAndPosition(15, 20, copyButton.getX() + 15, y);
//            pasteButton.render(context, mouseX, mouseY, tickDelta);

            testButton.setRectangle(15, ButtonHeight, customButton.getX() + 15, y);
            testButton.extractRenderState(context, mouseX, mouseY, tickDelta);


            context.text(textRenderer, this.text, x + (int) (entryWidth * 0.15), y + 5, 0xffffff);

            int duration = DGWaveformTool.checkAndCountValidSubstrings(waveformDataText.getValue());
            if(duration == 0)
                context.text(textRenderer, "ERROR", testButton.getX() + 20, y + 5, 0xFF0000);
            else context.text(textRenderer, (duration * 100) + "ms", testButton.getX() + 15, y + 5, 0xFFFFFF);

        }


    }

}
