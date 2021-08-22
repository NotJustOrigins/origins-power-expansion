package me.jarva.origins_power_expansion.forge.mixin;

import me.jarva.origins_power_expansion.entity.CustomProjectile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;

// Forge-specific Mixin.
// In Fabric, the Network API exposes the add entity packet, and in
// non-Architectury Forge, this would be done in the class itself.

// On the Forge edition of the mod, we give the Entity a proper
// spawning-in packet. Without this Mixin, the entity would appear
// to not exist for clients.

// This might not be the best way to register a spawning packet, but
// it's what Origins-Architectury does in its implementation of the
// Enderian Pearl entity.


@Mixin(CustomProjectile.class)
public abstract class CustomProjectileMixin extends ThrowableItemProjectile {
    public CustomProjectileMixin(EntityType<? extends ThrowableItemProjectile> arg, Level arg2) { super(arg, arg2); }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}