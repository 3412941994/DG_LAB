package online.kbpf.dg_lab.client;

import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.command.Default;
import online.kbpf.dg_lab.client.Config.ModConfig;
import online.kbpf.dg_lab.client.Config.StrengthConfig;
import online.kbpf.dg_lab.client.Config.WaveformConfig;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import online.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import java.net.InetSocketAddress;
import java.util.Map;



public class Dg_labClient implements ClientModInitializer {

    public static webSocketServer webSocketServer = null;
    public static StrengthConfig strengthConfig = new StrengthConfig();
    public static final ModConfig modConfig = ModConfig.loadJson();
    public static Map<String, Waveform> waveformMap = WaveformConfig.LoadWaveform();
    public static boolean twoPlayerMode = false;
    public static String secondPlayer = "null";
    public static int secondPlayerQuitStrength = 200;

    private static KeyMapping keyBinding;
    private final Screen configScreen = new ConfigScreen();




    @Override
    public void onInitializeClient() {



        //注册连接的服务器
        webSocketServer = new webSocketServer(new InetSocketAddress(modConfig.getServerPort()));

        strengthConfig = online.kbpf.dg_lab.client.Config.StrengthConfig.loadJson();

        DGWaveformTool.updateDuration();

        keyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "打开配置界面",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("dg_lab", "general"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {

                client.setScreenAndShow(configScreen);
            }
        });
        //指令定义
        Default.register(modConfig, strengthConfig, webSocketServer);

        if(modConfig.getAutoStartWebSocketServer()) webSocketServer.start();
    }



}
