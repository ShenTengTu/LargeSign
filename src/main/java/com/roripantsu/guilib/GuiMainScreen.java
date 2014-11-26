package com.roripantsu.guilib;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Let screen to lay out its subcomponents depending on grids,
 *and let components to scale when resize window.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class GuiMainScreen extends GuiScreen {
	
	/**
	 * Width of a grid.
	 */
	protected int gridWidth;
	/**
	 *  Height of a grid.
	 */
	protected int gridHeight;
	/**
	 * the factor affect Width-related scale.
	 */
	float factorW;
	/**
	 * the factor affect Height-related scale.
	 */
	float factorH;
	
	
	@Override
	public void setWorldAndResolution(Minecraft MC, int width,
			int height) {

		if(MC.displayWidth>=854F)
			this.factorW=MC.displayWidth/854F;
		else
			this.factorW=854F/MC.displayWidth;
		
		if(MC.displayHeight>=480F)
			this.factorH=MC.displayHeight/480F;
		else
			this.factorH=480F/MC.displayHeight;
		
		this.gridWidth=(int) (width/(64F*this.factorW));
		if (this.gridWidth<2)
			this.gridWidth=2;
		this.gridHeight=(int) (height/(64F*this.factorH));
		if (this.gridHeight<1)
			this.gridWidth=1;
		
		super.setWorldAndResolution(MC, width, height);
		
	}
	
	/**
	 * 
	 * @return GuiButtton list of this screen.
	 */
	@SuppressWarnings("rawtypes")
	protected List getButtonList(){
		return this.buttonList;
	}
	
	/**
	 * Show grids.
	 */
	protected void showGrid(){
		int c=0;
		for(int x=gridWidth;x<this.width;x+=gridWidth){
			c++;
			this.drawVerticalLine(x, 0, this.height, new Color(63,255,63,c%5==0?127:63).getRGB());
		}
		c=0;
		for(int y=gridHeight;y<this.height;y+=gridHeight){
			c++;
			this.drawHorizontalLine(0, this.width, y, new Color(63,255,63,c%5==0?127:63).getRGB());
		}
	}
	
	/**
	 * Fix Width-related value for auto scale. 
	 */
	protected int fixW(int value){
		return (int)(value*this.factorW);
	}
	
	/**
	 * Fix Height-related value for auto scale. 
	 */
	protected int fixH(int value){
		return (int)(value*this.factorH);
	}
}
