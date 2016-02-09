package com.bluehex.bh_housing.common.init;

import com.bluehex.bh_housing.common.item.NPCHouse_Item;
import com.bluehex.bh_housing.helpers.RegisterHelper;

import net.minecraft.item.Item;

/*
 * Class called in the proxy and used for registering any
 * mod specific items.
 */
public class BHH_Items {

	public static Item npch_item = new NPCHouse_Item();
	
	public static void registerItems()
	{
		RegisterHelper.registerItem(npch_item);
	}
	
	public static void registerRenderItems()
	{
		RegisterHelper.registerRenderItem(npch_item);
	}
	
}
