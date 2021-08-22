package me.jarva.origins_power_expansion.fabric;

import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.OriginsPowerExpansionClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import static me.jarva.origins_power_expansion.registry.EntitiesRegistry.CUSTOM_PROJECTILE_ENTITY;

@Environment(EnvType.CLIENT)
public class OriginsPowerExpansionFabricClient implements ClientModInitializer {
    public static void receiveEntityPacket(ResourceLocation PacketID) {
        ClientPlayNetworking.registerGlobalReceiver(PacketID, (client, handler, byteBuf, responseSender) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.byId(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUUID();
            int entityId = byteBuf.readVarInt();
            Vec3 pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            client.execute(() -> {
                if (Minecraft.getInstance().level == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(Minecraft.getInstance().level);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.setPacketCoordinates(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.xRot = pitch;
                e.yRot = yaw;
                e.setId(entityId);
                e.setUUID(uuid);
                Minecraft.getInstance().level.putNonPlayerEntity(entityId, e);
            });
        });
    }

    public void onInitializeClient() {
        OriginsPowerExpansionClient.register();
        EntityRendererRegistry.INSTANCE.register(CUSTOM_PROJECTILE_ENTITY, (dispatcher, context) -> new ThrownItemRenderer(dispatcher, context.getItemRenderer()));
        OriginsPowerExpansionFabricClient.receiveEntityPacket(OriginsPowerExpansion.identifier("custom_projectile"));
    }
}
