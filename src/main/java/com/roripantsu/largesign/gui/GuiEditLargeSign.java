package com.roripantsu.largesign.gui;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.roripantsu.common.ModI18n;
import com.roripantsu.guilib.GuiMainScreen;
import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.packet.CPacketUpdateLargeSign;
import com.roripantsu.largesign.packet.SPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSignRenderer;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Main Gui Screen of Large Sign Editor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class GuiEditLargeSign extends GuiMainScreen {

	Minecraft MC = Minecraft.getMinecraft();
	private GuiButton doneBtn;
	private GuiButton textModeBtn;
	private GuiButton itemModeBtn;
	private CustomGuiTextAndFontStyleEditor fontStyleChooser;
	private CustomGuiPositonAndScaleAdjustor Adjuter;
	private CustomGuiColorSwitcher colorChooser;
	private CustomGuiIconList iconList;
	private TileEntityLargeSign tileLargeSign;
	private int modeNumber;
	GuiButton selectedButton;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String textMode=ModI18n.gui(guiName, "textMode", new Object[0]);
	private String itemIconMode=ModI18n.gui(guiName, "itemIconMode", new Object[0]);
	private String done=ModI18n.gui(guiName, "done", new Object[0]);
	//<--Localize Gui
	
	public GuiEditLargeSign(TileEntityLargeSign tileEntity) {
		this.tileLargeSign = tileEntity;
		((TileEntityLargeSignRenderer)TileEntityRendererDispatcher.instance
		.getSpecialRenderer(this.tileLargeSign)).showWarning=false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		this.drawDefaultBackground();
		this.drawLargeSign();
		this.drawHorizontalLine(0, this.width, this.height / 32 * 4+4, new Color(
				255, 255, 255, 128).getRGB());
		
		this.fontStyleChooser.drawScreen(mouseX, mouseY, par3);
		this.Adjuter.drawScreen(mouseX, mouseY, par3);
		this.colorChooser.drawScreen(mouseX, mouseY, par3);
		this.iconList.drawScreen(mouseX, mouseY, par3);
		if(this.modeNumber==1 && this.iconList.selectedItemStack != null)
			this.drawCenteredString(fontRendererObj,
					this.iconList.selectedItemStack.getDisplayName(), 
					this.width/2, this.gridHeight*50, 16777215);
		super.drawScreen(mouseX, mouseY, par3);//draw all GuiButton and GuiLabel
		this.iconList.renderButtonHoveringText(mouseX, mouseY);
	}

	@Override
	protected void keyTyped(char character, int code) {
		this.colorChooser.keyTyped(character, code);
		this.fontStyleChooser.keyTyped(character, code);

		if (code == 1) {
			this.actionPerformed(this.doneBtn);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int buttonClicked) {
		this.fontStyleChooser.mouseClicked(x, y, buttonClicked);
		this.colorChooser.mouseClicked(x, y, buttonClicked);
		super.mouseClicked(x, y, buttonClicked);//for all button.
	}
	  
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long  timeSinceMouseClick) {
		this.Adjuter.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
	}
	
	/**
	 *Define action performed of the button.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			this.fontStyleChooser.actionPerformed(button);
			this.Adjuter.actionPerformed(button);
			this.colorChooser.actionPerformed(button);
			this.iconList.actionPerformed(button);

			if (button.id == 0) {
				this.tileLargeSign.markDirty();
				this.mc.displayGuiScreen((GuiScreen) null);
			}

			if (button.id == 1) {
				this.modeNumber = 0;
				modeChange(this.modeNumber);
			}

			if (button.id == 2) {
				this.modeNumber = 1;
				modeChange(this.modeNumber);
			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {

		this.allowUserInput = true;
		Keyboard.enableRepeatEvents(true);
		if (!this.MC.gameSettings.forceUnicodeFont) {
			this.fontRendererObj.setUnicodeFlag(true);
			this.fontRendererObj.setBidiFlag(true);
		}
		
		this.buttonList.clear();
		this.buttonList.add(this.doneBtn = 
				new GuiButton(0,fixW(this.gridWidth*29),fixH(this.gridHeight*71), fixW(this.gridWidth*12),20, done));
		this.buttonList.add(this.textModeBtn = 
				new GuiButton(1,fixW(this.gridWidth), fixH(this.gridHeight*2), fixW(this.gridWidth*12),20, textMode));
		this.buttonList.add(this.itemModeBtn = 
				new GuiButton(2,fixW(this.gridWidth*14), fixH(this.gridHeight*2), fixW(this.gridWidth*12),20, itemIconMode));
		
		this.fontStyleChooser = 
				new CustomGuiTextAndFontStyleEditor(3,this.mc,this.fontRendererObj,this,
						fixW(this.gridWidth*4),fixH(this.gridHeight*50),fixW(this.gridWidth*63),fixH(this.gridHeight*19));
		this.fontStyleChooser.initGui();
		this.Adjuter = 
				new CustomGuiPositonAndScaleAdjustor(this.fontStyleChooser.getLastID()+1,this.mc,this.fontRendererObj,this,
						fixW(this.gridWidth*5),fixH(this.gridHeight*15),fixW(this.gridWidth*20),fixH(this.gridHeight*30));
		this.Adjuter.initGui();
		this.colorChooser = 
				new CustomGuiColorSwitcher(this.Adjuter.getLastID()+1,this.mc,this.fontRendererObj,this,
						fixW(this.gridWidth*45),fixH(this.gridHeight*15),fixW(this.gridWidth*22),fixH(this.gridHeight*30));
		this.colorChooser.initGui();
		this.iconList = 
				new CustomGuiIconList(this.colorChooser.getLastID()+1,this.mc,this.fontRendererObj,this,
						fixW(this.gridWidth*45),fixH(this.gridHeight*15),fixW(this.gridWidth*25),fixH(this.gridHeight*59));
		this.iconList.initGui();
		
		this.tileLargeSign.setEditable(false);
		modeChange(this.modeNumber);
	}

	@Override
	public void updateScreen() {
		this.fontStyleChooser.updateScreen();
		this.colorChooser.updateScreen();
		this.tileLargeSign.largeSignText[0] = this.fontStyleChooser
				.formatStringChange(this.fontStyleChooser.editTextField
						.getText());
		this.tileLargeSign.largeSignTextColor = this.colorChooser.ColorObj
				.getRGB();
		this.tileLargeSign.modeNumber = this.modeNumber;
		this.tileLargeSign.itemID = this.iconList.selectedItemID;
		this.tileLargeSign.itemMetadata = this.iconList.selectedItemMatadata;
		this.tileLargeSign.hasShadow = CustomGuiTextAndFontStyleEditor.FontStyles.SHADOW.enable;
		this.tileLargeSign.scaleAdjust = this.Adjuter.adjust[0];
		this.tileLargeSign.XAdjust = this.Adjuter.adjust[1];
		this.tileLargeSign.YAdjust = this.Adjuter.adjust[2];
		if(this.iconList.selectedItemStack!=null)
			this.tileLargeSign.setItemStack(this.iconList.selectedItemStack);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if (!this.MC.gameSettings.forceUnicodeFont) {
			this.fontRendererObj.setUnicodeFlag(false);
			this.fontRendererObj.setBidiFlag(false);
		}

		CPacketUpdateLargeSign thePacketC = 
				new CPacketUpdateLargeSign(this.tileLargeSign);
		SPacketUpdateLargeSign thePacketS = 
				new SPacketUpdateLargeSign(this.tileLargeSign);

		try {
			List<Object> list = new LinkedList<Object>();
			Mod_LargeSign.proxy.packetPipeline.encode(thePacketC, list);
			Mod_LargeSign.proxy.packetPipeline.encode(thePacketS, list);
			FMLProxyPacket pktC = (FMLProxyPacket) list.get(0);
			FMLProxyPacket pktS = (FMLProxyPacket) list.get(1);
			Mod_LargeSign.proxy.packetPipeline.sendToServer(pktC);
			Mod_LargeSign.proxy.packetPipeline.sendToAll(pktS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.tileLargeSign.setEditable(true);
		((TileEntityLargeSignRenderer)TileEntityRendererDispatcher.instance
		.getSpecialRenderer(this.tileLargeSign)).showWarning=true;
	}
	
	private void modeChange(int i) {

		switch (i) {
		case 0:
			this.Adjuter.setVisible(true);
			this.colorChooser.setVisible(true);
			this.fontStyleChooser.setVisible(true);
			this.iconList.setVisible(false);
			this.textModeBtn.enabled=false;
			this.itemModeBtn.enabled=true;

			break;
		case 1:
			this.Adjuter.setVisible(false);
			this.colorChooser.setVisible(false);
			this.fontStyleChooser.setVisible(false);
			this.iconList.setVisible(true);
			this.textModeBtn.enabled=true;
			this.itemModeBtn.enabled=false;

		}

	}
	
	private void drawLargeSign() {

		float SizePercent = 85F;
		float angle = 0.0F;
		int k = this.mc.theWorld.getBlockMetadata(this.tileLargeSign.xCoord,
				this.tileLargeSign.yCoord, this.tileLargeSign.zCoord);
		
		
		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslatef(this.width / 2F, fixH(fixH(fixH(this.gridHeight*4))), 50.0F);
		GL11.glScalef(-SizePercent, -SizePercent, -SizePercent);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

		if (k == 2)
			angle = 180.0F;

		if (k == 4)
			angle = 90.0F;

		if (k == 5)
			angle = -90.0F;

		GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, -1.05F, 0.0F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(
				this.tileLargeSign, -0.5D, -0.5D, -0.5D, 0.0F);
		GL11.glPopMatrix();

	}
}
