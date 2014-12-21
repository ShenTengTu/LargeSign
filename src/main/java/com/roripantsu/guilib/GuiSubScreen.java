package com.roripantsu.guilib;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *This class let components become a group for individual features.
 *It can put components in a main screen together.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class GuiSubScreen extends GuiMainScreen {
	
	/**
	 * The id of first GuiButton in this sub screen
	 */
	protected int id;
	/**
	 * parent screen which contain this sub screen.
	 */
	GuiMainScreen parent;
	/**
	 * GuiButton list of parent screen.
	 */
	@SuppressWarnings("rawtypes")
	protected
	List parentButtonList;
	/**
	 * the x coordinate (left) of this sub screen.
	 */
	protected int GroupX;
	/**
	 * the y coordinate (top) of this sub screen.
	 */
	protected int GroupY;
	/**
	 * Visibility of this sub screen.
	 */
	protected boolean isVisible=true;
	
	public GuiSubScreen(int ID,Minecraft MC,FontRenderer font,GuiMainScreen parent,
			int x, int y,int width,int height) {
		this.id=ID;
		this.mc=MC;
		this.fontRendererObj=font;
		this.parent=parent;
		this.parentButtonList=parent.getButtonList();
		this.gridWidth=parent.gridWidth;
		this.gridHeight=parent.gridHeight;
		this.factorW=parent.factorW;
		this.factorH=parent.factorH;
		this.GroupX=x;
		this.GroupY=y;
		this.width=width;
		this.height=height;
	}
	
	/**
	 * @return the id of last GuiButton in this sub screen
	 */
	public int getLastID(){
		int i=this.buttonList.size()-1;
		return ((GuiButton)this.buttonList.get(i)).id;
	}
	
	/**
	 * Set visibility of this sub screen.
	 */
	public void setVisible(boolean isVisible){
		this.isVisible=isVisible;
	}
	
	/**
	 * Show the range of this sub screen.
	 */
	protected void showRange(){
		drawRect(this.GroupX, this.GroupY, 
				this.GroupX+this.width, this.GroupY+this.height, 
				new Color(63,63,255,128).getRGB());
	}
}

