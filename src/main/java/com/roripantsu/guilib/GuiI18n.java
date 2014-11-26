package com.roripantsu.guilib;

import net.minecraft.client.resources.I18n;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *For Gui localization.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class GuiI18n {

	public static String localize(String guiName,String comp,Object... param){
		String key="gui."+guiName+"."+comp;
		return I18n.format(key, param);
	}
}
