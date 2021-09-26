package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.powers.ActionOnEquip;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World level) {
        super(type, level);
        throw new AssertionError("MobMixin constructor called.");
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"))
    private LivingEntity modifyTarget(LivingEntity target) {
        if (world.isClient() || !(target instanceof PlayerEntity)) {
            return target;
        }

        List<MobsIgnorePower> powers = OriginComponent.getPowers(target, MobsIgnorePower.class);
        boolean shouldIgnore = powers.stream().anyMatch(power -> power.shouldIgnore(this));

        return shouldIgnore ? null : target;
    }

    @Inject(at = @At("HEAD"), method = "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
    private void equipStack(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (stack.isEmpty()) return;

        List<ActionOnEquip> powers = OriginComponent.getPowers(this, ActionOnEquip.class);
        powers.forEach(power -> power.onEquipStack(slot, stack));
    }
}
