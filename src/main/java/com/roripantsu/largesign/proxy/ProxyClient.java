package com.roripantsu.largesign.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.roripantsu.largesign.manager.ModTileEntitys;

/**
 *Differentiate between the combined client and the dedicated server.
 *this is client proxy.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyClient extends ProxyCommon {
		
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
		/* TileEntitys */
		ModTileEntitys.register();
		
    }
	
}
