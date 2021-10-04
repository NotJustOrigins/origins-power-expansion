package me.jarva.origins_power_expansion.registry.actions;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import me.jarva.origins_power_expansion.actions.block.BonemealAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class BlockActionRegistry {
    public static void register() {
        register(BonemealAction.getFactory());
    }

    @ExpectPlatform
    private static void register(ActionFactory<Triple<World, BlockPos, Direction>> actionFactory) {
        throw new AssertionError();
    }
}
