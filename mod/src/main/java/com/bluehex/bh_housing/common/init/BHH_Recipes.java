package com.bluehex.bh_housing.common.init;


import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/*
 * Class called in the proxy and used for registering any
 * mod specific recipes.
 */
public class BHH_Recipes
{
	public static void registerRecipes()
	{
		Item npch = Item.getByNameOrId("bh_housing:npch_item");
		GameRegistry.addShapedRecipe(new ItemStack(npch), 
				"SSS",
				"XXS",
				"XXS",
				'S', Items.stick, 'X', Items.sign);
	}
}
