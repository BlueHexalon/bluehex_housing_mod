package com.bluehex.bh_housing.helpers;

import com.bluehex.bh_housing.BH_HousingMod;
import com.bluehex.bh_housing.BH_ModInfo;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/*
 * This class we designed to handle registration calls to
 * the main Minecraft instance/GameRegistry.  I plan to break
 * this into two different files soon, one for registering with 
 * GameRegistry, one for registering renders with Minecraft and
 * the RenderingRegistry.
 */
public class RegisterHelper 
{
	public static void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, block.getRegistryName());
	}
	
	public static void registerItem(Item item)
	{
		GameRegistry.registerItem(item, item.getRegistryName());
	}
	
	public static void registerEntity(int id, Class classIn, String name, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(classIn, name, id, BH_HousingMod.instance, 80, 3, false);
		EntityRegistry.registerEgg(classIn, eggPrimary, eggSecondary);
	}
	
	public static void registerRenderBlock(Block block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(BH_ModInfo.MOD_ID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
	
	public static void registerRenderItem(Item item)
	{		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(BH_ModInfo.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
	/*
	 * This function will be brought in to use once I have the new
	 * IRenderFactory method working properly.  Until then, it's comments.
	 */
	
	/*public static void registerRenderNPC(Class classIn, IRenderFactory renderFactory)
	*{
	*	RenderingRegistry.registerEntityRenderingHandler(classIn, renderFactory);
	*}
	*/
}
