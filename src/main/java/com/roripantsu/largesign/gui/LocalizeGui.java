package com.roripantsu.largesign.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class LocalizeGui {

	public static String guiLocalString(String guiName,String comp,Object... param){
		String key="gui."+guiName+"."+comp;
		return I18n.format(key, param);
	}
}
