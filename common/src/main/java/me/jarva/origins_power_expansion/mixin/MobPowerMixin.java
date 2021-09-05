package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Mob.class)
public abstract class MobPowerMixin extends LivingEntity {
    protected MobPowerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        throw new AssertionError("MobMixin constructor called.");
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"))
    private LivingEntity modifyTarget(LivingEntity target) {
        if (level.isClientSide() || !(target instanceof Player)) {
            return target;
        }

        List<MobsIgnorePower> powers = OriginComponent.getPowers(target, MobsIgnorePower.class);
        boolean groupPowerMatch = powers.stream().anyMatch(power -> power.getMobGroups().contains(this.getMobType()));
        if (groupPowerMatch) {
            return null;
        }

        boolean mobPowerMatch = powers.stream().anyMatch(power -> power.getMobTypes().contains(this.getType()));
        if (mobPowerMatch) {
            return null;
        }

        return target;
    }
}
