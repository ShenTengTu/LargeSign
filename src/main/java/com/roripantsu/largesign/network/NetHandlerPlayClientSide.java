package com.roripantsu.largesign.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mojang.authlib.GameProfile;
import com.roripantsu.largesign.gui.GuiEditLargeSign;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.packet.SPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *handle opening Large Sign editor and updating Large Sign at client side
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class NetHandlerPlayClientSide extends NetHandlerPlayClient {
	private static final Minecraft mc= Minecraft.getMinecraft();
	private static final GuiScreen guiScreen = mc.currentScreen;
	private static final NetworkManager networkManager = mc.getNetHandler().getNetworkManager();
	private static final GameProfile gameProfile = mc.getSession().getProfile();
	private static final WorldClient worldClient =mc.theWorld;
	
	public NetHandlerPlayClientSide() {
		super(mc, guiScreen, networkManager,gameProfile);
	}

	public void handleLargeSignEditorOpen(SPacketLargeSignEditorOpen thePacket) {
		
		Object object = NetHandlerPlayClientSide.mc.theWorld.getTileEntity(thePacket.getBlockPos());
		
		if (!(object instanceof TileEntityLargeSign)) {
			object = new TileEntityLargeSign();
			((TileEntity)object).setWorldObj(NetHandlerPlayClientSide.worldClient);
			((TileEntity)object).setPos(thePacket.getBlockPos());		
		}

		mc.displayGuiScreen(new GuiEditLargeSign((TileEntityLargeSign) object));

	}

	public void handleUpdateLargeSign(SPacketUpdateLargeSign thePacket) {
		WorldClient worldClient=mc.theWorld;
		boolean flag = false;
		int x=thePacket.getBlockPos().getX();
		int y=thePacket.getBlockPos().getY();
		int z=thePacket.getBlockPos().getZ();
		
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

		if (!flag && mc.thePlayer != null) {
			mc.thePlayer.addChatMessage(new ChatComponentText(
					"Unable to locate LargeSign at "+ x + ", "+ y + ", "+ z));
		}
	}
	
}
