package com.roripantsu.largesign.proxy;

import com.roripantsu.largesign.tileentity.TileEntityLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSignRenderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
		this.ClientRegistry();
		
    }
	
	private void ClientRegistry() {
		/* TileEntity */
		ClientRegistry.registerTileEntity(TileEntityLargeSign.class,
				TileEntityLargeSign.class.getSimpleName(),
				new TileEntityLargeSignRenderer());
		
	}
	
}
