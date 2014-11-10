package com.roripantsu.largesign.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;







import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of  Icon list
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiIconList extends GuiScreen {

	private static final int[] ButtonSize = { 18, 18 };
	private static final int columnNuber = 8;
	private static final int rowNuber = 8;
	private static List<ItemStack> itemList;
	private static Map<Integer, List<ItemStack>> itemListMap;
	
	static{
		getItemList(itemList = new ArrayList<ItemStack>());
		itemListMap = splitListPutMap(itemList, columnNuber* rowNuber);
	}
	
	protected final int lastButtonID;
	protected GuiButton nextBtn;
	protected GuiButton prevBtn;
	protected int selectedItemID = 1;
	protected int selectedItemMatadata = 0;
	private Map<Integer, List<CustomGuiButton>> buttonListMap = Maps
			.newHashMap();
	private int ID;
	private boolean isVisible;
	private int page = 1;
	private int xPosition;
	private int yPosition;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String prev=LocalizeGui.guiLocalString(guiName, "prev", new Object[0]);
	private String next=LocalizeGui.guiLocalString(guiName, "next", new Object[0]);
	//<--Localize Gui
	
	public CustomGuiIconList(Minecraft MC, FontRenderer FontRenderer,
			List<GuiButton> screenButtonList, int ID, int X, int Y) {
		
		
		this.fontRendererObj=FontRenderer;
		this.ID=ID;
		this.lastButtonID=ID + itemList.size()+1;
		this.xPosition=X;
		this.yPosition=Y;
		
		
		screenButtonList.add(this.prevBtn = new GuiButton(ID, this.xPosition,
				this.yPosition + ButtonSize[1] * 9 - 4, ButtonSize[0] * 2,
				ButtonSize[1] + 2, prev));
		screenButtonList.add(this.nextBtn = new GuiButton(ID + 1,
				this.xPosition + ButtonSize[0] * 6 + 6, this.yPosition
						+ ButtonSize[1] * 9 - 4, ButtonSize[0] * 2,
				ButtonSize[1] + 2, next));
		

		for (int j = 1; j <= itemListMap.size(); j++) {
			if (itemListMap.containsKey(j)) {
				List<ItemStack> list = itemListMap.get(j);
				List<CustomGuiButton> buttonList = new ArrayList<CustomGuiButton>();

				for (int i = 0; i < list.size(); i++) {
					int x = X + ButtonSize[0] * (i % columnNuber)
							+ (i % columnNuber);
					int y = Y + ButtonSize[1]
							* (int) Math.floor(i / (double) rowNuber)
							+ (i / rowNuber);
					buttonList.add(new CustomGuiButton(ID + 2 + i * j, x, y,
							ButtonSize[0], ButtonSize[1],
							CustomGuiButton.buttonStyle.ITEMICON, list.get(i)));
				}
				this.buttonListMap.put(j, buttonList);
				screenButtonList.addAll(buttonList);
			}
		}

		this.changePage();

	}

	@Override
	public void drawScreen(int mouseX,int mouseY,float par3) {
		if (this.isVisible)
			this.drawCenteredString(this.fontRendererObj,
					Integer.toString(this.page), this.xPosition + ButtonSize[0]
							* 4, this.yPosition + ButtonSize[1] * 9, 16777215);
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.prevBtn.visible = isVisible;
		this.nextBtn.visible = isVisible;
		this.prevBtn.enabled = isVisible;
		this.nextBtn.enabled = isVisible;
		this.page = 1;
		if (!isVisible)
			for (int i = 1; i <= this.buttonListMap.size(); i++)
				for (CustomGuiButton btn : this.buttonListMap.get(i))
					btn.visible = isVisible;
		else
			this.changePage();

		for (int i = 1; i <= this.buttonListMap.size(); i++)
			for (CustomGuiButton btn : this.buttonListMap.get(i))
				btn.enabled = isVisible;

	}

	@Override
	public void updateScreen() {

	}

	@Override
	protected void actionPerformed(GuiButton btn) {

		if (btn.id == this.ID) {
			if (this.page > 1) {
				page--;
				this.changePage();
			}
		}

		if (btn.id == this.ID + 1) {
			if (this.page < this.buttonListMap.size()) {
				page++;
				this.changePage();
			}
		}

		if (btn.id >= this.ID + 2) {
			;
			ItemStack itemStack = ((CustomGuiButton) btn).itemStack;
			this.selectedItemID = Item.getIdFromItem(itemStack.getItem());
			this.selectedItemMatadata = itemStack.getItemDamage();
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
    protected void drawHoveringText(List strList, int X, int Y, FontRenderer fontRenderer)
    {
        if (!strList.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int ContentMaxWidth = 0;
            Iterator iterator = strList.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int strWidth = fontRenderer.getStringWidth(s);

                if (strWidth > ContentMaxWidth)
                {
                	ContentMaxWidth = strWidth;
                }
            }

            int OffsetX = X - ContentMaxWidth-6;
            int OffsetY = Y - 6;
            int ContentMaxHeight = 8;

            if (strList.size() > 1)
            {
                ContentMaxHeight += 2 + (strList.size() - 1) * 10;
            }

            this.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(OffsetX - 3, OffsetY - 4, OffsetX + ContentMaxWidth + 3, OffsetY - 3, j1, j1);
            this.drawGradientRect(OffsetX - 3, OffsetY + ContentMaxHeight + 3, OffsetX + ContentMaxWidth + 3, OffsetY + ContentMaxHeight + 4, j1, j1);
            this.drawGradientRect(OffsetX - 3, OffsetY - 3, OffsetX + ContentMaxWidth + 3, OffsetY + ContentMaxHeight + 3, j1, j1);
            this.drawGradientRect(OffsetX - 4, OffsetY - 3, OffsetX - 3, OffsetY + ContentMaxHeight + 3, j1, j1);
            this.drawGradientRect(OffsetX + ContentMaxWidth + 3, OffsetY - 3, OffsetX + ContentMaxWidth + 4, OffsetY + ContentMaxHeight + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(OffsetX - 3, OffsetY - 3 + 1, OffsetX - 3 + 1, OffsetY + ContentMaxHeight + 3 - 1, k1, l1);
            this.drawGradientRect(OffsetX + ContentMaxWidth + 2, OffsetY - 3 + 1, OffsetX + ContentMaxWidth + 3, OffsetY + ContentMaxHeight + 3 - 1, k1, l1);
            this.drawGradientRect(OffsetX - 3, OffsetY - 3, OffsetX + ContentMaxWidth + 3, OffsetY - 3 + 1, k1, k1);
            this.drawGradientRect(OffsetX - 3, OffsetY + ContentMaxHeight + 2, OffsetX + ContentMaxWidth + 3, OffsetY + ContentMaxHeight + 3, l1, l1);

            for (int i = 0; i < strList.size(); ++i)
            {
                String s1 = (String)strList.get(i);
                fontRenderer.drawStringWithShadow(s1, OffsetX, OffsetY, -1);

                if (i == 0)
                {
                    OffsetY += 2;
                }

                OffsetY += 10;
            }

            this.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

	@Override
	protected void keyTyped(char par1, int par2) {

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) {

	}

	protected void renderButtonHoveringText(int X,int Y){
		CustomGuiButton btn=this.getHoverdButton();
		if(btn != null){
			String name=btn.itemStack.getDisplayName();
			int id=Item.getIdFromItem(btn.itemStack.getItem());
			int damage=btn.itemStack.getItemDamage();
			this.drawHoveringText(
				Arrays.asList(new String[] {name,"("+id+":"+damage+")"}), 
				X,Y,this.fontRendererObj);
		}
	}
	
	private void changePage() {
		
		int maxPage=this.buttonListMap.size();
		
		this.prevBtn.enabled=(this.page>1);
		this.nextBtn.enabled=(this.page<maxPage);

		for (int i = 1; i <= maxPage; i++) {
			if (i == this.page) {
				for (CustomGuiButton btn : this.buttonListMap.get(i))
					btn.visible = true;
			} else {
				for (CustomGuiButton btn : this.buttonListMap.get(i))
					btn.visible = false;
			}
		}

	}
	
	private CustomGuiButton getHoverdButton(){
		Iterator<CustomGuiButton> iterator=this.buttonListMap.get(this.page).iterator();
		while(iterator.hasNext()){
			CustomGuiButton btn=iterator.next();
			if(btn.id >= this.ID + 2 && btn.getHoverState(btn.func_146115_a())==2)
				return btn;
		}
		return null;
	}
	
	private static Map<Integer, List<ItemStack>> splitListPutMap(List<ItemStack> list,
			int subListSize) {
		Map<Integer, List<ItemStack>> map = Maps.newHashMap();
		int remainder = list.size() % subListSize;
		int maxKey = (int) Math.floor((double) list.size()
				/ (double) subListSize);

		for (int i = 1; i <= maxKey; i++)
			map.put(i, list.subList((i - 1) * subListSize, i * subListSize));

		if (remainder > 0)
			map.put(maxKey + 1,
					list.subList(maxKey * subListSize, maxKey * subListSize
							+ remainder));

		return map;

	}
	
	private static void getItemList(List<ItemStack> itemList) {
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
								
				//fix for other mod items.>>
				Method[] methods=item.getClass().getDeclaredMethods();
				int theIndex=-1;
				for(int i=0;i<methods.length;i++){
					if(methods[i].getName()=="func_150895_a"){
						theIndex=i;
						break;
					}
				}
					
				if(theIndex>=0){
					try {
						methods[theIndex].invoke(item, item,(CreativeTabs)null,subItemList);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}else{
						item.getSubItems(item, (CreativeTabs)null, subItemList);
				}
				//<<fix for other mod items.
				
				for (ItemStack itemStack : subItemList){
					if(id==373){
						if(!ignoreMetaList.contains(itemStack.getItemDamageForDisplay()))
							itemList.add(itemStack);
					}else{
						
						itemList.add(itemStack);
					}
				}
			}
		}

	}

}
