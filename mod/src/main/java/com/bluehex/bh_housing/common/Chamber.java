package com.bluehex.bh_housing.common;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/*
 * Chamber is under construction.  It is not yet in use, but it will be used in an upcoming update.
 * Chamber is going to replace NPCHouse and will allow for any shape house as long as it's under
 * an upper limit of air blocks in an enclosed area.
 */

public class Chamber
{
	private byte[][][] house;
	private BlockPos doorTop;
	
	public Chamber()
	{
		house = null;
		doorTop = null;
	}
	
	public Chamber(World worldIn, BlockPos doorIn)
	{
		doorTop = doorIn;
		house = airSearch(worldIn, doorIn);
	}
	
	public void setHouse(byte[][][] inHouse)
	{
		this.house = inHouse;
	}
	
	public static byte[][][] airSearch(World worldIn, BlockPos doorIn)
	{
		byte[][][] houseMD = new byte[20][10][20];
		return null;
	}
	
	/*
	 * Direction is Up, Down, North, South, East, or West, in that order (0-5)
	 */
	public static byte[][][] shiftEntries(byte[][][] inputGrid, byte direction)
	{
		byte[][][] outputGrid = new byte[inputGrid.length][inputGrid[0].length][inputGrid[0][0].length];
		byte x=0;
		byte y=0;
		byte z=0;
		switch(direction)
		{
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z++;
				break;
			case 3:
				z--;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
		}
		for(int i=0 ; i<outputGrid.length; i++)
		{
			for(int j=0; j<outputGrid[i].length; j++)
			{
				for(int k=0; k<outputGrid[i][j].length; k++)
				{
					if((i+x == -1) || (j+y == -1) || (k+z == -1) || (i+x == outputGrid.length) || (y+j == outputGrid[i].length) || (k+z == outputGrid[i][j].length))
					{
						outputGrid[i+x][j+y][k+z] = 7;
					}
					outputGrid[i][j][k] = inputGrid[i+x][j+y][k+z];
				}
			}
		}
		return outputGrid;
	}
}
