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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.Maps;
import com.roripantsu.guilib.CustomGuiButton;
import com.roripantsu.guilib.GuiI18n;
import com.roripantsu.guilib.GuiMainScreen;
import com.roripantsu.guilib.GuiSubScreen;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Sub Gui Screen of Large Sign Editor
 *Gui Screen of  Icon list
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiIconList extends GuiSubScreen {
	

	private GuiButton prevBtn;
	private GuiButton nextBtn;
	private static final int[] ButtonSize = { 18, 18 };
	private static final int columnNuber = 8;
	private static final int rowNuber = 8;
	private static List<ItemStack> itemList;
	private static Map<Integer, List<ItemStack>> itemListMap;
	
	static{
		getItemList(itemList = new ArrayList<ItemStack>());
		itemListMap = splitListPutMap(itemList, columnNuber* rowNuber);
	}
	
	private Map<Integer, List<CustomGuiButton>> buttonListMap = Maps.newHashMap();
	private int page = 1;
	int selectedItemID = 1;
	int selectedItemMatadata = 0;
	ItemStack selectedItemStack;
	
	//Localize Gui-->
	private String guiName=this.getClass().getSimpleName();
	private String prev=GuiI18n.localize(guiName, "prev", new Object[0]);
	private String next=GuiI18n.localize(guiName, "next", new Object[0]);
	//<--Localize Gui
	

	public CustomGuiIconList(int ID,Minecraft MC,FontRenderer font,GuiMainScreen parent,
			int x, int y,int width,int height) {
		super(ID, MC, font, parent, x, y, width, height);
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {	
		if (this.isVisible)
			this.drawCenteredString(this.fontRendererObj,
					Integer.toString(this.page), this.GroupX+this.width/2, 
					this.GroupY+fixH(this.gridHeight*54), 16777215);
	}

	/**
	 *Draw hovering text when when mouse is over specific place with custom font renderer.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void drawHoveringText(List list, int mouseX, int mouseY,
			FontRenderer font) {

        if (!list.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int ContentMaxWidth = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int strWidth = font.getStringWidth(s);

                if (strWidth > ContentMaxWidth)
                {
                	ContentMaxWidth = strWidth;
                }
            }

            int OffsetX = mouseX - ContentMaxWidth-6;
            int OffsetY = mouseY - 6;
            int ContentMaxHeight = 8;

            if (list.size() > 1)
            {
                ContentMaxHeight += 2 + (list.size() - 1) * 10;
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

            for (int i = 0; i < list.size(); ++i)
            {
                String s1 = (String)list.get(i);
                font.drawStringWithShadow(s1, OffsetX, OffsetY, -1);

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

	/**
	 *Define action performed of the button.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {

		if (button.id == this.prevBtn.id) {
			if (this.page > 1) {
				page--;
				this.changePage();
			}
		}

		if (button.id == this.nextBtn.id) {
			if (this.page < this.buttonListMap.size()) {
				page++;
				this.changePage();
			}
		}

		if (button.id >= this.nextBtn.id+1) {
			this.selectedItemStack=((CustomGuiButton) button).getItemStack();
			NBTTagCompound nbt= this.selectedItemStack.writeToNBT(new NBTTagCompound());
			this.selectedItemID = nbt.getInteger("id");
			this.selectedItemMatadata = nbt.getInteger("Damage");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(this.prevBtn = 
				new GuiButton(id, this.GroupX,this.GroupY + fixH(this.gridHeight*52), fixW(this.gridWidth*5),20, prev));
		this.buttonList.add(this.nextBtn = 
				new GuiButton(id + 1,this.GroupX + fixW(this.gridWidth*20),this.GroupY + fixH(this.gridHeight*52), fixW(this.gridWidth*5),20, next));
		
		for (int j = 1; j <= itemListMap.size(); j++) {
			if (itemListMap.containsKey(j)) {
				List<ItemStack> list = itemListMap.get(j);
				List<CustomGuiButton> buttonList = new ArrayList<CustomGuiButton>();

				for (int i = 0; i < list.size(); i++) {
					int x = GroupX + fixW(ButtonSize[0]) * (i % columnNuber)
							+ (i % columnNuber);
					int y = GroupY + fixH(ButtonSize[1])
							* (int) Math.floor(i / (double) rowNuber)
							+ (i / rowNuber);
					buttonList.add(new CustomGuiButton(id + 2 + i * j, x, y,
							fixW(ButtonSize[0]), fixH(ButtonSize[1]),list.get(i)));
				}
				this.buttonListMap.put(j, buttonList);
				this.buttonList.addAll(buttonList);
			}
		}
		
		this.parentButtonList.addAll(this.buttonList);
		this.changePage();
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
	
	protected void renderButtonHoveringText(int X,int Y){
		CustomGuiButton btn=this.getHoverdButton();
		if(btn != null){
			String name=btn.getItemStack().getDisplayName();
			int id=Item.getIdFromItem(btn.getItemStack().getItem());
			int damage=btn.getItemStack().getItemDamage();
			this.drawHoveringText(
				Arrays.asList(new String[] {name,"("+id+":"+damage+")"}), 
				X,Y,this.fontRendererObj);
		}
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
			if(btn.id >= this.nextBtn.id+1 && btn.getHoverState(btn.func_146115_a())==2)
				return btn;
		}
		return null;
	}
}

