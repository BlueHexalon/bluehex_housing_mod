package com.bluehex.bh_housing.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class NPCHouse
{
	private BlockPos UNW, UNE, USW, USE, DNW, DNE, DSW, DSE;
	private BlockPos doorSign;
	private boolean isBuilding;
	
	public NPCHouse()
	{
		UNW = new BlockPos(0,0,0);
		UNE = new BlockPos(0,0,0);
		USW = new BlockPos(0,0,0);
		USE = new BlockPos(0,0,0);
		DNW = new BlockPos(0,0,0);
		DNE = new BlockPos(0,0,0);
		DSW = new BlockPos(0,0,0);
		DSE = new BlockPos(0,0,0);
		doorSign = new BlockPos(0,0,0);
		isBuilding = false;
	}
	
	public NPCHouse(BlockPos unw, BlockPos une, BlockPos usw, BlockPos use, BlockPos dnw, BlockPos dne, BlockPos dsw, BlockPos dse, boolean build)
	{
		UNW = unw;
		UNE = une;
		USW = usw;
		USE = use;
		DNW = dnw;
		DNE = dne;
		DSW = dsw;
		DSE = dse;
		doorSign = unw;
		isBuilding = build;
	}
	
	public boolean isEqual(NPCHouse npchIn)
	{
		if(UNW.equals(npchIn.getCorner(0)))
		{
			if(UNE.equals(npchIn.getCorner(1)))
			{
				if(USW.equals(npchIn.getCorner(2)))
				{
					if(USE.equals(npchIn.getCorner(3)))
					{
						if(DNW.equals(npchIn.getCorner(4)))
						{
							if(DNE.equals(npchIn.getCorner(5)))
							{
								if(DSW.equals(npchIn.getCorner(6)))
								{
									if(DSE.equals(npchIn.getCorner(7)))
									{
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isBuilding()
	{
		return isBuilding;
	}
	
	public BlockPos getCorner(int index)
	{
		BlockPos temp = UNW;
		switch(index)
		{
			case 0:
				temp = UNW;
				break;
			case 1:
				temp = UNE;
				break;
			case 2:
				temp = USW;
				break;
			case 3:
				temp = USE;
				break;
			case 4:
				temp = DNW;
				break;
			case 5:
				temp = DNE;
				break;
			case 6:
				temp = DSW;
				break;
			case 7:
				temp = DSE;
				break;
			case 8:
				temp = doorSign;
				break;
		}
		return temp;
	}
	
	public void setCorner(int index, BlockPos value)
	{
		switch(index)
		{
			case 0:
				UNW = value;
				break;
			case 1:
				UNE = value;
				break;
			case 2:
				USW = value;
				break;
			case 3:
				USE = value;
				break;
			case 4:
				DNW = value;
				break;
			case 5:
				DNE = value;
				break;
			case 6:
				DSW = value;
				break;
			case 7:
				DSE = value;
				break;
			case 8:
				doorSign = value;
				break;
		}
	}
	
	public static NPCHouse loadNPCHFromNBT(NBTTagCompound tag)
	{
		NPCHouse npch = new NPCHouse();
		npch.readFromNBT(tag);
		return npch;
	}

	public void readFromNBT(NBTTagCompound tag)
	{
		String a0 = "UNW";
		String a1 = "UNE";
		String a2 = "USW";
		String a3 = "USE";
		String a4 = "DNW";
		String a5 = "DNE";
		String a6 = "DSW";
		String a7 = "DSE";
		String a8 = "doorSign";
		List<String> corners = new ArrayList();
		corners.add(a0);
		corners.add(a1);
		corners.add(a2);
		corners.add(a3);
		corners.add(a4);
		corners.add(a5);
		corners.add(a6);
		corners.add(a7);
		corners.add(a8);
		String b1 = "X";
		String b2 = "Y";
		String b3 = "Z";
		List<String> coordinate = new ArrayList();
		coordinate.add(b1);
		coordinate.add(b2);
		coordinate.add(b3);
		int index = 0;
		for (String corner : corners)
		{
			double tempX = 0;
			double tempY = 0;
			double tempZ = 0;
			for (String coor : coordinate)
			{
				if ( coor == "X")
					tempX =	tag.getDouble((corner + coor));
				else if ( coor == "Y")
					tempY = tag.getDouble((corner + coor));
				else if ( coor == "Z")
					tempZ = tag.getDouble((corner + coor));
			}
			BlockPos temp = new BlockPos(tempX, tempY, tempZ);
			this.setCorner(index, temp);
			index++;
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		String a0 = "UNW";
		String a1 = "UNE";
		String a2 = "USW";
		String a3 = "USE";
		String a4 = "DNW";
		String a5 = "DNE";
		String a6 = "DSW";
		String a7 = "DSE";
		String a8 = "doorSign";
		List<String> corners = new ArrayList();
		corners.add(a0);
		corners.add(a1);
		corners.add(a2);
		corners.add(a3);
		corners.add(a4);
		corners.add(a5);
		corners.add(a6);
		corners.add(a7);
		corners.add(a8);
		String b1 = "X";
		String b2 = "Y";
		String b3 = "Z";
		List<String> coordinate = new ArrayList();
		coordinate.add(b1);
		coordinate.add(b2);
		coordinate.add(b3);
		int index = 0;
		for (String corner : corners)
		{
			BlockPos temp = this.getCorner(index);
			for (String coor : coordinate)
			{
				if ( coor == "X")
					tag.setDouble((corner + coor), temp.getX());
				else if ( coor == "Y")
					tag.setDouble((corner + coor), temp.getY());
				else if ( coor == "Z")
					tag.setDouble((corner + coor), temp.getZ());
			}
			index++;
		}
		return tag;
	}
}
