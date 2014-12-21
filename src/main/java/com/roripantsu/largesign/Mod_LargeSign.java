package com.roripantsu.largesign;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.proxy.ProxyCommon;

/**
 *Minecraft Mod - Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
@Mod(modid = Mod_LargeSign.MODID, name = Mod_LargeSign.NAME, version = Mod_LargeSign.VERSION,dependencies = "required-after:Forge")
public class Mod_LargeSign {
	
	public static final String MCVERSION = "1.8";
	public static final String MODID = "mod_LargeSign";
	public static final String NAME = "Large Sign";
	public static final String VERSION = "2.0";
	
	@Mod.Metadata(Mod_LargeSign.MODID)
    public static ModMetadata metadata;
	
	@Mod.Instance(Mod_LargeSign.MODID)
	public static Mod_LargeSign instance;

	@SidedProxy(clientSide = "com.roripantsu.largesign.proxy.ProxyClient", serverSide = "com.roripantsu.largesign.proxy.ProxyServer")
	public static ProxyCommon proxy;

	/* CreativeTab */
	public static final CreativeTabs tab_modRoriPantsu;
	static{
		tab_modRoriPantsu=new CreativeTabs("tab_modRoriPantsu") {
			@Override
			public Item getTabIconItem() {
				return GameRegistry.findItem(Mod_LargeSign.MODID,
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