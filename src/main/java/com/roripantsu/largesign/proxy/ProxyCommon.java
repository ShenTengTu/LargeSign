package com.roripantsu.largesign.proxy;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.roripantsu.common.network.PacketPipeline;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.ItemBlock_LargeSign;
import com.roripantsu.largesign.packet.CPacketUpdateLargeSign;
import com.roripantsu.largesign.packet.CustomPacketEventListener;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.packet.SPacketUpdateLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *Differentiate between the combined client and the dedicated server.
 *Superclass of ProxyClient and ProxyServer.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyCommon {
	
	public Block block_LargeSign;
	public List<ItemStack> itemList;
	public PacketPipeline packetPipeline;
	
	public void init(FMLInitializationEvent e) {
		
		packetPipeline=new PacketPipeline();
		packetPipeline.registeEventListener(new CustomPacketEventListener(packetPipeline.getPacketsList()));
		packetPipeline.registerPacket(SPacketUpdateLargeSign.class);
		packetPipeline.registerPacket(SPacketLargeSignEditorOpen.class);
		packetPipeline.registerPacket(CPacketUpdateLargeSign.class);
	}

    public void postInit(FMLPostInitializationEvent e) {
    	packetPipeline.postInitialise();
    }

    public void preInit(FMLPreInitializationEvent e) {
		/* Block */
		block_LargeSign = new Block_LargeSign(TileEntityLargeSign.class);
		GameRegistry.registerBlock(block_LargeSign,ItemBlock_LargeSign.class,
				Block_LargeSign.class.getSimpleName());

		/* Recipe */
		ItemStack[] itemStacks=new ItemStack[]{
				new ItemStack(Blocks.planks,1,0),
				new ItemStack(Blocks.planks,1,1),
				new ItemStack(Blocks.planks,1,2),
				new ItemStack(Blocks.planks,1,3),
				new ItemStack(Blocks.planks,1,4),
				new ItemStack(Blocks.planks,1,5)};
		for(int i=0;i<itemStacks.length;i++){
			GameRegistry.addRecipe(new ItemStack(block_LargeSign, 3,i), new Object[] {
				"SSS", "SIS", "SSS", 'S', itemStacks[i] ,'I',Items.item_frame});
		}
		
    }
    
}
