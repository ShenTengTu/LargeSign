package com.roripantsu.largesign.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
