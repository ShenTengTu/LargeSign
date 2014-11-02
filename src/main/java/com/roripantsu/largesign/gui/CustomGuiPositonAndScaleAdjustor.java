package com.roripantsu.largesign.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of  Large sign text positon and scale adjustor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiPositonAndScaleAdjustor extends GuiScreen {

	private static final int[] ButtonSize = { 18, 18 };
	protected float[] adjust = { 0, 0, 0 };
	protected final int lastButtonID;
	protected GuiButton resetBtn;
	protected GuiButton sacleNBtn;
	protected GuiButton saclePBtn;
	protected GuiButton XnBtn;
	protected GuiButton XpBtn;
	protected GuiButton YnBtn;
	protected GuiButton YpBtn;
	private List<GuiButton> buttonList = new ArrayList<GuiButton>();
	private int ID;
	private boolean isVisible;
	private int xPosition;
	private int yPosition;

	public CustomGuiPositonAndScaleAdjustor(Minecraft MC,
			FontRenderer FontRenderer, List<GuiButton> screenButtonList,
			int ID, int X, int Y) {
		
		this.fontRendererObj=FontRenderer;
		this.ID=ID;
		this.lastButtonID=ID + 6;
		this.xPosition=X;
		this.yPosition=Y;
		
		this.buttonList.add(this.XpBtn = new GuiButton(ID, X
				+ (ButtonSize[0] + 1) * 4+1, Y, ButtonSize[0], ButtonSize[1]+2,
				"\u2192"));
		this.buttonList.add(this.XnBtn = new GuiButton(ID + 1, X
				+ (ButtonSize[0] + 1), Y, ButtonSize[0], ButtonSize[1]+2,
				"\u2190"));
		this.buttonList.add(this.YpBtn = new GuiButton(ID + 2, X
				+ (ButtonSize[0] + 1) * 4+1, Y + ButtonSize[1] + 2,
				ButtonSize[0], ButtonSize[1]+2, "\u2193"));
		this.buttonList.add(this.YnBtn = new GuiButton(ID + 3, X
				+ (ButtonSize[0] + 1), Y + ButtonSize[1] + 2, ButtonSize[0],
				ButtonSize[1]+2, "\u2191"));
		this.buttonList.add(this.saclePBtn = new GuiButton(ID + 4, X
				+ (ButtonSize[0] + 1) * 4+1, Y + (ButtonSize[1] + 2) * 2,
				ButtonSize[0], ButtonSize[1]+2, "+1.0"));
		this.buttonList.add(this.sacleNBtn = new GuiButton(ID + 5, X
				+ (ButtonSize[0] + 1), Y + (ButtonSize[1] + 2) * 2,
				ButtonSize[0], ButtonSize[1]+2, "-1.0"));
		this.buttonList.add(this.resetBtn = new GuiButton(ID + 6, X
				+ (ButtonSize[0] + 1), Y + (ButtonSize[1] + 2) * 3,
				ButtonSize[0] * 4 + 4, ButtonSize[1]+2, "reset"));
		screenButtonList.addAll(this.buttonList);
	}

	@Override
	public void drawScreen(int mouseX,int mouseY,float par3) {
		if (this.isVisible) {
			this.drawCenteredString(this.fontRendererObj, "Adjustor:",
					this.xPosition + (ButtonSize[0] + 2) * 3,
					this.yPosition - 14, 16777215);
			this.drawCenteredString(this.fontRendererObj, "X pos.",
					this.xPosition + (ButtonSize[0] + 2) * 3,
					this.yPosition + 6, 16777215);
			this.drawCenteredString(this.fontRendererObj, "Y pos.",
					this.xPosition + (ButtonSize[0] + 2) * 3, this.yPosition
							+ ButtonSize[1] + 6, 16777215);
			this.drawCenteredString(this.fontRendererObj, "Scale", this.xPosition
					+ (ButtonSize[0] + 1) * 3, this.yPosition
					+ (ButtonSize[1] + 4) * 2, 16777215);
		}
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.adjust = new float[] { 0, 0, 0 };
		for (int i = 0; i < this.buttonList.size(); i++) {
			this.buttonList.get(i).visible = isVisible;
			this.buttonList.get(i).enabled = isVisible;
		}
	}

	/*@Override
	public void updateScreen() {
	}*/

	@Override
	protected void actionPerformed(GuiButton btn) {

		if (btn.id == this.ID)
			this.adjust[1] += 0.2F;
		if (btn.id == this.ID + 1)
			this.adjust[1] -= 0.2F;
		if (btn.id == this.ID + 2)
			this.adjust[2] += 0.2F;
		if (btn.id == this.ID + 3)
			this.adjust[2] -= 0.2F;
		if (btn.id == this.ID + 4)
			this.adjust[0] += 1F;
		if (btn.id == this.ID + 5)
			this.adjust[0] -= 1F;
		if (btn.id == this.ID + 6) {
			this.adjust[0] = 0F;
			this.adjust[1] = 0F;
			this.adjust[2] = 0F;
		}

	}

	/*@Override
	protected void keyTyped(char par1, int par2) {
	}*/

	/*@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) {

	}*/

}
