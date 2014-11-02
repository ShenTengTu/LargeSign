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

		if (worldserver.blockExists(thePacket.getXCoordinate(),
				thePacket.getYCoordinate(), thePacket.getZCoordinate())) {
			TileEntity tileentity = worldserver.getTileEntity(
					thePacket.getXCoordinate(), thePacket.getYCoordinate(),
					thePacket.getZCoordinate());

			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileentitylagesign = (TileEntityLargeSign) tileentity;

				if (!tileentitylagesign.isEditable()
						|| tileentitylagesign.getEntityPlayer() != this.playerEntity) {
					this.serverController.logWarning("Player "
							+ this.playerEntity.getCommandSenderName()
							+ " just tried to change non-editable sign");
					return;
				}
			}

			boolean flag = true;

			if (thePacket.geTheString()[0].length() > 64) {
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

			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) tileentity;
				tileentitylargesign.readFromNBT(thePacket.getNBTTC());
				tileentitylargesign.markDirty();
				worldserver.markBlockForUpdate(thePacket.getXCoordinate(),
						thePacket.getYCoordinate(), thePacket.getZCoordinate());
			}
		}
	}

}
