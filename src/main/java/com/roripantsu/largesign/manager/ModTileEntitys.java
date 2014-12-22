package com.roripantsu.largesign.manager;

import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.roripantsu.largesign.tileentity.TileEntityLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSignRenderer;

public class ModTileEntitys {

	public static Class<TileEntityLargeSign> tileEntityLargeSign = TileEntityLargeSign.class;
	
	public static void register(){
		
		ClientRegistry.registerTileEntity(tileEntityLargeSign,
				RegistryName.TileEntityLargeSign,
				new TileEntityLargeSignRenderer());
	}
}
