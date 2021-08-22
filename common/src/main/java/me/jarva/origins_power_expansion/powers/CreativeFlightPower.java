package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.ValueModifyingPower;
import io.github.apace100.origins.util.AttributeUtil;
import net.minecraft.world.entity.player.Player;

public class CreativeFlightPower extends ValueModifyingPower {
    private Float defaultSpeed;

    public CreativeFlightPower(PowerType<?> type, Player player) {
        super(type, player);
        this.defaultSpeed = player.abilities.getFlyingSpeed();
        this.setTicking(true);
    }

    private void startFlying(Boolean shouldFly) {
        if (!player.isCreative() && !player.isSpectator()) {
            if (shouldFly) {
                float flySpeed = (float) AttributeUtil.sortAndApplyModifiers(this.getModifiers(), this.defaultSpeed);
                player.abilities.mayfly = true;
                player.abilities.setFlyingSpeed(flySpeed);
            } else {
                player.abilities.flying = false;
                player.abilities.mayfly = false;
                player.abilities.setFlyingSpeed(this.defaultSpeed);
            }
            player.onUpdateAbilities();
        }
    }

    @Override
    public void onAdded() {
        startFlying(true);
    }

    @Override
    public void tick() {
        if (!player.abilities.mayfly && this.isActive()) {
            startFlying(true);
        } else {
            startFlying(false);
        }
    }

    @Override
    public void onRemoved() {
        startFlying(false);
    }
}