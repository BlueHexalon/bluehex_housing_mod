package com.bluehex.bh_housing.common.item;

import com.bluehex.bh_housing.common.NPCHouse;
import com.bluehex.bh_housing.common.entity.EntityNPC;
import com.bluehex.bh_housing.helpers.BlockHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class NPCHouse_Item extends Item
{
	private static final int NULL = 0;
	private final String name = "npch_item";
	
	public NPCHouse_Item()
	{
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
	}
	
	
	 /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		NPCHouse BuildInfo;
		if(BlockHelper.isWooden(worldIn,pos))
		{
			BuildInfo = BlockHelper.isBox(worldIn, pos, side);
			if(BlockHelper.isHabitable(worldIn, BuildInfo))
			{
				BlockPos sign = BlockHelper.placeBlockOverDoor(stack, playerIn, worldIn, BuildInfo, "npch_block", hitX, hitY, hitZ);
				BuildInfo.setCorner(8, sign);
				if(!BlockHelper.hasOccupant(worldIn, BuildInfo))
				{
					EntityNPC.spawnNewEntityNPC(worldIn, BuildInfo);
				}
			}
		}
		return false;
	}
}
