package com.roripantsu.largesign.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import com.roripantsu.common.ModI18n;
import com.roripantsu.guilib.CustomGuiButton;
import com.roripantsu.guilib.GuiMainScreen;
import com.roripantsu.guilib.GuiSubScreen;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of Color switcher
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiColorSwitcher extends GuiSubScreen {
	
	private GuiButton redDecBtn;
	private GuiButton redIncBtn;
	private GuiButton greenDecBtn;
	private GuiButton greenIncBtn;
	private GuiButton blueDecBtn;
	private GuiButton blueIncBtn;
	private GuiButton alphaDecBtn;
	private GuiButton alphaIncBtn;
	private CustomGuiButton[] ColorBolock;
	private final String[] Color16Code = { "#000000", "#000080", "#0000FF",
			"#008000", "#008080", "#00FF00", "#00FFFF", "#808080", "#800000",
			"#800080", "#808000", "#C0C0C0", "#FF0000", "#FF00FF", "#FFFF00",
			"#FFFFFF" };
	private int[] RGBA = { 225, 225, 225, 255 };
	Color ColorObj;
	private List<GuiTextField> textFieldList;
	private GuiTextField redTextField;
	private GuiTextField greenTextField;
	private GuiTextField blueTextField;
	private GuiTextField alphaTextField;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String title=ModI18n.gui(guiName, "title", new Object[0]);
	//<--Localize Gui

	public CustomGuiColorSwitcher(int ID,Minecraft MC,FontRenderer font,GuiMainScreen parent,
			int x, int y,int width,int height) {
		super(ID, MC, font, parent, x, y, width, height);
		this.ColorBolock=new CustomGuiButton[16];
		this.textFieldList=new ArrayList<GuiTextField>();
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		
		if (this.isVisible) {
			this.drawCenteredString(this.fontRendererObj, title,
					this.GroupX+this.width/2, this.GroupY,16777215);
			this.redTextField.drawTextBox();
			this.blueTextField.drawTextBox();
			this.greenTextField.drawTextBox();
			this.alphaTextField.drawTextBox();
		}
	}

	@Override
	protected void keyTyped(char character, int code) {

		if (!Character.isISOControl(character) && !Character.isDigit(character)) {
			character = '0';
		}
		
		this.redTextField.textboxKeyTyped(character, code);
		this.greenTextField.textboxKeyTyped(character, code);
		this.blueTextField.textboxKeyTyped(character, code);
		this.alphaTextField.textboxKeyTyped(character, code);

		if (Character.isDigit(character)) {
			String[] strArr = { this.redTextField.getText(),
					this.greenTextField.getText(),
					this.blueTextField.getText(), this.alphaTextField.getText() };
			for (int i = 0; i < 4; i++) {
				if (!(strArr[i] == null && strArr[i] == "")) {
					if (Integer.parseInt(strArr[i]) > 255) {
						strArr[i] = "255";
						this.RGBA[i] = 255;
					} else {
						strArr[i] = String.valueOf(Integer.parseInt(strArr[i]));
						this.RGBA[i] = Integer.parseInt(strArr[i]);
					}
				}
			}

			this.redTextField.setText(strArr[0]);
			this.greenTextField.setText(strArr[1]);
			this.blueTextField.setText(strArr[2]);
			this.alphaTextField.setText(strArr[3]);

			this.ColorObj = new Color(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);

		}
	}

	@Override
	protected void mouseClicked(int x, int y, int buttonClicked) {

		if (buttonClicked == 0) {
			this.redTextField.mouseClicked(x, y, buttonClicked);
			this.greenTextField.mouseClicked(x, y, buttonClicked);
			this.blueTextField.mouseClicked(x, y, buttonClicked);
			this.alphaTextField.mouseClicked(x, y, buttonClicked);
		}
	}

	/**
	 *Define action performed of the button.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {

		if (button.enabled) {
			if (button.id == this.redDecBtn.id)
				this.RGBA[0] -= 8;
			if (button.id == this.redIncBtn.id)
				this.RGBA[0] += 8;
			if (button.id == this.greenDecBtn.id)
				this.RGBA[1] -= 8;
			if (button.id == this.greenIncBtn.id)
				this.RGBA[1] += 8;
			if (button.id == this.blueDecBtn.id)
				this.RGBA[2] -= 8;
			if (button.id == this.blueIncBtn.id)
				this.RGBA[2] += 8;
			if (button.id == this.alphaDecBtn.id)
				this.RGBA[3] -= 8;
			if (button.id == this.alphaIncBtn.id)
				this.RGBA[3] += 8;
			if (button.id >= this.ColorBolock[0].id && button.id <= this.ColorBolock[ColorBolock.length-1].id) {
				this.RGBA[0] = ((CustomGuiButton) button).getColor().getRed();
				this.RGBA[1] = ((CustomGuiButton) button).getColor().getGreen();
				this.RGBA[2] = ((CustomGuiButton) button).getColor().getBlue();
			}

			for (int i = 0; i < 4; i++) {
				if (this.RGBA[i] < 0)
					this.RGBA[i] = 0;
				if (this.RGBA[i] > 255)
					this.RGBA[i] = 255;
			}
			
			this.redTextField.setText(String.valueOf(this.RGBA[0]));
			this.greenTextField.setText(String.valueOf(this.RGBA[1]));
			this.blueTextField.setText(String.valueOf(this.RGBA[2]));
			this.alphaTextField.setText(String.valueOf(this.RGBA[3]));

			this.ColorObj = new Color(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(this.redDecBtn = 
				new GuiButton(id, this.GroupX,this.GroupY+fixH(this.gridHeight*4),fixW(18),20,"R-8"));
		this.buttonList.add(this.redIncBtn = 
				new GuiButton(id+1,this.GroupX+fixW(42),this.GroupY+fixH(this.gridHeight*4),fixW(18),20,"R+8"));
		this.buttonList.add(this.greenDecBtn = 
				new GuiButton(id+2,this.GroupX,this.GroupY+fixH(this.gridHeight*4+20),fixW(18),20,"G-8"));
		this.buttonList.add(this.greenIncBtn = 
				new GuiButton(id+3,this.GroupX+fixW(42),this.GroupY+fixH(this.gridHeight*4+20),fixW(18),20,"G+8"));
		this.buttonList.add(this.blueDecBtn = 
				new GuiButton(id+4,this.GroupX,this.GroupY+fixH(this.gridHeight*4+40),fixW(18),20,"B-8"));
		this.buttonList.add(this.blueIncBtn = 
				new GuiButton(id+5,this.GroupX+fixW(42),this.GroupY+fixH(this.gridHeight*4+40),fixW(18),20,"B+8"));
		this.buttonList.add(this.alphaDecBtn = 
				new GuiButton(id+6,this.GroupX,this.GroupY+fixH(this.gridHeight*4+60),fixW(18),20,"A-8"));
		this.buttonList.add(this.alphaIncBtn = 
				new GuiButton(id+7,this.GroupX+fixW(42),this.GroupY+fixH(this.gridHeight*4+60),fixW(18),20,"A+8"));
		for (int i = 0; i < this.ColorBolock.length; i++) {
			int x = this.GroupX + fixW(this.gridWidth*10) + i % 4 * fixW(18);
			int yshift = (i < 16) ? ((i < 12) ? ((i < 8) ? (i < 4 ? 0: fixW(18)) : fixW(36)): fixW(54)) : 0;
			int y = this.GroupY+fixH(this.gridHeight*4)+ yshift;
			this.ColorBolock[i] = new CustomGuiButton(id + 8 + i, x, y, fixW(18), fixH(18),Color.decode(this.Color16Code[i]));
		}
		this.buttonList.addAll(Arrays.asList(this.ColorBolock));
		this.parentButtonList.addAll(this.buttonList);
		
		this.ColorObj = new Color(this.RGBA[0], this.RGBA[1], this.RGBA[2],
				this.RGBA[3]);
		
		this.textFieldList.add(this.redTextField = 
				new GuiTextField(this.fontRendererObj,this.GroupX+fixW(20),this.GroupY+fixH(this.gridHeight*4+2),fixW(20),16));
		this.textFieldList.add(this.greenTextField = 
				new GuiTextField(this.fontRendererObj,this.GroupX+fixW(20),this.GroupY+fixH(this.gridHeight*4+22),fixW(20),16));
		this.textFieldList.add(this.blueTextField = 
				new GuiTextField(this.fontRendererObj,this.GroupX+fixW(20),this.GroupY+fixH(this.gridHeight*4+42),fixW(20),16));
		this.textFieldList.add(this.alphaTextField = 
				new GuiTextField(this.fontRendererObj,this.GroupX+fixW(20),this.GroupY+fixH(this.gridHeight*4+62),fixW(20),16));
		this.redTextField.setMaxStringLength(3);
		this.greenTextField.setMaxStringLength(3);
		this.blueTextField.setMaxStringLength(3);
		this.alphaTextField.setMaxStringLength(3);
		this.redTextField.setText(String.valueOf(this.RGBA[0]));
		this.greenTextField.setText(String.valueOf(this.RGBA[1]));
		this.blueTextField.setText(String.valueOf(this.RGBA[2]));
		this.alphaTextField.setText(String.valueOf(this.RGBA[3]));
	}

	@Override
	public void updateScreen() {

		this.redDecBtn.enabled=(this.RGBA[0]>0);
		this.greenDecBtn.enabled=(this.RGBA[1]>0);
		this.blueDecBtn.enabled=(this.RGBA[2]>0);
		this.alphaDecBtn.enabled=(this.RGBA[3]>0);
		this.redIncBtn.enabled=(this.RGBA[0]<255);
		this.greenIncBtn.enabled=(this.RGBA[1]<255);
		this.blueIncBtn.enabled=(this.RGBA[2]<255);
		this.alphaIncBtn.enabled=(this.RGBA[3]<255);
		this.redTextField.updateCursorCounter();
		this.greenTextField.updateCursorCounter();
		this.blueTextField.updateCursorCounter();
		this.alphaTextField.updateCursorCounter();
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		if (isVisible) {
			this.RGBA = new int[] { 225, 225, 225, 255 };
			this.redTextField.setText(Integer.toString(RGBA[0]));
			this.greenTextField.setText(Integer.toString(RGBA[1]));
			this.blueTextField.setText(Integer.toString(RGBA[2]));
			this.alphaTextField.setText(Integer.toString(RGBA[3]));
		}
		for (int i = 0; i < this.textFieldList.size(); i++) {
			this.textFieldList.get(i).setVisible(isVisible);
			this.textFieldList.get(i).setEnabled(isVisible);
		}
		for (int i = 0; i < this.buttonList.size(); i++) {
			((GuiButton)this.buttonList.get(i)).visible = isVisible;
			((GuiButton)this.buttonList.get(i)).enabled = isVisible;
		}
	}
}

