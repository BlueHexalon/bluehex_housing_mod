package com.bluehex.bh_housing.common.block;

import java.util.Random;

import com.bluehex.bh_housing.common.NPCHouse;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

public class NPCHouse_Block extends Block
{
	private final String name = "npch_block";
	private NPCHouse npch = new NPCHouse();
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public NPCHouse_Block()
	{
		super(Material.wood);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public String getName()
	{
		return name;
	}
	
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[]{FACING});
    }
	
	public void setNPCHouse(NPCHouse npch_new)
	{
		npch = npch_new;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		
		if(enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}
		
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override	
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		//drop the item into the world instead of the block
		return Item.getByNameOrId("bh_housing:npch_item");
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1;
	}
}
