package me.jarva.origins_power_expansion.entity;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.powers.FireCustomProjectilePower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class CustomProjectile extends ThrowableItemProjectile {
    public boolean hasHit = false;

    public CustomProjectile(EntityType<? extends CustomProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Environment(EnvType.CLIENT)
    private ParticleOptions getParticle() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemStack);
    }

    @Environment(EnvType.CLIENT)
    public void handleEntityEvent(byte b) {
        if (b == 3) {
            ParticleOptions particleOptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.hasHit) return;
        HitResult.Type type = hitResult.getType();
        Entity owner = this.getOwner();
        if (owner == null) return;
        List<FireCustomProjectilePower> powers = OriginComponent.getPowers(owner, FireCustomProjectilePower.class);
        powers.forEach((FireCustomProjectilePower power) -> {
            if (type == HitResult.Type.ENTITY) {
                power.onHitEntity((EntityHitResult)hitResult);
            } else if (type == HitResult.Type.BLOCK) {
                power.onHitBlock(this, (BlockHitResult)hitResult);
            }
        });
        this.hasHit = true;
    }
}
