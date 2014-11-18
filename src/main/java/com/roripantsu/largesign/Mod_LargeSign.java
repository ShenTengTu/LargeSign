package com.roripantsu.largesign;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.proxy.ProxyCommon;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 *Minecraft Mod - Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, useMetadata = true)
public class Mod_LargeSign {
	
	@Instance(value = References.MODID)
	public static Mod_LargeSign instance;

	@SidedProxy(clientSide = "com.roripantsu.largesign.proxy.ProxyClient", serverSide = "com.roripantsu.largesign.proxy.ProxyServer")
	public static ProxyCommon proxy;

	/* CreativeTab */
	public static final CreativeTabs tab_modRoriPantsu;
	static{
		tab_modRoriPantsu=new CreativeTabs("tab_modRoriPantsu") {
			@Override
			public Item getTabIconItem() {
				return GameRegistry.findItem(References.MODID,
						Block_LargeSign.class.getSimpleName());
			}
		};
	}

	public Mod_LargeSign() {}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
}
