package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.packet.*;

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

		net.messageBuilder(EntryManipModeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(EntryManipModeC2SPacket::new)
				.encoder(EntryManipModeC2SPacket::toBytes)
				.consumerMainThread(EntryManipModeC2SPacket::handle)
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
