package com.roripantsu.largesign.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.apache.commons.lang3.text.StrBuilder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of Large sign text and font style editor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiTextAndFontStyleEditor extends GuiScreen {

	public static enum FontStyles {

		BOLD("\u00a7l", "<b:>"), ITALIC("\u00a7o", "<i:>"), RESET(
		"\u00a7r", "</r>"), RESETALL, SHADOW(false), STRIKETHROUGH(
		"\u00a7m", "<s:>"), UNDERLINE("\u00a7n", "<u:>");

		public boolean enable = false;
		public final String styleCode;
		public final String styleDisplayCode;

		private FontStyles() {
			this.styleCode = "";
			this.styleDisplayCode = "";
		}

		private FontStyles(boolean enable) {
			this.styleCode = "";
			this.styleDisplayCode = "";
			this.enable = enable;
		}

		private FontStyles(String code, String displayCode) {
			this.styleCode = code;
			this.styleDisplayCode = displayCode;
		}
	}

	private static final int[] ButtonSize = { 60, 18 };
	private static final String[] OnOffStr = { "[off]", "[on]" };
	protected GuiButton boldBtn;
	protected FontStyles currentFontStyle;
	protected GuiTextField editTextField;
	protected GuiButton italicBtn;
	protected final int lastButtonID;
	protected GuiButton resetAllBtn;
	protected GuiButton resetBtn;
	protected GuiButton selectedButton;
	protected GuiButton shadowBtn;
	protected GuiButton strikethroughBtn;
	protected GuiButton underlineBtn;

	private List<GuiButton> buttonList = new ArrayList<GuiButton>();
	private int ID;
	private boolean isVisible;
	private int xPosition;
	private int yPosition;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String title=LocalizeGui.guiLocalString(guiName, "title", new Object[0]);
	private String editMsg=LocalizeGui.guiLocalString(guiName, "editMsg", new Object[0]);
	private String shadow=LocalizeGui.guiLocalString(guiName, "shadow", new Object[0]);
	private String reset=LocalizeGui.guiLocalString(guiName, "reset", new Object[0]);
	private String resetAll=LocalizeGui.guiLocalString(guiName, "resetAll", new Object[0]);
	//<--Localize Gui

	public CustomGuiTextAndFontStyleEditor(Minecraft MC,
			FontRenderer FontRenderer, List<GuiButton> screenButtonList,
			int ID, int X, int Y) {
		
		this.fontRendererObj=FontRenderer;
		this.ID=ID;
		this.lastButtonID=ID + 6;
		this.xPosition=X;
		this.yPosition=Y;
		
		this.currentFontStyle = FontStyles.RESET;
		this.editTextField = new GuiTextField(this.fontRendererObj,
				this.xPosition * 4 - 360 / 2, this.yPosition / 6 * 18, 360, 15);
		this.editTextField.setMaxStringLength(256);
		this.buttonList.add(this.shadowBtn = new GuiButton(ID, this.xPosition
				- ButtonSize[0] / 2, this.yPosition, ButtonSize[0],
				ButtonSize[1]+2, shadow+" "+ OnOffStr[0]));
		this.buttonList.add(this.boldBtn = new GuiButton(ID + 1, this.xPosition
				- ButtonSize[0] / 2, this.yPosition + (ButtonSize[1] + 2),
				ButtonSize[0] / 2, ButtonSize[1]+2, "\u00a7l[B]\u00a7r "
						+ FontStyles.BOLD.styleDisplayCode));
		this.buttonList.add(this.strikethroughBtn = new GuiButton(ID + 2,
				this.xPosition, this.yPosition + (ButtonSize[1] + 2),
				ButtonSize[0] / 2, ButtonSize[1]+2, "\u00a7m[S]\u00a7r "
						+ FontStyles.STRIKETHROUGH.styleDisplayCode));
		this.buttonList.add(this.underlineBtn = new GuiButton(ID + 3,
				this.xPosition - ButtonSize[0] / 2, this.yPosition
						+ (ButtonSize[1] + 2) * 2, ButtonSize[0] / 2,
				ButtonSize[1]+2, "\u00a7n[U]\u00a7r "
						+ FontStyles.UNDERLINE.styleDisplayCode));
		this.buttonList.add(this.italicBtn = new GuiButton(ID + 4,
				this.xPosition, this.yPosition + (ButtonSize[1] + 2) * 2,
				ButtonSize[0] / 2, ButtonSize[1]+ 2, "\u00a7o[I]\u00a7r "
						+ FontStyles.ITALIC.styleDisplayCode));
		this.buttonList.add(this.resetBtn = new GuiButton(ID + 5,
				this.xPosition - ButtonSize[0] / 2, this.yPosition
						+ (ButtonSize[1] + 2) * 3, ButtonSize[0],
				ButtonSize[1]+ 2, reset+" "+ FontStyles.RESET.styleDisplayCode));
		this.buttonList.add(this.resetAllBtn = new GuiButton(ID + 6,
				this.xPosition - ButtonSize[0] / 2, this.yPosition
						+ (ButtonSize[1] + 2) * 4, ButtonSize[0],
				ButtonSize[1]+2, resetAll));
		screenButtonList.addAll(this.buttonList);
		CustomGuiTextAndFontStyleEditor.FontStyles.SHADOW.enable = false;
	}

	@Override
	public void drawScreen(int mouseX,int mouseY,float par3) {
		if (this.isVisible) {
			this.editTextField.drawTextBox();
			this.drawCenteredString(this.fontRendererObj, title,
					this.xPosition, this.yPosition - 13, 16777215);
			this.drawCenteredString(this.fontRendererObj, editMsg,
					this.xPosition * 4, this.yPosition / 6 * 16, 16777215);
		}
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.editTextField.setVisible(isVisible);
		this.editTextField.setEnabled(isVisible);
		this.editTextField.setFocused(isVisible);

		if (!isVisible) {
			this.editTextField.setText("");
			FontStyles.SHADOW.enable = isVisible;
		}
		for (int i = 0; i < this.buttonList.size(); i++) {
			this.buttonList.get(i).visible = isVisible;
			this.buttonList.get(i).enabled = isVisible;
		}

	}

	@Override
	public void updateScreen() {
		this.editTextField.updateCursorCounter();
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		if (btn.enabled) {
			if (btn.id == this.ID) {
				this.currentFontStyle = FontStyles.SHADOW;
				FontStyles.SHADOW.enable = !FontStyles.SHADOW.enable;

				if (btn.displayString.contains(OnOffStr[0]))
					btn.displayString = btn.displayString.replace(OnOffStr[0],
							OnOffStr[1]);
				else
					btn.displayString = btn.displayString.replace(OnOffStr[1],
							OnOffStr[0]);
			}
			if (btn.id == this.ID + 1) {
				this.currentFontStyle = FontStyles.BOLD;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (btn.id == this.ID + 2) {
				this.currentFontStyle = FontStyles.STRIKETHROUGH;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (btn.id == this.ID + 3) {
				this.currentFontStyle = FontStyles.UNDERLINE;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (btn.id == this.ID + 4) {
				this.currentFontStyle = FontStyles.ITALIC;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (btn.id == this.ID + 5) {
				this.currentFontStyle = FontStyles.RESET;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (btn.id == this.ID + 6) {
				this.currentFontStyle = FontStyles.RESETALL;
				this.editTextField.setText(this
						.formatStringClear(this.editTextField.getText()));
				FontStyles.SHADOW.enable = false;
				if (this.shadowBtn.displayString.contains(OnOffStr[1]))
					this.shadowBtn.displayString = this.shadowBtn.displayString
							.replace(OnOffStr[1], OnOffStr[0]);
			}

		}
	}

	protected String formatStringChange(String str) {
		String[] displycodes = {
				CustomGuiTextAndFontStyleEditor.FontStyles.BOLD.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.ITALIC.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.RESET.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.STRIKETHROUGH.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.UNDERLINE.styleDisplayCode };

		String[] formatcodes = {
				CustomGuiTextAndFontStyleEditor.FontStyles.BOLD.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.ITALIC.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.RESET.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.STRIKETHROUGH.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.UNDERLINE.styleCode };

		StrBuilder strb = new StrBuilder(str);
		for (int i = 0; i < displycodes.length; i++)
			strb.replaceAll(displycodes[i], formatcodes[i]);

		return strb.toString();
	}

	@Override
	protected void keyTyped(char par1, int par2) {

		this.editTextField.textboxKeyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) {
		if (par3 == 0){
		this.editTextField.mouseClicked(mouseX, mouseY, par3);
		}
	}

	private String formatStringClear(String str) {

		String[] displycodes = {
				CustomGuiTextAndFontStyleEditor.FontStyles.BOLD.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.ITALIC.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.RESET.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.STRIKETHROUGH.styleDisplayCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.UNDERLINE.styleDisplayCode };

		StrBuilder strb = new StrBuilder(str);
		if (this.editTextField.getText() != null)
			for (String c : displycodes)
				strb.deleteAll(c);

		return strb.toString();
	}
	
}
