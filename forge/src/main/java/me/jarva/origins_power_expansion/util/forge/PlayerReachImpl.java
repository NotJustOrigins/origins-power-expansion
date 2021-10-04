package me.jarva.origins_power_expansion.util.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;

public class PlayerReachImpl {
    public static double getReach(PlayerEntity player) {
        return player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
    }
}
