package online.kbpf.dg_lab.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface ClientPlayerEntityAccessor {
    @Accessor("flashOnSetHealth")
    boolean getHealthInitialized();

    @Accessor("flashOnSetHealth")
    void setHealthInitialized(boolean value);
}
