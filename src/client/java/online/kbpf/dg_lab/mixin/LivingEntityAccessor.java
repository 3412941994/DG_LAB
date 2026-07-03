package online.kbpf.dg_lab.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("lastHurt")
    float getLastDamageTaken();

    @Accessor("lastHurt")
    void setLastDamageTaken(float value);


}
