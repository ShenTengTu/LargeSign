package com.roripantsu.largesign.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Maps;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSignRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;

/**
 *Differentiate between the combined client and the dedicated server.
 *this is client proxy.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyClient extends ProxyCommon {

	@Override
    public void init(FMLInitializationEvent e) {
		super.init(e);
		this.getItemList(itemList = new ArrayList<ItemStack>());
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
	
	private void getItemList(List<ItemStack> itemList) {
		Map<String, Integer> map = Maps.newHashMap();
		GameData.getItemRegistry().serializeInto(map);
		List<Integer> IDList = new ArrayList<Integer>(map.values());
		Collections.sort(IDList);
		
		//ignore item id
		Integer[] ignoreID={7,8,9,10,11,51,52,60,62,78,90,99,100,119,122,127,137,141,142};
		List<Integer> ignoreIDList=new ArrayList<Integer>(Arrays.asList(ignoreID));
		
		//ignore potion meta
		Integer[] ignoreMeta={
				8225 ,8226 ,8228 ,8229 ,8233 ,8236 ,8257 ,8258 ,
				8259 ,8260 ,8262 ,8264 ,8265 ,8266 ,8269 ,8270 ,
				16417 ,16418 ,16420 ,16421 ,16425 ,16428 ,16449 ,16450 ,
				16451 ,16452 ,16454 ,16456 ,16457 ,16458 ,16461 ,16462 };
		List<Integer> ignoreMetaList=new ArrayList<Integer>(Arrays.asList(ignoreMeta));
		
		for (int id : IDList) {
			if(!ignoreIDList.contains(id)){
				Item item = Item.getItemById(id);
				List<ItemStack> subItemList = new ArrayList<ItemStack>();
				item.getSubItems(item, item.getCreativeTab(), subItemList);
				for (ItemStack itemStack : subItemList){
					if(id==373){
						if(!ignoreMetaList.contains(itemStack.getItemDamageForDisplay()))
							itemList.add(itemStack);
					}else
						itemList.add(itemStack);
				}
			}
		}

	}
	
}
