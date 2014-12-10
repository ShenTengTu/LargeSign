package com.roripantsu.largesign.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import org.apache.commons.lang3.text.StrBuilder;

import com.roripantsu.common.ModI18n;
import com.roripantsu.guilib.GuiMainScreen;
import com.roripantsu.guilib.GuiSubScreen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of Large sign text and font style editor
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiTextAndFontStyleEditor extends GuiSubScreen {
	
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
	private FontStyles currentFontStyle;
	private GuiButton shadowBtn;
	private GuiButton boldBtn;
	private GuiButton strikethroughBtn;
	private GuiButton underlineBtn;
	private GuiButton italicBtn;
	private GuiButton resetBtn;
	private GuiButton resetAllBtn;
	private static final String[] OnOffStr = { "[off]", "[on]" };
	GuiTextField editTextField;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String title=ModI18n.gui(guiName, "title", new Object[0]);
	private String editMsg=ModI18n.gui(guiName, "editMsg", new Object[0]);
	private String shadow=ModI18n.gui(guiName, "shadow", new Object[0]);
	private String reset=ModI18n.gui(guiName, "reset", new Object[0]);
	private String resetAll=ModI18n.gui(guiName, "resetAll", new Object[0]);
	//<--Localize Gui


	public CustomGuiTextAndFontStyleEditor(int ID,Minecraft MC,FontRenderer font,GuiMainScreen parent,
			int x, int y,int width,int height) {
		super(ID, MC, font, parent, x, y, width, height);
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		if (this.isVisible) {
			this.editTextField.drawTextBox();
			this.drawCenteredString(this.fontRendererObj, title,
					this.GroupX+this.width/2, this.GroupY, 16777215);
			this.drawCenteredString(this.fontRendererObj, editMsg,
					this.GroupX+this.width/2, this.GroupY+fixH(this.gridHeight*10), 16777215);
		}
		
	}

	@Override
	protected void keyTyped(char character, int code) {
		this.editTextField.textboxKeyTyped(character, code);
	}

	@Override
	protected void mouseClicked(int x, int y, int buttonClicked) {
		if (buttonClicked == 0){
		this.editTextField.mouseClicked(x, y, buttonClicked);
		}
	}

	/**
	 *Define action performed of the button.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == this.shadowBtn.id) {
				this.currentFontStyle = FontStyles.SHADOW;
				FontStyles.SHADOW.enable = !FontStyles.SHADOW.enable;

				if (button.displayString.contains(OnOffStr[0]))
					button.displayString = button.displayString.replace(OnOffStr[0],
							OnOffStr[1]);
				else
					button.displayString = button.displayString.replace(OnOffStr[1],
							OnOffStr[0]);
			}
			if (button.id == this.boldBtn.id) {
				this.currentFontStyle = FontStyles.BOLD;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (button.id == this.strikethroughBtn.id) {
				this.currentFontStyle = FontStyles.STRIKETHROUGH;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (button.id == this.underlineBtn.id) {
				this.currentFontStyle = FontStyles.UNDERLINE;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (button.id == this.italicBtn.id) {
				this.currentFontStyle = FontStyles.ITALIC;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (button.id == this.resetBtn.id) {
				this.currentFontStyle = FontStyles.RESET;
				this.editTextField.writeText(this.currentFontStyle.styleDisplayCode);
				this.editTextField.setFocused(true);
			}

			if (button.id == this.resetAllBtn.id) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(this.shadowBtn = 
				new GuiButton(id, this.GroupX, this.GroupY+fixH(this.gridHeight*3), fixW(this.gridWidth*9),20, shadow+" "+ OnOffStr[0]));
		this.buttonList.add(this.boldBtn = 
				new GuiButton(id + 1, this.GroupX+fixW(this.gridWidth*9), this.GroupY+fixH(this.gridHeight*3),fixW(this.gridWidth*9),20, "\u00a7l[B]\u00a7r "+ FontStyles.BOLD.styleDisplayCode));
		this.buttonList.add(this.strikethroughBtn = 
				new GuiButton(id + 2,this.GroupX+fixW(this.gridWidth*18), this.GroupY+fixH(this.gridHeight*3) ,fixW(this.gridWidth*9),20, "\u00a7m[S]\u00a7r "+ FontStyles.STRIKETHROUGH.styleDisplayCode));
		this.buttonList.add(this.underlineBtn = 
				new GuiButton(id + 3,this.GroupX+fixW(this.gridWidth*27), this.GroupY+fixH(this.gridHeight*3) ,fixW(this.gridWidth*9),20, "\u00a7n[U]\u00a7r "+ FontStyles.UNDERLINE.styleDisplayCode));
		this.buttonList.add(this.italicBtn = 
				new GuiButton(id + 4,this.GroupX+fixW(this.gridWidth*36), this.GroupY+fixH(this.gridHeight*3) ,fixW(this.gridWidth*9),20, "\u00a7o[I]\u00a7r "+ FontStyles.ITALIC.styleDisplayCode));
		this.buttonList.add(this.resetBtn = 
				new GuiButton(id + 5,this.GroupX+fixW(this.gridWidth*45), this.GroupY+fixH(this.gridHeight*3) ,fixW(this.gridWidth*9),20, reset+" "+ FontStyles.RESET.styleDisplayCode));
		this.buttonList.add(this.resetAllBtn = 
				new GuiButton(id + 6,this.GroupX+fixW(this.gridWidth*54), this.GroupY+fixH(this.gridHeight*3) ,fixW(this.gridWidth*9),20, resetAll));
		this.parentButtonList.addAll(this.buttonList);
		
		this.editTextField = new GuiTextField(this.fontRendererObj,
				this.GroupX, this.GroupY+fixH(this.gridHeight*14), this.width, fixH(this.gridHeight*5));
		this.editTextField.setMaxStringLength(256);
		
		this.currentFontStyle = FontStyles.RESET;
		FontStyles.SHADOW.enable = false;
	}

	@Override
	public void updateScreen() {
		this.editTextField.updateCursorCounter();
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
			((GuiButton)this.buttonList.get(i)).visible = isVisible;
			((GuiButton)this.buttonList.get(i)).enabled = isVisible;
		}

	}
	
	protected String formatStringChange(String str) {
		String[] displycodes = {
				FontStyles.BOLD.styleDisplayCode,
				FontStyles.ITALIC.styleDisplayCode,
				FontStyles.RESET.styleDisplayCode,
				FontStyles.STRIKETHROUGH.styleDisplayCode,
				FontStyles.UNDERLINE.styleDisplayCode };

		String[] formatcodes = {
				FontStyles.BOLD.styleCode,
				FontStyles.ITALIC.styleCode,
				FontStyles.RESET.styleCode,
				FontStyles.STRIKETHROUGH.styleCode,
				FontStyles.UNDERLINE.styleCode };

		StrBuilder strb = new StrBuilder(str);
		for (int i = 0; i < displycodes.length; i++)
			strb.replaceAll(displycodes[i], formatcodes[i]);

		return strb.toString();
	}
	
	private String formatStringClear(String str) {

		String[] displycodes = {
				FontStyles.BOLD.styleDisplayCode,
				FontStyles.ITALIC.styleDisplayCode,
				FontStyles.RESET.styleDisplayCode,
				FontStyles.STRIKETHROUGH.styleDisplayCode,
				FontStyles.UNDERLINE.styleDisplayCode };

		StrBuilder strb = new StrBuilder(str);
		if (this.editTextField.getText() != null)
			for (String c : displycodes)
				strb.deleteAll(c);

		return strb.toString();
	}
}

