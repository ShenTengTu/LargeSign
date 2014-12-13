package com.roripantsu.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.roripantsu.common.ModInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

/**
 *Controling communication for client and server
 *@author ShenTeng Tu(RoriPantsu)
 */
@Sharable
public class PacketPipeline {

	private static final Logger logger = LogManager.getLogger();
	private FMLEventChannel channel;
	private boolean isPostInitialised = false;
	private List<Class<? extends Packet>> packetsList = new ArrayList<Class<? extends Packet>>();

	public PacketPipeline() {
		this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(ModInfo.MODID);
	}

	public void decode(FMLProxyPacket proxyPacket,List<Object> out) throws Exception {

		ByteBuf payload = proxyPacket.payload();
		byte discriminator = payload.readByte();
		Class<? extends Packet> theClass = this.packetsList
				.get(discriminator);

		if (theClass == null) {
			throw new NullPointerException(
					"No packet registered for discriminator: " + discriminator);
		}

		Packet thePacket = theClass.newInstance();
		thePacket.readPacketData(new PacketBuffer(payload.slice()));

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
		case CLIENT:
			NetHandlerPlayClientSide NHPCS = new NetHandlerPlayClientSide(
					Minecraft.getMinecraft(), Minecraft.getMinecraft().currentScreen,
					Minecraft.getMinecraft().getNetHandler().getNetworkManager());
			thePacket.processPacket(NHPCS);
			break;
		case SERVER:
			NetHandlerPlayServer netHandler = (NetHandlerPlayServer) NetworkRegistry.INSTANCE
					.getChannel(ModInfo.MODID, Side.SERVER)
					.attr(NetworkRegistry.NET_HANDLER).get();
			NetHandlerPlayServerSide NHPSS = new NetHandlerPlayServerSide(
					MinecraftServer.getServer(), netHandler.playerEntity,
					netHandler.netManager);
			thePacket.processPacket(NHPSS);
			break;
		default:
		}
		out.add(thePacket);
	}

	public void encode(Packet thePacket, List<Object> out) throws Exception {
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		Class<? extends Packet> theClass = thePacket.getClass();

		if (!this.packetsList.contains(thePacket.getClass())) {
			throw new NullPointerException("No Packet Registered for: "
					+ thePacket.getClass().getCanonicalName());
		}

		byte discriminator = (byte) this.packetsList.indexOf(theClass);
		packetBuffer.writeByte(discriminator);
		thePacket.writePacketData(packetBuffer);
		FMLProxyPacket proxyPacket = new FMLProxyPacket(packetBuffer.copy(),ModInfo.MODID);
		out.add(proxyPacket);
	}
	
	public List<Object> getPacketsList() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.packetsList);
		return list;
	}

	public void postInitialise() {
		if (this.isPostInitialised) {
			return;
		}

		this.isPostInitialised = true;
		Collections.sort(this.packetsList,
				new Comparator<Class<? extends Packet>>() {

					@Override
					public int compare(Class<? extends Packet> theClass1,
							Class<? extends Packet> theClass2) {
						int com = String.CASE_INSENSITIVE_ORDER.compare(
								theClass1.getCanonicalName(),
								theClass2.getCanonicalName());
						if (com == 0) {
							com = theClass1.getCanonicalName().compareTo(
									theClass2.getCanonicalName());
						}

						return com;
					}
				});
	}

	public void registeEventListener(Object obj) {
		this.channel.register(obj);
	}

	public boolean registerPacket(Class<? extends Packet> theClass) {
		if (this.packetsList.size() > 256) {
			logger.debug("packetsList.size() > 256");
			return false;
		}

		if (this.packetsList.contains(theClass)) {
			logger.debug("packetsList.contains(theClass)");
			return false;
		}

		if (this.isPostInitialised) {
			logger.debug("isPostInitialised");
			return false;
		}

		this.packetsList.add(theClass);
		return true;
	}

	public void sendTo(FMLProxyPacket pkt, EntityPlayerMP player) {
		this.channel.sendTo(pkt, player);
	}

	public void sendToAll(FMLProxyPacket pkt) {
		this.channel.sendToAll(pkt);
	}

	public void sendToAllAround(FMLProxyPacket pkt,
			NetworkRegistry.TargetPoint point) {
		this.channel.sendToAllAround(pkt, point);
	}

	public void sendToDimension(FMLProxyPacket pkt, int dimensionId) {
		this.channel.sendToDimension(pkt, dimensionId);
	}

	public void sendToServer(FMLProxyPacket pkt) {
		this.channel.sendToServer(pkt);
	}

}