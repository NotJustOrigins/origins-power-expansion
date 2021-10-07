package me.jarva.origins_power_expansion.mixin;

import me.jarva.origins_power_expansion.access.ItemStackEntity;
import me.jarva.origins_power_expansion.powers.ModifyEnchantmentLevelPower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackEntity {
    @Shadow private CompoundTag tag;
    @Unique
    public Entity entity;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void cacheEntity(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (this.getEntity() == null) this.setEntity(entity);
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCooldown(I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void copyEntity(CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack) {
        if (this.entity != null) {
            ((ItemStackEntity) (Object) itemStack).setEntity(this.getEntity());
        }
    }

    @Inject(method = "addEnchantment", at = @At(value = "TAIL"))
    private void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
        ModifyEnchantmentLevelPower.updateEnchantments((ItemStack) (Object) this);
    }

    public void setEntity(Entity entity) { this.entity = entity; }

    public Entity getEntity() {
        return this.entity;
    }
}
