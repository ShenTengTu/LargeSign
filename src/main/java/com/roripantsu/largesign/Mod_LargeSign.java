package com.roripantsu.largesign;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.roripantsu.common.ModInfo;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.proxy.ProxyCommon;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *Minecraft Mod - Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, useMetadata = true)
public class Mod_LargeSign {
	
	@Instance(value = ModInfo.MODID)
	public static Mod_LargeSign instance;

	@SidedProxy(clientSide = "com.roripantsu.largesign.proxy.ProxyClient", serverSide = "com.roripantsu.largesign.proxy.ProxyServer")
	public static ProxyCommon proxy;

	/* CreativeTab */
	public static final CreativeTabs tab_modRoriPantsu;
	static{
		tab_modRoriPantsu=new CreativeTabs("tab_modRoriPantsu") {
			@Override
			public Item getTabIconItem() {
				return GameRegistry.findItem(ModInfo.MODID,
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