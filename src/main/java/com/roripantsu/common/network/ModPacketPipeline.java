package com.roripantsu.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Transfer Manager of Mod packets.
 *Register, en/decode, and send the packets.
 *@author ShenTeng Tu(RoriPantsu)
 */
@Sharable
public class ModPacketPipeline {

	private static final Logger logger = LogManager.getLogger();
	private FMLEventChannel channel;
	private String channelName;
	private Class<? extends INetHandler> clientNetHandler;
	private Class<? extends INetHandler> serverNetHandler;
	private boolean hasPostedInitialise = false;
	private List<Class<? extends Packet>> packetsList = new ArrayList<Class<? extends Packet>>();

	public ModPacketPipeline(String modID, IModPacketEventHandler eventHandler, 
			Class<? extends INetHandler> clientHandler, Class<? extends INetHandler> serverHandler) {
		channelName =  modID;
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
		channel.register(eventHandler);
		clientNetHandler = clientHandler;
		serverNetHandler = serverHandler;
		
		
	}

	public void decode(FMLProxyPacket proxyPacket) throws Exception {

		ByteBuf payload = proxyPacket.payload();
		byte discriminator = payload.readByte();
		Class<? extends Packet> theClass = this.packetsList.get(discriminator);

		if (theClass == null){
			logger.error("The packet can not be decoded ...");
			throw new NullPointerException("No packet registered for discriminator: " + discriminator);
		}

		Packet thePacket = theClass.newInstance();
		thePacket.readPacketData(new PacketBuffer(payload.slice()));
		
		switch (proxyPacket.getTarget()) {
		case CLIENT:
			thePacket.processPacket(clientNetHandler.newInstance());
			break;
		case SERVER:
			thePacket.processPacket(serverNetHandler.newInstance());
			break;
		}

	}

	public void encode(IModPacket thePacket, List<FMLProxyPacket> out) throws Exception {
		
		if (!this.packetsList.contains(thePacket.getClass())){
			logger.error("The packet can not be encoded ...");
			throw new NullPointerException("No Packet Registered for: "+ thePacket.getClass().getCanonicalName());
		}
		
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		Class<? extends Packet> theClass = thePacket.getClass();
		byte discriminator = (byte) this.packetsList.indexOf(theClass);
		
		packetBuffer.writeByte(discriminator);
		thePacket.writePacketData(packetBuffer);
		FMLProxyPacket proxyPacket = new FMLProxyPacket(packetBuffer,channelName);
		proxyPacket.setTarget(thePacket.getTarget());
		out.add(proxyPacket);
	}
	
	public void postInitialise() {
		if (this.hasPostedInitialise)
			return;

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
		
		this.hasPostedInitialise = true;
		logger.info(getClass().getSimpleName()+"posetd initialise.");
	}

	public void registerPacket(Class<? extends Packet> theClass) {
		if (this.hasPostedInitialise)
			logger.debug(getClass().getSimpleName()+": has posted initialise");
		else if (this.packetsList.contains(theClass))
			logger.debug(getClass().getSimpleName()+":"+theClass.getSimpleName()+" has registered.");
		else
			this.packetsList.add(theClass);

	}
	
	public void registerPacket(List<Class<? extends Packet>> list) {
		for(Class<? extends Packet> theClass: list)
			registerPacket(theClass);
	}
	
	/**
	 * Server send to a specific player.
	 */
	public void sendToPlayer(FMLProxyPacket pkt, EntityPlayerMP player) {
		this.channel.sendTo(pkt, player);
	}
	
	/**
	 * Server send to all on the server.
	 */
	public void sendToAll(FMLProxyPacket pkt) {
		this.channel.sendToAll(pkt);
	}
	
	/**
	 * Server send to all around a point.
	 */
	public void sendToAllAround(FMLProxyPacket pkt,
			NetworkRegistry.TargetPoint point) {
		this.channel.sendToAllAround(pkt, point);
	}
	
	/**
	 * Server send to all in the dimension.
	 */
	public void sendToDimension(FMLProxyPacket pkt, int dimensionId) {
		this.channel.sendToDimension(pkt, dimensionId);
	}
	
	/**
	 * Cileint send to the server.
	 */
	public void sendToServer(FMLProxyPacket pkt) {
		this.channel.sendToServer(pkt);
	}
		
}
