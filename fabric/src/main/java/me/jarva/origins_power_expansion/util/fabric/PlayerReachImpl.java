package me.jarva.origins_power_expansion.util.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerReachImpl {
    public static double getReach(PlayerEntity player) {
        return ReachEntityAttributes.getReachDistance(player, player.isCreative() ? 5.0D : 4.5D);
    }
}
