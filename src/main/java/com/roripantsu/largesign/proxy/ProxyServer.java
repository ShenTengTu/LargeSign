package com.roripantsu.largesign.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 *Differentiate between the combined client and the dedicated server.
 *this is server proxy.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyServer extends ProxyCommon {

	@Override
    public void init(FMLInitializationEvent e) {
		super.init(e);
    }
	
	@Override
    public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
    }
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
    }
}
