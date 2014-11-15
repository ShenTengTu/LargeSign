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

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.network.CPacketUpdateLargeSign;
import com.roripantsu.largesign.network.SPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Main Gui Screen of Large Sign Editor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class GuiEditLargeSign extends GuiScreen {
	private static final int[] ButtonSize = { 70, 20 };
	protected Minecraft MC = Minecraft.getMinecraft();
	GuiButton selectedButton;
	private CustomGuiPositonAndScaleAdjustor Adjuter;
	private CustomGuiColorSwitcher colorChooser;
	private GuiButton doneBtn;
	private CustomGuiTextAndFontStyleEditor fontStyleChooser;
	private CustomGuiIconList iconList;
	private GuiButton itemModeBtn;
	private int modeNumber = 0;
	private GuiButton textModeBtn;
	private TileEntityLargeSign tileLargeSign;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String textMode=LocalizeGui.guiLocalString(guiName, "textMode", new Object[0]);
	private String itemIconMode=LocalizeGui.guiLocalString(guiName, "itemIconMode", new Object[0]);
	private String done=LocalizeGui.guiLocalString(guiName, "done", new Object[0]);
	//<--Localize Gui

	public GuiEditLargeSign(TileEntityLargeSign tileEntity) {
		this.tileLargeSign = tileEntity;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		this.drawDefaultBackground();
		this.drawHorizontalLine(0, this.width, this.height / 32 * 4+4, new Color(
				255, 255, 255, 128).getRGB());
		this.drawLargeSign();
		this.fontStyleChooser.drawScreen(mouseX, mouseY, par3);
		this.Adjuter.drawScreen(mouseX, mouseY, par3);
		this.colorChooser.drawScreen(mouseX, mouseY, par3);
		this.iconList.drawScreen(mouseX, mouseY, par3);
		super.drawScreen(mouseX, mouseY, par3);//draw all GuiButton and GuiLabel
		this.iconList.renderButtonHoveringText(mouseX, mouseY);

	}

	public GuiButton getItemModeBtn() {
		return itemModeBtn;
	}

	public GuiButton getTextModeBtn() {
		return textModeBtn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {

		this.buttonList.clear();
		this.allowUserInput = true;
		Keyboard.enableRepeatEvents(true);
		if (!this.MC.gameSettings.forceUnicodeFont) {
			this.fontRendererObj.setUnicodeFlag(true);
			this.fontRendererObj.setBidiFlag(true);
		}

		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2
				- ButtonSize[0] / 2, this.height / 32 * 30, ButtonSize[0],
				ButtonSize[1], done));
		this.buttonList.add(this.setTextModeBtn(new GuiButton(1,
				this.width / 32, this.height / 32, ButtonSize[0],
				ButtonSize[1], textMode)));
		this.buttonList.add(this.setItemModeBtn(new GuiButton(2, this.width
				/ 32 + ButtonSize[0] + 2, this.height / 32, ButtonSize[0],
				ButtonSize[1], itemIconMode)));
		this.fontStyleChooser = new CustomGuiTextAndFontStyleEditor(this.mc,
				this.fontRendererObj, this.buttonList, 3, this.width * 1 / 8,
				this.height / 32 * 8);
		this.Adjuter = new CustomGuiPositonAndScaleAdjustor(this.mc,
				this.fontRendererObj, this.buttonList,
				this.fontStyleChooser.lastButtonID + 1,
				this.width / 32 * 6 - 8, this.height / 32 * 8 + 2);
		this.colorChooser = new CustomGuiColorSwitcher(this.mc,
				this.fontRendererObj, this.buttonList,
				this.Adjuter.lastButtonID + 1, this.width * 3 / 4 - 28,
				this.height / 32 * 8);
		this.iconList = new CustomGuiIconList(this.mc, this.fontRendererObj,
				this.buttonList, this.colorChooser.lastButtonID + 1,
				this.width / 32*20+8, this.height / 32 * 6);

		this.tileLargeSign.setEditable(false);
		modeChange(this.modeNumber);
	}

	@Override
	public void onGuiClosed() {

		Keyboard.enableRepeatEvents(false);
		if (!this.MC.gameSettings.forceUnicodeFont) {
			this.fontRendererObj.setUnicodeFlag(false);
			this.fontRendererObj.setBidiFlag(false);
		}

		this.tileLargeSign.writeToNBT(this.tileLargeSign.getNBTTC());
		CPacketUpdateLargeSign thePacketC = new CPacketUpdateLargeSign(
				this.tileLargeSign.getNBTTC());
		SPacketUpdateLargeSign thePacketS = new SPacketUpdateLargeSign(
				this.tileLargeSign.getNBTTC());

		try {
			List<Object> list = new LinkedList<Object>();
			Mod_LargeSign.proxy.packetPipeline.encode(thePacketC,
					Mod_LargeSign.proxy.channelKey, list);
			Mod_LargeSign.proxy.packetPipeline.encode(thePacketS,
					Mod_LargeSign.proxy.channelKey, list);
			FMLProxyPacket pktC = (FMLProxyPacket) list.get(0);
			FMLProxyPacket pktS = (FMLProxyPacket) list.get(1);
			Mod_LargeSign.proxy.packetPipeline.sendToServer(pktC);
			Mod_LargeSign.proxy.packetPipeline.sendToAll(pktS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.tileLargeSign.setEditable(true);
	}

	public GuiButton setItemModeBtn(GuiButton itemModeBtn) {
		this.itemModeBtn = itemModeBtn;
		return itemModeBtn;
	}

	public GuiButton setTextModeBtn(GuiButton textModeBtn) {
		this.textModeBtn = textModeBtn;
		return textModeBtn;
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
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		if (btn.enabled) {
			this.fontStyleChooser.actionPerformed(btn);
			this.Adjuter.actionPerformed(btn);
			this.colorChooser.actionPerformed(btn);
			this.iconList.actionPerformed(btn);

			if (btn.id == 0) {
				this.tileLargeSign.markDirty();
				this.mc.displayGuiScreen((GuiScreen) null);
			}

			if (btn.id == 1) {
				this.modeNumber = 0;
				modeChange(this.modeNumber);
			}

			if (btn.id == 2) {
				this.modeNumber = 1;
				modeChange(this.modeNumber);
			}

		}
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		this.colorChooser.keyTyped(par1, par2);
		this.fontStyleChooser.keyTyped(par1, par2);

		if (par2 == 1) {
			this.actionPerformed(this.doneBtn);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) {
		this.fontStyleChooser.mouseClicked(mouseX, mouseY, par3);
		this.colorChooser.mouseClicked(mouseX, mouseY, par3);

		if (par3 == 0) {
			for (int l = 0; l < this.buttonList.size(); ++l) {
				GuiButton guibutton = (GuiButton) this.buttonList.get(l);

				if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
					this.selectedButton = guibutton;
					guibutton.func_146113_a(this.mc.getSoundHandler());
					this.actionPerformed(guibutton);
				}
			}
		}
	}

	private void drawLargeSign() {

		float SizePercent = 85F;
		float angle = 0.0F;
		int k = this.mc.theWorld.getBlockMetadata(this.tileLargeSign.xCoord,
				this.tileLargeSign.yCoord, this.tileLargeSign.zCoord);

		GL11.glPushMatrix();
		GL11.glTranslatef(this.width / 2F, this.height / 32F, 50.0F);
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
	
}
