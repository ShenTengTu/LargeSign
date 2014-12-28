package com.roripantsu.largesign;

import java.util.Arrays;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.roripantsu.common.network.ModPacketPipeline;
import com.roripantsu.largesign.proxy.ProxyCommon;

/**
 *Minecraft Mod - Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
@Mod(modid = Mod_LargeSign.MODID,
name = Mod_LargeSign.NAME, 
version = Mod_LargeSign.VERSION,
acceptableRemoteVersions="*")
public class Mod_LargeSign{
	
	public static final String MCVERSION = "1.8";
	public static final String MODID = "LargeSign";
	public static final String NAME = "Large Sign";
	public static final String VERSION = "2.0";
	
	@Mod.Metadata(Mod_LargeSign.MODID)
    public static ModMetadata metadata = new ModMetadata();
	
	@Mod.Instance(Mod_LargeSign.MODID)
	public static Mod_LargeSign instance = new Mod_LargeSign();

	@SidedProxy(clientSide = "com.roripantsu.largesign.proxy.ProxyClient", serverSide = "com.roripantsu.largesign.proxy.ProxyServer")
	public static ProxyCommon proxy;
	
	public static ModPacketPipeline packetPipeline;
	
	public Mod_LargeSign(){
		metadata.modId       = Mod_LargeSign.MODID;
		metadata.name        = Mod_LargeSign.NAME;
		metadata.version     = Mod_LargeSign.VERSION;
		metadata.credits     = "Love games,love codes";
		metadata.authorList  = Arrays.asList("RoriPantsu");
		metadata.description = "<Large Sign> which has bigger block size and font size.You can edit text or insert item icon on it.";
		metadata.url         = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2269082";
		metadata.updateUrl   = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2269082";
		metadata.screenshots = new String[0];
		metadata.logoFile    = "/logo.png";	
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		
	}
		
}