package com.bluehex.bh_housing.helpers;

import java.util.ArrayList;
import java.util.List;

import com.bluehex.bh_housing.common.NPCHouse;
import com.bluehex.bh_housing.common.block.NPCHouse_Block;
import com.bluehex.bh_housing.common.entity.EntityNPC;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * BlockHelper is a class that currently contains a bunch of static helper
 * functions for working with the World object.  Most functions in here
 * will soon either be depreciated or moved, most likely.  After that is done,
 * I may turn this into an interface instead.
 */

public class BlockHelper
{

	/*
	 * Temporary method to check if a BlockStat is of a certain material.
	 * Plan to change from IBlockState to World and BlockPos input.  That will
	 * remove the need to get an IBlockState from the world and then pass it in.
	 * 
	 * Returns true if the block for the given blockstate is of the passed in material.
	 */
	public static boolean isMaterial(IBlockState blockState, Material mat)
	{
		Block posBlock = blockState.getBlock();
		if(posBlock.getMaterial() == mat)
			return true;
		else
			return false;
	}
	
	/*
	 * Temporary method for checking if the block at the location was made of wood.
	 * I don't see much need for it in the future and expect depreciation soon.
	 * 
	 * Returns true if block at given blockposition is made of wood.
	 */
	public static boolean isWooden(World worldIn, BlockPos pos)
	{
		return BlockHelper.isMaterial(worldIn.getBlockState(pos), Material.wood);
	}
	
	/*
	 * Returns true if the block at BlockPos 'pos' is the same type of block as blocktype.
	 * 
	 * Example:  if the block at BlockPos pos in World worldIn is an Oak Door
	 * 			 then isBlockType(worldIn, pos, Blocks.oak_door) will return true. 
	 */
	public static boolean isBlockType(World worldIn, BlockPos pos, Block blockType)
	{
		Block temp = worldIn.getBlockState(pos).getBlock();
		return (temp == blockType);
	}
	
	/*
	 * First write of a function to place a block. Plan to rewrite soon now that I have more
	 * experience with the Minecraft code and Java.
	 * 
	 * Attaches a block from my mod by the name of "block_name" to the block at pos in the direction of side.
	 */
	public static void placeBlock(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, String block_name)
	{			
		String placeMe = ("bh_housing:" + block_name);
		int direction = side.getIndex(); //D-U-N-S-W-E
		switch (direction)
		{
			case 0:	worldIn.setBlockState(pos.down(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
			case 1:	worldIn.setBlockState(pos.up(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
			case 2:	worldIn.setBlockState(pos.north(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
			case 3:	worldIn.setBlockState(pos.south(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
			case 4:	worldIn.setBlockState(pos.west(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
			case 5:	worldIn.setBlockState(pos.east(1), Block.getBlockFromName(placeMe).getDefaultState());
					break;
		}
		--stack.stackSize;
		if(stack.stackSize == 0)
		{
			playerIn.destroyCurrentEquippedItem();
		}
	}
	
	/*
	 * Returns the location of vanilla door within the bounds of NPCHouse npch.
	 */
	public static BlockPos findDoor(World worldIn, NPCHouse npch)
	{
		Block a = Blocks.acacia_door;
		Block b = Blocks.birch_door;
		Block c = Blocks.dark_oak_door;
		Block d = Blocks.iron_door;
		Block e = Blocks.jungle_door;
		Block f = Blocks.oak_door;
		Block g = Blocks.spruce_door;
		List<Block> doors = new ArrayList();
		doors.add(a);
		doors.add(b);
		doors.add(c);
		doors.add(d);
		doors.add(e);
		doors.add(f);
		doors.add(g);
		BlockPos door = npch.getCorner(0);
		for (Block temp : doors)
		{
			door = BlockHelper.findInBox(worldIn, npch, temp);
			if(!door.equals(npch.getCorner(0)))
			{
				return door;
			}
		}
		return door;
	}
	
	
	/*
	 * This messy function is a work in progress.  It is a temporary setup for
	 * placing a custom block from my mod over the door of the given npch.
	 * 
	 * Returns the location of where the sign should be placed.
	 * 
	 * Needs to be rewritten badly.
	 */
	public static BlockPos placeBlockOverDoor(ItemStack stack, EntityPlayer playerIn, World worldIn, NPCHouse npch, String block_name, float hitX, float hitY, float hitZ)
	{
		String placeMe = ("bh_housing:" + block_name);
		BlockPos door = BlockHelper.findDoor(worldIn, npch);
		if (!door.equals(npch.getCorner(0)))
		{
			int north = npch.getCorner(0).getZ();
			int south = npch.getCorner(2).getZ();
			int west = npch.getCorner(0).getX();
			int east = npch.getCorner(1).getX();
			BlockPos placeHere = door;
			boolean shouldUse = false;
			EnumFacing direction = EnumFacing.NORTH;
			if(worldIn.getBlockState(placeHere).getBlock() instanceof NPCHouse_Block)
			{
				return placeHere;
			}
			else if(door.getX() == west)
			{
				placeHere = door.up().west();
				direction = EnumFacing.WEST;
			}
			else if(door.getX() == east)
			{
				placeHere = door.up().east();
				direction = EnumFacing.EAST;
			}
			else if(door.getZ() == north)
			{
				placeHere = door.up().north();
				direction = EnumFacing.NORTH;
			}
			else if(door.getZ() == south)
			{
				placeHere = door.up().south();
				direction = EnumFacing.SOUTH;
			}
			shouldUse = worldIn.setBlockState(placeHere, NPCHouse_Block.getBlockFromName(placeMe).getDefaultState().withProperty(NPCHouse_Block.FACING, direction), 3);
			if ( shouldUse )
			{
				--stack.stackSize;
				if(stack.stackSize == 0)
				{
					playerIn.destroyCurrentEquippedItem();
				}
				if(block_name == "npch_block")
				{
					NPCHouse_Block npchblock = (NPCHouse_Block) worldIn.getBlockState(placeHere).getBlock();
					npchblock.setNPCHouse(npch);
				}
				IBlockState temp = worldIn.getBlockState(placeHere);
				if (temp.getBlock().getRegistryName() == "npch_block")
				{
					ItemBlock.setTileEntityNBT(worldIn, playerIn, placeHere, new ItemStack(Block.getBlockFromName("npch_block"), 1));
				}
			}
			return placeHere;	
		}
		return null;
	}

	/*
	 * Returns true if a 2x2 or larger rectangle in the X-Z plane is a solid wooden rectangle
	 */
	public static boolean isSolidPlatform(World worldIn, BlockPos ne, BlockPos nw, BlockPos se, BlockPos sw)
	{
		if(nw.equals(se))
		{
			return false;
		}
		int far_east = ne.getX();
		int far_north = ne.getZ();
		int far_west = sw.getX();
		int far_south = sw.getZ();
		for(int i=far_north; i<=far_south; i++)
		{
			for(int j=far_west; j<=far_east; j++)
			{
				BlockPos temp = new BlockPos(j, ne.getY(), i);
				if(!(BlockHelper.isWooden(worldIn, temp)))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Returns true if a 2x2 or larger rectangle in the Y-Z plane is a solid wooden rectangle
	 */
	public static boolean isSolidNSWall(World worldIn, BlockPos un, BlockPos us, BlockPos dn, BlockPos ds)
	{
		if(un.equals(ds))
		{
			return false;
		}
		int far_up = un.getY();
		int far_north = un.getZ();
		int far_down = ds.getY();
		int far_south = ds.getZ();
		for(int i=far_north; i<=far_south; i++)
		{
			for(int j=far_down; j<=far_up; j++)
			{
				BlockPos temp = new BlockPos(un.getX(), j, i);
				if(!(BlockHelper.isWooden(worldIn, temp)))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Returns true if a 2x2 or larger rectangle in the X-Y plane is a solid wooden rectangle
	 */
	public static boolean isSolidEWWall(World worldIn, BlockPos ue, BlockPos uw, BlockPos de, BlockPos dw)
	{
		if(ue.equals(dw))
		{
			return false;
		}
		int far_up = ue.getY();
		int far_east = ue.getX();
		int far_down = dw.getY();
		int far_west = dw.getX();
		for(int i=far_west; i<=far_east; i++)
		{
			for(int j=far_down; j<=far_up; j++)
			{
				BlockPos temp = new BlockPos(i, j, ue.getZ());
				if(!(BlockHelper.isWooden(worldIn, temp)))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Needs to be rewritten to return null instead of UNW corner of npch.
	 * 
	 * Returns the Upper NorthWestern most BlockPos in the worldIn of a block inside of the NPCHouse npch
	 */
	public static BlockPos findInBox(World worldIn, NPCHouse npch, Block block)
	{
		BlockPos pos = npch.getCorner(0);
		int far_east = npch.getCorner(1).getX();
		int far_west = npch.getCorner(0).getX();
		int far_up = npch.getCorner(0).getY();
		int far_down = npch.getCorner(4).getY();
		int far_north = npch.getCorner(0).getZ();
		int far_south = npch.getCorner(2).getZ();
		for(int i=far_north; i<=far_south; i++)
		{
			for(int j=far_west; j<=far_east; j++)
			{
				for(int k=far_down; k<=far_up; k++)
				{
					BlockPos temp = new BlockPos(j, k, i);
					//isn't catching the if statement
					if(BlockHelper.isBlockType(worldIn, temp, block))
					{
						pos = temp;
					}
				}
			}
		}
		return pos;
	}

	/*
	 * Returns true if the given block can be found inside the NPCHouse npch.
	 */
	public static boolean isInBox(World worldIn, NPCHouse npch, Block block)
	{
		BlockPos pos = BlockHelper.findInBox(worldIn, npch, block);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int a = npch.getCorner(0).getX();
		int b = npch.getCorner(0).getY();
		int c = npch.getCorner(0).getZ();
		if((a == x) && (b == y) && (c == z))
		{
			return false;
		}
		return true;
	}

	/*
	 * This function is used to determine if the player clicked on the inside of a solid wooden box.
	 * 
	 * Returns NPCHouse of what you clicked on, whether it's a real building or not. The returned
	 * NPCHouse has a boolean to indicate if it is a valid house or not.
	 */
	public static NPCHouse isBox(World worldIn, BlockPos pos, EnumFacing side)
	{
		BlockPos une = pos, unw = pos, use = pos, usw = pos, dne = pos, dnw = pos, dse = pos, dsw = pos;
		boolean isBuilding = false;
		
		//D-U-N-S-W-E
		assess:
		switch(side.getIndex())
		{
			case 0:	//assume ceiling
					//while not wall && wood, go in direction to find wall
					//know is ceiling, so we can us a block at least 2 below  && can only click ceiling from inside, so one in any Cardinal direction can be assumed as wood for wall checks
					//move east and west, checking if wall with 2 blocks down and 1 block north  (neCorner
					une = pos.north().east();
					unw = pos.north().west();
					use = pos.south().east();
					usw = pos.south().west();
					//make sure all start corners are actually wood, else fail
					if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw)))
					{
						isBuilding = false;
						break assess;
					}
					//these are clearly not wooden, but we need them to check for walls, when they become wooden, we have found walls
					dne = une.down();
					dnw = unw.down();
					dse = use.down();
					dsw = usw.down();
					if((BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					//move corners to find walls in order, north, south, east, west
					while(!BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw))
					{
						//if we're no longer looking at wood, then it's not a domicile, so fail
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.north();
						unw = unw.north();
						dne = dne.north();
						dnw = dnw.north();
					}
					while(!BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw))
					{
						//if we're no longer looking at wood, then it's not a domicile, so fail
						if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw)))
						{
							isBuilding = false;
							break assess;
						}
						use = use.south();
						usw = usw.south();
						dse = dse.south();
						dsw = dsw.south();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse))
					{
						//if we're no longer looking at wood, then it's not a domicile, so fail
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, use)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.east();
						use = use.east();
						dne = dne.east();
						dse = dse.east();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw))
					{
						//if we're no longer looking at wood, then it's not a domicile, so fail
						if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, usw)))
						{
							isBuilding = false;
							break assess;
						}
						unw = unw.west();
						usw = usw.west();
						dnw = dnw.west();
						dsw = dsw.west();
					}
					//We now have the real upper corners of the building, let's find out if we have a floor!  :)
					while(!BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
						{
							//for some reason it's going past all of them and I've no fucking clue why.
							
							isBuilding = false;
							break assess;
						}
						dne = dne.down();
						dnw = dnw.down();
						dse = dse.down();
						dsw = dsw.down();
					}
					//This should be all of our corners.  Let's run another check on every wall and floor just to make sure its' not missing pieces on us.  :D
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;
					break assess;
			case 1:	//assume floor	
					dne = pos.north().east();
					dnw = pos.north().west();
					dse = pos.south().east();
					dsw = pos.south().west();
					if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					une = dne.up();
					unw = dnw.up();
					use = dse.up();
					usw = dsw.up();
					if((BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw)))
					{
						isBuilding = false;
						break assess;
					}
					//Start finding Walls in order: N-S-E-W
					while(!BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw))
					{
						if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.north();
						unw = unw.north();
						dne = dne.north();
						dnw = dnw.north();
					}
					while(!BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						use = use.south();
						usw = usw.south();
						dse = dse.south();
						dsw = dsw.south();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse))
					{
						if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dse)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.east();
						use = use.east();
						dne = dne.east();
						dse = dse.east();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						unw = unw.west();
						usw = usw.west();
						dnw = dnw.west();
						dsw = dsw.west();
					}
					//Find the Roof!
					while(!BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.up();
						unw = unw.up();
						use = use.up();
						usw = usw.up();
					}
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;
					break assess;
					
			case 2: //assume ewWall (North facing side)
					use = pos.up().east();
					usw = pos.up().west();
					dse = pos.down().east();
					dsw = pos.down().west();
					if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					une = use.north();
					unw = usw.north();
					dne = dse.north();
					dnw = dsw.north();
					if((BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw)))
					{
						isBuilding = false;
						break assess;
					}
					//First, we check Ceiling and Floor
					while(!BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw))
					{
						if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.up();
						unw = unw.up();
						use = use.up();
						usw = usw.up();
					}
					while(!BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						dne = dne.down();
						dnw = dnw.down();
						dse = dse.down();
						dsw = dsw.down();
					}
					//Now we check the Eastern and Western Walls
					while(!BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse))
					{
						if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, dse)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.east();
						use = use.east();
						dne = dne.east();
						dse = dse.east();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						unw = unw.west();
						usw = usw.west();
						dnw = dnw.west();
						dsw = dsw.west();
					}
					//Finally, we check for the North Wall
					while(!BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.north();
						unw = unw.north();
						dne = dne.north();
						dnw = dnw.north();
					}
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;	
					break assess;
			case 3:	//assume ewWall (South facing Wall - North wall of room)
					une = pos.up().east();
					unw = pos.up().west();
					dne = pos.down().east();
					dnw = pos.down().west();
					if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw)))
					{
						isBuilding = false;
						break assess;
					}
					use = une.south();
					usw = unw.south();
					dse = dne.south();
					dsw = dnw.south();
					if((BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					//First, we check Ceiling and Floor
					while(!BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, unw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.up();
						unw = unw.up();
						use = use.up();
						usw = usw.up();
					}
					while(!BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dnw)))
						{
							isBuilding = false;
							break assess;
						}
						dne = dne.down();
						dnw = dnw.down();
						dse = dse.down();
						dsw = dsw.down();
					}
					//Now we check the Eastern and Western Walls
					while(!BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, dne)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.east();
						use = use.east();
						dne = dne.east();
						dse = dse.east();
					}
					while(!BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, dnw)))
						{
							isBuilding = false;
							break assess;
						}
						unw = unw.west();
						usw = usw.west();
						dnw = dnw.west();
						dsw = dsw.west();
					}
					//Finally, we check for the South Wall
					while(!BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dse) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						use = use.south();
						usw = usw.south();
						dse = dse.south();
						dsw = dsw.south();
					}
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;	
					break assess;
			case 4:	//assume nsWall (West Facing Wall - Eastern wall of room)
					une = pos.up().north();
					use = pos.up().south();
					dne = pos.down().north();
					dse = pos.down().south();
					if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dse)))
					{
						isBuilding = false;
						break assess;
					}
					unw = une.west();
					usw = use.west();
					dnw = dne.west();
					dsw = dse.west();
					if((BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					//First we check the Ceiling and Floor
					//First, we check Ceiling and Floor
					while(!BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, use)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.up();
						unw = unw.up();
						use = use.up();
						usw = usw.up();
					}
					while(!BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dse)))
						{
							isBuilding = false;
							break assess;
						}
						dne = dne.down();
						dnw = dnw.down();
						dse = dse.down();
						dsw = dsw.down();
					}
					//Next we are gonna check the North and South Walls
					while(!BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, dne)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.north();
						unw = unw.north();
						dne = dne.north();
						dnw = dnw.north();
					}
					while(!BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, dse)))
						{
							isBuilding = false;
							break assess;
						}
						use = use.south();
						usw = usw.south();
						dse = dse.south();
						dsw = dsw.south();
					}
					//Finally, we move West!
					while(!BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
							
						}
						unw = unw.west();
						usw = usw.west();
						dnw = dnw.west();
						dsw = dsw.west();
					}
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;
					break assess;
			case 5:	//assume nsWall (East Facing Wall - Western wall of room)
					unw = pos.up().north();
					usw = pos.up().south();
					dnw = pos.down().north();
					dsw = pos.down().south();
					if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dsw)))
					{
						isBuilding = false;
						break assess;
					}
					une = unw.east();
					use = usw.east();
					dne = dnw.east();
					dse = dsw.east();
					if((BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dse)))
					{
						isBuilding = false;
						break assess;
					}
					//First we check the Ceiling and Floor
					//First, we check Ceiling and Floor
					while(!BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw))
					{
						if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, usw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.up();
						unw = unw.up();
						use = use.up();
						usw = usw.up();
					}
					while(!BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, dnw) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						dne = dne.down();
						dnw = dnw.down();
						dse = dse.down();
						dsw = dsw.down();
					}
					//Next we are gonna check the North and South Walls
					while(!BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw))
					{
						if(!(BlockHelper.isWooden(worldIn, unw) && BlockHelper.isWooden(worldIn, dnw)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.north();
						unw = unw.north();
						dne = dne.north();
						dnw = dnw.north();
					}
					while(!BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw))
					{
						if(!(BlockHelper.isWooden(worldIn, usw) && BlockHelper.isWooden(worldIn, dsw)))
						{
							isBuilding = false;
							break assess;
						}
						use = use.south();
						usw = usw.south();
						dse = dse.south();
						dsw = dsw.south();
					}
					//Finally, we move East!
					while(!BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse))
					{
						if(!(BlockHelper.isWooden(worldIn, une) && BlockHelper.isWooden(worldIn, use) && BlockHelper.isWooden(worldIn, dne) && BlockHelper.isWooden(worldIn, dse)))
						{
							isBuilding = false;
							break assess;
						}
						une = une.east();
						use = use.east();
						dne = dne.east();
						dse = dse.east();
					}
					if((BlockHelper.isSolidPlatform(worldIn, une, unw, use, usw) && BlockHelper.isSolidPlatform(worldIn, dne, dnw, dse, dsw) && BlockHelper.isSolidEWWall(worldIn, une, unw, dne, dnw) &&
							BlockHelper.isSolidEWWall(worldIn, use, usw, dse, dsw) && BlockHelper.isSolidNSWall(worldIn, une, use, dne, dse) && BlockHelper.isSolidNSWall(worldIn, unw, usw, dnw, dsw)))
					{
						isBuilding = true;
						break assess;
					}
					isBuilding = false;
					break assess;
		}
		NPCHouse house =new NPCHouse(unw, une, usw, use, dnw, dne, dsw, dse, isBuilding);
		return house;
	}
	
	/*
	 * Returns true if a the given pos is inside the given npch.
	 */
	public static boolean isInside(World worldIn, BlockPos pos, NPCHouse npch)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int far_east = npch.getCorner(1).getX();
		int far_west = npch.getCorner(0).getX();
		int far_up = npch.getCorner(0).getY();
		int far_down = npch.getCorner(4).getY();
		int far_north = npch.getCorner(0).getZ();
		int far_south = npch.getCorner(2).getZ();
		if ((x <= far_east) && (x >= far_west))
		{
			if ((y <= far_up) && (y >= far_down))
			{
				if ((z <= far_south) && (z >= far_north))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Returns true if the given npch already has an occupant.
	 */
	public static boolean hasOccupant(World worldIn, NPCHouse npch)
	{
		List<Entity> inHouse = new ArrayList();
		List<Entity> allEntities = worldIn.loadedEntityList;
		for (Entity ent : allEntities)
		{
			if (BlockHelper.isInside(worldIn, ent.getPosition(), npch))
			{
				inHouse.add(ent);
			}
		}
		for (Entity inside : inHouse)
		{
			if ( inside instanceof EntityNPC)
			{
				EntityNPC temp = (EntityNPC) inside;
				if ( npch.isEqual(temp.getNPCHouse()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Runs a set of tests to make sure that the given npch is able to be used for housing
	 * for an NPC.
	 * 
	 * Returns true if given npch meets requirements for living quarters.
	 */
	public static boolean isHabitable(World worldIn, NPCHouse npch)
	{
		if(npch.isBuilding())
		{
			if(BlockHelper.isInBox(worldIn, npch, Blocks.bed))
			{
				if(BlockHelper.isInBox(worldIn, npch, Blocks.chest))
				{
					if(BlockHelper.isInBox(worldIn, npch, Blocks.crafting_table))
					{
						if(BlockHelper.isInBox(worldIn, npch, Blocks.torch))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
