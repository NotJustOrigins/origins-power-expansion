package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.powers.ActionOnEquipPower;
import me.jarva.origins_power_expansion.powers.AerialAffinityPower;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> player, World level) {
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

    @Redirect(method = {"Lnet/minecraft/entity/player/PlayerEntity;getDigSpeed(Lnet/minecraft/block/BlockState;)F","Lnet/minecraft/entity/player/PlayerEntity;getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F"}, at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z", opcode = Opcodes.GETFIELD), remap = false)
    private boolean hasAirAffinity(PlayerEntity instance) {
        return OriginComponent.hasPower(instance, AerialAffinityPower.class) || instance.isOnGround();
    }

    @Inject(method = "equipStack", at = @At(value = "TAIL"))
    public void equipStack(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (slot.getType() != EquipmentSlot.Type.ARMOR || !slot.equals(EquipmentSlot.OFFHAND)) return;

        OriginComponent.getPowers(this, ActionOnEquipPower.class).forEach(power -> power.fireAction(slot, stack));
    }
}
