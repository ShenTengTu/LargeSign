package com.roripantsu.largesign.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Controling communication for client and server
 *@author ShenTeng Tu(RoriPantsu)
 */
@Sharable
public class PacketPipeline {

	private static final Logger logger = LogManager.getLogger();
	private static PacketPipeline packetPipeline;

	public static PacketPipeline getPacketPipeline() {
		return packetPipeline;
	}

	private FMLEventChannel channels;
	private BiMap<Integer, String> channelsList = HashBiMap.create();
	private boolean isPostInitialised = false;

	private LinkedList<Class<? extends Packet>> packetsList = new LinkedList<Class<? extends Packet>>();

	public PacketPipeline() {
		packetPipeline = this;
	}

	public void addNewChannel(int channelkey, String channelName) {
		if (this.channelsList.containsKey(channelkey)
				|| this.channelsList.containsValue(channelName)) {
			throw new RuntimeException("That channel is already exist");
		} else {
			this.channelsList.put(channelkey, channelName);
			this.channels = NetworkRegistry.INSTANCE
					.newEventDrivenChannel(channelName);
		}
	}

	public void decode(FMLProxyPacket proxyPacket, int channelKey,
			List<Object> out) throws Exception {

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
					.getChannel(this.channelsList.get(channelKey), Side.SERVER)
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

	public void encode(Packet thePacket, int channelKey, List<Object> out)
			throws Exception {
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		Class<? extends Packet> theClass = thePacket.getClass();

		if (!this.packetsList.contains(thePacket.getClass())) {
			throw new NullPointerException("No Packet Registered for: "
					+ thePacket.getClass().getCanonicalName());
		}

		byte discriminator = (byte) this.packetsList.indexOf(theClass);
		packetBuffer.writeByte(discriminator);
		thePacket.writePacketData(packetBuffer);
		FMLProxyPacket proxyPacket = new FMLProxyPacket(packetBuffer.copy(),
				this.channelsList.get(channelKey));
		out.add(proxyPacket);
	}
	
	@SideOnly(Side.CLIENT)
	public EntityPlayer getEntityPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	public List<Object> getPacketsList() {
		List<Object> list = new LinkedList<Object>();
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
		this.channels.register(obj);
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
		this.channels.sendTo(pkt, player);
	}

	public void sendToAll(FMLProxyPacket pkt) {
		this.channels.sendToAll(pkt);
	}

	public void sendToAllAround(FMLProxyPacket pkt,
			NetworkRegistry.TargetPoint point) {
		this.channels.sendToAllAround(pkt, point);
	}

	public void sendToDimension(FMLProxyPacket pkt, int dimensionId) {
		this.channels.sendToDimension(pkt, dimensionId);
	}

	public void sendToServer(FMLProxyPacket pkt) {
		this.channels.sendToServer(pkt);
	}

}
