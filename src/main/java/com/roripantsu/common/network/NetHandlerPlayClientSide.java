package com.roripantsu.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import com.mojang.authlib.GameProfile;
import com.roripantsu.largesign.gui.GuiEditLargeSign;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.packet.SPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *handle opening Large Sign editor and updating Large Sign at client side
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class NetHandlerPlayClientSide extends NetHandlerPlayClient {

	private WorldClient clientWorldController;
	private Minecraft gameController;
	private final GuiScreen guiScreenServer;

	public NetHandlerPlayClientSide(Minecraft gameController,GuiScreen guiScreenServer, 
			NetworkManager networkManager,GameProfile gameProfile) {
		super(gameController, guiScreenServer, networkManager,gameProfile);
		this.gameController = gameController;
		this.guiScreenServer = guiScreenServer;
		this.clientWorldController = this.gameController.theWorld;
	}

	public WorldClient getClientWorldController() {
		return clientWorldController;
	}

	public Minecraft getGameController() {
		return gameController;
	}

	public GuiScreen getGuiScreenServer() {
		return guiScreenServer;
	}

	public void handleLargeSignEditorOpen(SPacketLargeSignEditorOpen thePacket) {
		int x=thePacket.getXCoordinate();
		int y=thePacket.getYCoordinate();
		int z=thePacket.getZCoordinate();
		int theMetadata=thePacket.getTheMetadata();//for Sub Block or Item
		int side=thePacket.getSide();
		
		TileEntity tileEntity = this.gameController.theWorld.getTileEntity(new BlockPos(x,y,z));
		
		if (tileEntity == null) {
			tileEntity = new TileEntityLargeSign();
			tileEntity.setWorldObj(this.clientWorldController);
			tileEntity.setPos(new BlockPos(x,y,z));
			//for Sub Block or Item
			((TileEntityLargeSign) tileEntity).setTheMetadata(theMetadata);
			((TileEntityLargeSign) tileEntity).setSide(side);
				
		}

		this.gameController.displayGuiScreen(new GuiEditLargeSign(
				(TileEntityLargeSign) tileEntity));

	}

	public void handleUpdateLargeSign(SPacketUpdateLargeSign thePacket) {
		WorldClient worldClient=this.gameController.theWorld;
		boolean flag = false;
		int x=thePacket.getxCoordinate();
		int y=thePacket.getyCoordinate();
		int z=thePacket.getzCoordinate();
		
		//1.7.10 World.blockExists(int,int,int)
		boolean isBlockExist = y >= 0 && y < 256 ? worldClient.chunkExists(x >> 4, z >> 4) : false;
		//end
		
		if (isBlockExist) {
			TileEntity tileentity = worldClient.getTileEntity(new BlockPos(x,y,z));

			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileEntityLargeSign = (TileEntityLargeSign) tileentity;
				
				if (tileEntityLargeSign.isEditable()) {

					tileEntityLargeSign.readFromNBT(thePacket.getMainNBTTC());
					tileEntityLargeSign.markDirty();					
				}

				flag = true;
			}
		}

		if (!flag && this.gameController.thePlayer != null) {
			this.gameController.thePlayer.addChatMessage(new ChatComponentText(
					"Unable to locate LargeSign at "+ x + ", "+ y + ", "+ z));
		}
	}
	
}
