package com.bluehex.bh_housing.proxy;

import com.bluehex.bh_housing.common.init.BHH_Blocks;
import com.bluehex.bh_housing.common.init.BHH_Entities;
import com.bluehex.bh_housing.common.init.BHH_Items;
import com.bluehex.bh_housing.common.init.BHH_Recipes;

/*
 * CommonProxy runs on the server side and calls other files which contain
 * registration calls.  For example:  BHH_Blocks.java contains
 * all registration calls for any and all blocks for the mod.
 */

public class CommonProxy 
{
	public void preInit()
	{
		BHH_Blocks.registerBlocks();
		BHH_Entities.registerEntities();
		BHH_Items.registerItems();
		BHH_Recipes.registerRecipes();
	}
	
	public void init()
	{
		
	}
	
	public void postInit()
	{
		
	}
}
