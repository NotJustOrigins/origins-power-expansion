package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends Entity {
    protected PlayerMixin(EntityType<?> player, World level) {
        super(player, level);
        throw new AssertionError("PlayerMixin constructor called.");
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        List<CustomFootstepPower> powers = OriginComponent.getPowers(this, CustomFootstepPower.class);
        if (powers.stream().anyMatch(CustomFootstepPower::isMuted)) return;
        super.playStepSound(pos, state);
        if (powers.isEmpty()) return;
        powers.forEach(power -> power.playFootstep(this));
    }
}
