package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.packet.client.EntityCycleC2SPacket;
import com.beesechurger.flyingfamiliars.packet.client.InventoryEntryTagMoveC2SPacket;
import com.beesechurger.flyingfamiliars.packet.client.WandEffectAttackC2SPacket;
import com.beesechurger.flyingfamiliars.packet.client.WandEffectSelectionC2SPacket;
import com.beesechurger.flyingfamiliars.packet.server.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.packet.server.SyncInventoryCarriedItemS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class FFPackets
{
	private static SimpleChannel INSTANCE;
	private static final String PROTOCOL_VERSION = "1.0";
	private static int packetID = 0;
	
	private static int id()
	{
		return packetID++;
	}
	
	public static void register()
	{
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(FlyingFamiliars.MOD_ID, "messages"))
				.networkProtocolVersion(() -> PROTOCOL_VERSION)
				.clientAcceptedVersions(s -> true)
				.serverAcceptedVersions(s -> true)
				.simpleChannel();
		
		INSTANCE = net;
		
		net.messageBuilder(BEProgressS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(BEProgressS2CPacket::new)
				.encoder(BEProgressS2CPacket::toBytes)
				.consumerMainThread(BEProgressS2CPacket::handle)
				.add();
		
		net.messageBuilder(EntityCycleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(EntityCycleC2SPacket::new)
				.encoder(EntityCycleC2SPacket::toBytes)
				.consumerMainThread(EntityCycleC2SPacket::handle)
				.add();

		net.messageBuilder(InventoryEntryTagMoveC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(InventoryEntryTagMoveC2SPacket::new)
				.encoder(InventoryEntryTagMoveC2SPacket::toBytes)
				.consumerMainThread(InventoryEntryTagMoveC2SPacket::handle)
				.add();

		net.messageBuilder(SyncInventoryCarriedItemS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(SyncInventoryCarriedItemS2CPacket::new)
				.encoder(SyncInventoryCarriedItemS2CPacket::toBytes)
				.consumerMainThread(SyncInventoryCarriedItemS2CPacket::handle)
				.add();

		net.messageBuilder(WandEffectAttackC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(WandEffectAttackC2SPacket::new)
				.encoder(WandEffectAttackC2SPacket::toBytes)
				.consumerMainThread(WandEffectAttackC2SPacket::handle)
				.add();

        net.messageBuilder(WandEffectSelectionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WandEffectSelectionC2SPacket::new)
                .encoder(WandEffectSelectionC2SPacket::toBytes)
                .consumerMainThread(WandEffectSelectionC2SPacket::handle)
                .add();
	}
	
	public static <MSG> void sendToServer(MSG message)
	{
		INSTANCE.sendToServer(message);
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
	{
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
	
	public static <MSG> void sendToClients(MSG message)
	{
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
