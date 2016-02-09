package com.bluehex.bh_housing.common.init;

import com.bluehex.bh_housing.common.block.NPCHouse_Block;
import com.bluehex.bh_housing.helpers.RegisterHelper;

import net.minecraft.block.Block;

/*
 * Class called in the proxy and used for registering any
 * mod specific blocks.
 */
public class BHH_Blocks 
{
	public static Block npch_block = new NPCHouse_Block();
	
	public static void registerBlocks()
	{
		RegisterHelper.registerBlock(npch_block);
	}
	
	public static void registerRenderBlocks()
	{
		RegisterHelper.registerRenderBlock(npch_block);
	}
}
