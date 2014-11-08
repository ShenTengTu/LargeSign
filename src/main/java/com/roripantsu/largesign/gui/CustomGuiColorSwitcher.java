package com.roripantsu.largesign.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of Color switcher
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiColorSwitcher extends GuiScreen {

	private static final int[] ButtonSize = { 18, 18 };
	private static final int[] textFieldSize = { 20, 18 };
	protected GuiButton alphaDecBtn;
	protected GuiButton alphaIncBtn;
	protected GuiTextField alphaTextField;
	protected GuiButton blueDecBtn;
	protected GuiButton blueIncBtn;
	protected GuiTextField blueTextField;
	protected String[] Color16Code = { "#000000", "#000080", "#0000FF",
			"#008000", "#008080", "#00FF00", "#00FFFF", "#808080", "#800000",
			"#800080", "#808000", "#C0C0C0", "#FF0000", "#FF00FF", "#FFFF00",
			"#FFFFFF" };
	protected CustomGuiButton[] ColorBolock = new CustomGuiButton[16];
	protected Color ColorObj;
	protected GuiButton greenDecBtn;
	protected GuiButton greenIncBtn;
	protected GuiTextField greenTextField;
	protected final int lastButtonID;
	protected GuiButton redDecBtn;
	protected GuiButton redIncBtn;
	protected GuiTextField redTextField;
	private List<GuiButton> buttonList = new ArrayList<GuiButton>();
	private int ID;
	private boolean isVisible;
	private int[] RGBA = { 225, 225, 225, 255 };
	private List<GuiTextField> textFieldList = new ArrayList<GuiTextField>();
	private int xPosition;
	private int yPosition;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String title=LocalizeGui.guiLocalString(guiName, "title", new Object[0]);
	//<--Localize Gui
	
	public CustomGuiColorSwitcher(Minecraft MC, FontRenderer FontRenderer,
			List<GuiButton> screenButtonList, int ID, int X, int Y) {
		
		this.fontRendererObj=FontRenderer;
		this.ID=ID;
		this.lastButtonID=ID + 23;
		this.xPosition=X;
		this.yPosition=Y;

		this.ColorObj = new Color(this.RGBA[0], this.RGBA[1], this.RGBA[2],
				this.RGBA[3]);
		this.textFieldList.add(this.redTextField = new GuiTextField(
				this.fontRendererObj, this.xPosition - textFieldSize[0] / 2,
				this.yPosition, textFieldSize[0], textFieldSize[1]));
		this.textFieldList.add(this.greenTextField = new GuiTextField(
				this.fontRendererObj, this.xPosition - textFieldSize[0] / 2,
				this.yPosition + (textFieldSize[1] + 2), textFieldSize[0],
				textFieldSize[1]));
		this.textFieldList.add(this.blueTextField = new GuiTextField(
				this.fontRendererObj, this.xPosition - textFieldSize[0] / 2,
				this.yPosition + (textFieldSize[1] + 2) * 2, textFieldSize[0],
				textFieldSize[1]));
		this.textFieldList.add(this.alphaTextField = new GuiTextField(
				this.fontRendererObj, this.xPosition - textFieldSize[0] / 2,
				this.yPosition + (textFieldSize[1] + 2) * 3, textFieldSize[0],
				textFieldSize[1]));
		this.redTextField.setMaxStringLength(3);
		this.greenTextField.setMaxStringLength(3);
		this.blueTextField.setMaxStringLength(3);
		this.alphaTextField.setMaxStringLength(3);
		this.redTextField.setText(String.valueOf(this.RGBA[0]));
		this.greenTextField.setText(String.valueOf(this.RGBA[1]));
		this.blueTextField.setText(String.valueOf(this.RGBA[2]));
		this.alphaTextField.setText(String.valueOf(this.RGBA[3]));
		this.buttonList.add(this.redDecBtn = new GuiButton(ID, this.xPosition
				- textFieldSize[0] / 2 - ButtonSize[0] - 1, this.yPosition,
				ButtonSize[0], ButtonSize[1]+2, "R-8"));
		this.buttonList.add(this.redIncBtn = new GuiButton(ID + 1,
				this.xPosition + textFieldSize[0] / 2 + 1, this.yPosition,
				ButtonSize[0], ButtonSize[1]+2, "R+8"));
		this.buttonList.add(this.greenDecBtn = new GuiButton(ID + 2,
				this.xPosition - textFieldSize[0] / 2 - ButtonSize[0] - 1,
				this.yPosition + (textFieldSize[1] + 2), ButtonSize[0],
				ButtonSize[1]+2, "G-8"));
		this.buttonList.add(this.greenIncBtn = new GuiButton(ID + 3,
				this.xPosition + textFieldSize[0] / 2 + 1, this.yPosition
						+ (textFieldSize[1] + 2), ButtonSize[0], ButtonSize[1]+2,
				"G+8"));
		this.buttonList.add(this.blueDecBtn = new GuiButton(ID + 4,
				this.xPosition - textFieldSize[0] / 2 - ButtonSize[0] - 1,
				this.yPosition + (textFieldSize[1] + 2) * 2, ButtonSize[0],
				ButtonSize[1]+2, "B-8"));
		this.buttonList.add(this.blueIncBtn = new GuiButton(ID + 5,
				this.xPosition + textFieldSize[0] / 2 + 1, this.yPosition
						+ (textFieldSize[1] + 2) * 2, ButtonSize[0],
				ButtonSize[1]+2, "B+8"));
		this.buttonList.add(this.alphaDecBtn = new GuiButton(ID + 6,
				this.xPosition - textFieldSize[0] / 2 - ButtonSize[0] - 1,
				this.yPosition + (textFieldSize[1] + 2) * 3, ButtonSize[0],
				ButtonSize[1]+2, "A-8"));
		this.buttonList.add(this.alphaIncBtn = new GuiButton(ID + 7,
				this.xPosition + textFieldSize[0] / 2 + 1, this.yPosition
						+ (textFieldSize[1] + 2) * 3, ButtonSize[0],
				ButtonSize[1]+2, "A+8"));
		for (int i = 0; i < this.ColorBolock.length; i++) {
			int x = this.xPosition + textFieldSize[0] / 2 + ButtonSize[0] + 4
					+ i % 4 * (ButtonSize[0]);
			int yshift = (i < 16) ? ((i < 12) ? ((i < 8) ? (i < 4 ? 0
					: (ButtonSize[0])) : (ButtonSize[0]) * 2)
					: (ButtonSize[0]) * 3) : 0;
			int y = this.yPosition + yshift;
			this.ColorBolock[i] = new CustomGuiButton(ID + 8 + i, x, y,
					ButtonSize[0], ButtonSize[0],
					CustomGuiButton.buttonStyle.COLORBLOCK,
					Color.decode(this.Color16Code[i]));
		}
		this.buttonList.addAll(Arrays.asList(this.ColorBolock));
		screenButtonList.addAll(this.buttonList);
	}

	@Override
	public void drawScreen(int mouseX,int mouseY,float par3) {
		if (this.isVisible) {
			this.drawCenteredString(this.fontRendererObj, 
					title,
					this.xPosition + ButtonSize[0] * 2, this.yPosition - 15,
					16777215);
			this.redTextField.drawTextBox();
			this.blueTextField.drawTextBox();
			this.greenTextField.drawTextBox();
			this.alphaTextField.drawTextBox();

		}

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
			this.buttonList.get(i).visible = isVisible;
			this.buttonList.get(i).enabled = isVisible;
		}
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

	@Override
	protected void actionPerformed(GuiButton btn) {
		if (btn.enabled) {
			if (btn.id == this.ID)
				this.RGBA[0] -= 8;
			if (btn.id == this.ID + 1)
				this.RGBA[0] += 8;
			if (btn.id == this.ID + 2)
				this.RGBA[1] -= 8;
			if (btn.id == this.ID + 3)
				this.RGBA[1] += 8;
			if (btn.id == this.ID + 4)
				this.RGBA[2] -= 8;
			if (btn.id == this.ID + 5)
				this.RGBA[2] += 8;
			if (btn.id == this.ID + 6)
				this.RGBA[3] -= 8;
			if (btn.id == this.ID + 7)
				this.RGBA[3] += 8;
			if (btn.id >= this.ID + 8 && btn.id <= this.ID + 23) {
				this.RGBA[0] = ((CustomGuiButton) btn).color.getRed();
				this.RGBA[1] = ((CustomGuiButton) btn).color.getGreen();
				this.RGBA[2] = ((CustomGuiButton) btn).color.getBlue();
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

	@Override
	protected void keyTyped(char par1, int par2) {
		if (!Character.isISOControl(par1) && !Character.isDigit(par1)) {
			par1 = '0';
		}
		
		this.redTextField.textboxKeyTyped(par1, par2);
		this.greenTextField.textboxKeyTyped(par1, par2);
		this.blueTextField.textboxKeyTyped(par1, par2);
		this.alphaTextField.textboxKeyTyped(par1, par2);

		if (Character.isDigit(par1)) {
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
	protected void mouseClicked(int mouseX, int mouseY, int par3) {
		if (par3 == 0) {
			this.redTextField.mouseClicked(mouseX, mouseY, par3);
			this.greenTextField.mouseClicked(mouseX, mouseY, par3);
			this.blueTextField.mouseClicked(mouseX, mouseY, par3);
			this.alphaTextField.mouseClicked(mouseX, mouseY, par3);
		}

	}

}
