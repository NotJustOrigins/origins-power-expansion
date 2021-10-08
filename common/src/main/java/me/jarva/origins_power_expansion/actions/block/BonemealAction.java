package me.jarva.origins_power_expansion.actions.block;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

/** md
---
title: Bonemeal (Block Action)
date: 2021-10-08
---

# Bonemeal

[Block Action](../)

Applies bonemeal to the block.

Type ID: `ope:bonemeal`

### Example
```json
"block_action": {
    "type": "ope:bonemeal"
}
```
 */
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class BonemealAction {
    public static void action(SerializableData.Instance data, Triple<World, BlockPos, Direction> block) {
        World world = block.getLeft();
        BlockPos blockPos = block.getMiddle();

        BoneMealItem.useOnFertilizable(ItemStack.EMPTY, world, blockPos);
        if (world.isClient) {
            BoneMealItem.createParticles(world, blockPos, 0);
            return;
        };
    }

    public static ActionFactory<Triple<World, BlockPos, Direction>> getFactory() {
        return new ActionFactory<>(OriginsPowerExpansion.identifier("bonemeal"),
                new SerializableData(),
                BonemealAction::action
        );
    }
}
