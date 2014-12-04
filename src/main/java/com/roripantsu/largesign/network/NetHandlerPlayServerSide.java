package com.roripantsu.largesign.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.WorldServer;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *handle opening Large Sign editor at server side
 *@author ShenTeng Tu(RoriPantsu)
 */
public class NetHandlerPlayServerSide extends NetHandlerPlayServer {
	private PacketPipeline pipeline;
	private EntityPlayerMP playerEntity;
	private MinecraftServer serverController;

	public NetHandlerPlayServerSide(MinecraftServer serverController,
			EntityPlayerMP playerEntity, NetworkManager networkManager) {
		super(serverController, networkManager, playerEntity);
		this.serverController = serverController;
		this.playerEntity = playerEntity;
		this.pipeline = Mod_LargeSign.proxy.packetPipeline;

	}

	public PacketPipeline getPipeline() {
		return pipeline;
	}

	public EntityPlayerMP getPlayerEntity() {
		return this.playerEntity;
	}

	public void handleUpdateLargeSign(CPacketUpdateLargeSign thePacket) {
		this.playerEntity.func_143004_u();
		WorldServer worldserver = this.serverController
				.worldServerForDimension(this.playerEntity.dimension);
		int x=thePacket.getXCoordinate();
		int y=thePacket.getYCoordinate();
		int z=thePacket.getZCoordinate();
		
		if (worldserver.blockExists(x,y,z)) {
			TileEntity tileentity = worldserver.getTileEntity(x,y,z);
			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileEntityLargeSign = (TileEntityLargeSign) tileentity;

				if (!tileEntityLargeSign.isEditable()
						|| tileEntityLargeSign.getEntityPlayer() != this.playerEntity) {
					this.serverController.logWarning("Player "
							+ this.playerEntity.getCommandSenderName()
							+ " just tried to change non-editable sign");
					return;
				}
			

				boolean flag = true;

				if (thePacket.geTheString()[0].length() > 256) {
					flag = false;
				} else {
					for (int i = 0; i < thePacket.geTheString()[0].length(); ++i) {
						if (!ChatAllowedCharacters.isAllowedCharacter(thePacket
								.geTheString()[0].charAt(i))) {
							flag = false;
						}
					}
				}

				if (!flag) {
					thePacket.geTheString()[0] = "\u0000";
				}

				tileEntityLargeSign.readFromNBT(thePacket.getMainNBTTC());
				tileEntityLargeSign.markDirty();
				worldserver.markBlockForUpdate(x,y,z);
				
			}
		}
	}

}
