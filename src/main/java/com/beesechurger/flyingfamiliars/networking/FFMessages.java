package com.beesechurger.flyingfamiliars.networking;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.networking.packet.ItemStackSyncS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.ProgressSyncS2CPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class FFMessages 
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
		
		net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(ItemStackSyncS2CPacket::new)
				.encoder(ItemStackSyncS2CPacket::toBytes)
				.consumer(ItemStackSyncS2CPacket::handle)
				.add();
		
		net.messageBuilder(ProgressSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(ProgressSyncS2CPacket::new)
				.encoder(ProgressSyncS2CPacket::toBytes)
				.consumer(ProgressSyncS2CPacket::handle)
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
