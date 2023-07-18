package com.beesechurger.flyingfamiliars.networking;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.networking.packet.BEItemStackS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.EntityListS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.SoulWandSelectC2SPacket;

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
		
		net.messageBuilder(BEItemStackS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(BEItemStackS2CPacket::new)
				.encoder(BEItemStackS2CPacket::toBytes)
				.consumer(BEItemStackS2CPacket::handle)
				.add();
		
		net.messageBuilder(BEProgressS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(BEProgressS2CPacket::new)
				.encoder(BEProgressS2CPacket::toBytes)
				.consumer(BEProgressS2CPacket::handle)
				.add();
		
		net.messageBuilder(EntityListS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(EntityListS2CPacket::new)
				.encoder(EntityListS2CPacket::toBytes)
				.consumer(EntityListS2CPacket::handle)
				.add();
		
		net.messageBuilder(SoulWandSelectC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(SoulWandSelectC2SPacket::new)
				.encoder(SoulWandSelectC2SPacket::toBytes)
				.consumer(SoulWandSelectC2SPacket::handle)
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
