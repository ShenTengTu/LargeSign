package com.roripantsu.largesign.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.gui.GuiEditLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *handle opening Large Sign editor and updating Large Sign at client side
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class NetHandlerPlayClientSide extends NetHandlerPlayClient {

	private WorldClient clientWorldController;
	private Minecraft gameController;
	private GuiScreen guiScreenServer;
	private PacketPipeline pipeline = Mod_LargeSign.proxy.packetPipeline;

	public NetHandlerPlayClientSide(Minecraft gameController,
			GuiScreen guiScreenServer, NetworkManager networkManager) {
		super(gameController, guiScreenServer, networkManager);
		this.gameController = gameController;
		this.guiScreenServer = guiScreenServer;
		this.clientWorldController = this.gameController.theWorld;
	}

	public GuiScreen getGuiScreenServer() {
		return guiScreenServer;
	}

	public PacketPipeline getPipeline() {
		return pipeline;
	}

	public void handleLargeSignEditorOpen(SPacketLargeSignEditorOpen thePacket) {

		Object object = this.gameController.theWorld.getTileEntity(
				thePacket.getXCoordinate(), thePacket.getYCoordinate(),
				thePacket.getZCoordinate());

		if (object == null) {
			object = new TileEntityLargeSign();
			((TileEntity) object).setWorldObj(this.clientWorldController);
			((TileEntity) object).xCoord = thePacket.getXCoordinate();
			((TileEntity) object).yCoord = thePacket.getYCoordinate();
			((TileEntity) object).zCoord = thePacket.getZCoordinate();
		}

		this.gameController.displayGuiScreen(new GuiEditLargeSign(
				(TileEntityLargeSign) object));

	}

	public void handleUpdateLargeSign(SPacketUpdateLargeSign thePacket) {
		boolean flag = false;
		if (this.gameController.theWorld.blockExists(
				thePacket.getxCoordinate(), thePacket.getyCoordinate(),
				thePacket.getzCoordinate())) {
			TileEntity tileentity = this.gameController.theWorld.getTileEntity(
					thePacket.getxCoordinate(), thePacket.getyCoordinate(),
					thePacket.getzCoordinate());

			if (tileentity instanceof TileEntityLargeSign) {
				TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) tileentity;

				if (tileentitylargesign.isEditable()) {

					tileentitylargesign.readFromNBT(thePacket.getNBTTC());

					tileentitylargesign.markDirty();
				}

				flag = true;
			}
		}

		if (!flag && this.gameController.thePlayer != null) {
			this.gameController.thePlayer.addChatMessage(new ChatComponentText(
					"Unable to locate LargeSign at "
							+ thePacket.getxCoordinate() + ", "
							+ thePacket.getxCoordinate() + ", "
							+ thePacket.getzCoordinate()));
		}
	}

}
