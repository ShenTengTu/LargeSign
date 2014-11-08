package com.roripantsu.largesign.proxy;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.roripantsu.largesign.References;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.Item_LargeSign;
import com.roripantsu.largesign.network.CPacketUpdateLargeSign;
import com.roripantsu.largesign.network.CustomPacketEventListener;
import com.roripantsu.largesign.network.PacketPipeline;
import com.roripantsu.largesign.network.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.network.SPacketUpdateLargeSign;
import com.roripantsu.largesign.texture.CustomTextureSprite;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 *Differentiate between the combined client and the dedicated server.
 *Superclass of ProxyClient and ProxyServer.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyCommon {
	
	public Block block_LargeSign;
	public final int channelKey=0;
	public Item item_LargeSign;
	public List<ItemStack> itemList;
	public PacketPipeline packetPipeline;
	public CustomTextureSprite textureBlockLargeSign;
	
	public void init(FMLInitializationEvent e) {
		
		packetPipeline=new PacketPipeline();
		packetPipeline.addNewChannel(channelKey, References.MODID);
		packetPipeline.registeEventListener(new CustomPacketEventListener(
				channelKey, packetPipeline.getPacketsList()));
		packetPipeline.registerPacket(SPacketUpdateLargeSign.class);
		packetPipeline.registerPacket(SPacketLargeSignEditorOpen.class);
		packetPipeline.registerPacket(CPacketUpdateLargeSign.class);
		
		
	}

    public void postInit(FMLPostInitializationEvent e) {
    	packetPipeline.postInitialise();
    }

    public void preInit(FMLPreInitializationEvent e) {
		/* Item */
		item_LargeSign = new Item_LargeSign();
		GameRegistry.registerItem(item_LargeSign,
				Item_LargeSign.class.getSimpleName(), References.MODID);

		/* Block */
		block_LargeSign = new Block_LargeSign(TileEntityLargeSign.class);
		GameRegistry.registerBlock(block_LargeSign,null,
				Block_LargeSign.class.getSimpleName());
		//par2:set null let Block not be an ItemBlock

		/* Recipe */
		GameRegistry.addRecipe(new ItemStack(item_LargeSign, 3), new Object[] {
				"SxS", "xIx", "SxS", 'S', Items.sign ,'I',Items.item_frame});
    }
    
}
