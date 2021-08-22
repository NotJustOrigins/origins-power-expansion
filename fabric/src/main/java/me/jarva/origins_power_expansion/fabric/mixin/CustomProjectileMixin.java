package me.jarva.origins_power_expansion.fabric.mixin;

import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.entity.CustomProjectile;
import me.jarva.origins_power_expansion.fabric.EntitySpawnPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CustomProjectile.class)
public abstract class CustomProjectileMixin extends ThrowableItemProjectile {
    public CustomProjectileMixin(EntityType<? extends ThrowableItemProjectile> arg, Level arg2) { super(arg, arg2); }

    @Override
    public Packet getAddEntityPacket() {
        return EntitySpawnPacket.create(this, OriginsPowerExpansion.identifier("custom_projectile"));
    }
}
