package com.roripantsu.largesign.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import com.roripantsu.common.ModI18n;
import com.roripantsu.guilib.CustomGuiSlider;
import com.roripantsu.guilib.GuiMainScreen;
import com.roripantsu.guilib.GuiSubScreen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of  Large sign text positon and scale adjustor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiPositonAndScaleAdjustor extends GuiSubScreen {
	
	private CustomGuiSlider xPosSlider;
	private CustomGuiSlider yPosSlider;
	private CustomGuiSlider scaleSlider;
	private GuiButton resetBtn;
	float[] adjust= { 0, 0, 0 };
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String title=ModI18n.gui(guiName, "title", new Object[0]);
	private String xPos=ModI18n.gui(guiName, "xPos", new Object[0]);
	private String yPos=ModI18n.gui(guiName, "yPos", new Object[0]);
	private String scale=ModI18n.gui(guiName, "scale", new Object[0]);
	private String reset=ModI18n.gui(guiName, "reset", new Object[0]);
	//<--Localize Gui
	
	public CustomGuiPositonAndScaleAdjustor(int ID,Minecraft MC,FontRenderer font,GuiMainScreen parent,
			int x, int y,int width,int height) {
		super(ID, MC, font, parent, x, y, width, height);
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		if (this.isVisible) {
			this.drawCenteredString(this.fontRendererObj, title,this.GroupX+this.width/2,this.GroupY, 16777215);
		}
	}
	 
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long  timeSinceMouseClick) {
		if(this.xPosSlider.isPressed)
			this.adjust[1]=this.xPosSlider.getValue();
		if(this.yPosSlider.isPressed)
			this.adjust[2]=this.yPosSlider.getValue();
		if(this.scaleSlider.isPressed)
			this.adjust[0]=this.scaleSlider.getValue();
	}
	
	/**
	 *Define action performed of the button.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == this.resetBtn.id) {
			this.xPosSlider.setValue(this.adjust[0] = 0F);
			this.yPosSlider.setValue(this.adjust[1] = 0F);
			this.scaleSlider.setValue(this.adjust[2] = 0F);	
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {	
		this.buttonList.clear();
		this.buttonList.add(this.xPosSlider = 
				new CustomGuiSlider(id, GroupX, GroupY+fixH(this.gridHeight*4), fixW(this.gridWidth*20), xPos, 1.0F, -32F, 32F, 0));
		this.buttonList.add(this.yPosSlider = 
				new CustomGuiSlider(id+1, GroupX, GroupY+fixH(this.gridHeight*4+20), fixW(this.gridWidth*20), yPos, 1.0F, -32F, 32F, 0));
		this.buttonList.add(this.scaleSlider = 
				new CustomGuiSlider(id+2, GroupX, GroupY+fixH(this.gridHeight*4+40), fixW(this.gridWidth*20), scale, 2.0F, -64F, 64F, 0));
		this.buttonList.add(this.resetBtn = 
				new GuiButton(id+3, GroupX, GroupY+fixH(this.gridHeight*4+60), fixW(this.gridWidth*20), 20, reset));
		this.parentButtonList.addAll(this.buttonList);

	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.adjust = new float[] { 0, 0, 0 };
		for (int i = 0; i < this.buttonList.size(); i++) {
			((GuiButton)this.buttonList.get(i)).visible = isVisible;
			((GuiButton)this.buttonList.get(i)).enabled = isVisible;
		}
	}
}

