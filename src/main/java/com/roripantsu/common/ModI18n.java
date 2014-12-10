package com.roripantsu.common;

import net.minecraft.client.resources.I18n;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *For Mod localization.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class ModI18n {
	
	/**
	 * For GUI localization.Key format = gui.<b>guiScreenName</b>.<b>componentName</b>
	 * @param guiName Name of Class extends {@link net.minecraft.client.gui.Gui}
	 * @param componentName Name of the component
	 * @param arg Arguments referenced by the format specifiers in localized string in lang file.
	 */
	public static String gui(String guiName,String componentName,Object... arg){
		String key="gui."+guiName+"."+componentName;
		return I18n.format(key, arg);
	}
}
