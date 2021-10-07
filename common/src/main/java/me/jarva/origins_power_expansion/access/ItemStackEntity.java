package me.jarva.origins_power_expansion.access;

import net.minecraft.entity.Entity;

public interface ItemStackEntity {
    void setEntity(Entity entity);
    Entity getEntity();
}
