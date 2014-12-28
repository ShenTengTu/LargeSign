package com.roripantsu.largesign.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.packet.CPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *handle opening Large Sign editor at server side
 *@author ShenTeng Tu(RoriPantsu)
 */
public class NetHandlerPlayServerSide extends NetHandlerPlayServer {

	private static final MinecraftServer mcServer = MinecraftServer.getServer();
	private static final NetworkManager networkManager;
	private static final EntityPlayerMP playerEntity;
	static{
		NetHandlerPlayServer netHandler = 
				(NetHandlerPlayServer) NetworkRegistry.INSTANCE
				.getChannel(Mod_LargeSign.MODID, Side.SERVER)
				.attr(NetworkRegistry.NET_HANDLER).get();
		playerEntity = netHandler.playerEntity;
		networkManager = netHandler.netManager;
	}

	public NetHandlerPlayServerSide() {
		super(mcServer, networkManager, playerEntity);


	}

	public EntityPlayerMP getPlayerEntity() {
		return playerEntity;
	}

	public MinecraftServer getServerController() {
		return mcServer;
	}

	public void handleUpdateLargeSign(CPacketUpdateLargeSign thePacket) {
		playerEntity.markPlayerActive();
		WorldServer worldserver = mcServer
				.worldServerForDimension(playerEntity.dimension);
		int x=thePacket.getBlockPos().getX();
		int y=thePacket.getBlockPos().getY();
		int z=thePacket.getBlockPos().getZ();
		
		//1.7.10 World.blockExists(int,int,int)
		boolean isBlockExist = y >= 0 && y < 256 ? worldserver.chunkExists(x >> 4, z >> 4) : false;
		//end
		
		if (isBlockExist) {
			TileEntity tileentity = worldserver.getTileEntity(new BlockPos(x,y,z));
			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileEntityLargeSign = (TileEntityLargeSign) tileentity;

				if (!tileEntityLargeSign.isEditable()
						|| tileEntityLargeSign.getEntityPlayer() != playerEntity) {
					mcServer.logWarning("Player "
							+ playerEntity.getName()
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
				worldserver.markBlockForUpdate(new BlockPos(x,y,z));
				
			}
		}
	}

}
