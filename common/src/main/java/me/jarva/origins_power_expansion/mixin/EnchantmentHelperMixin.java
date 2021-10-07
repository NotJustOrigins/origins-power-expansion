package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.access.ItemStackEntity;
import me.jarva.origins_power_expansion.powers.ModifyEnchantmentLevelPower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
    private static void getLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        ItemStackEntity itemStack = (ItemStackEntity) (Object) stack;
        if (itemStack != null && itemStack.getEntity() instanceof PlayerEntity) {
            int newEnchantLevel = (int) OriginComponent.modify(itemStack.getEntity(), ModifyEnchantmentLevelPower.class, cir.getReturnValue(), power -> power.doesApply(enchantment));
            cir.setReturnValue(newEnchantLevel);
        }
    }

    @Inject(method ="getEquipmentLevel", at = @At("RETURN"), cancellable = true)
    private static void getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        int originalReturn = cir.getReturnValue();
        if (originalReturn != 0) return;
        int newEnchantLevel = (int) OriginComponent.modify(entity, ModifyEnchantmentLevelPower.class, originalReturn, power -> power.doesApply(enchantment));
        cir.setReturnValue(newEnchantLevel);
    }
}
