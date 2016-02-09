package com.bluehex.bh_housing.common.entity.ai;

import com.bluehex.bh_housing.common.NPCHouse;
import com.bluehex.bh_housing.common.block.NPCHouse_Block;
import com.bluehex.bh_housing.common.entity.EntityNPC;
import com.bluehex.bh_housing.helpers.BlockHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAINPCMoveIndoors extends EntityAIBase
{
	private EntityNPC npc;
	private NPCHouse npch;
	
	public EntityAINPCMoveIndoors(EntityNPC npcIn)
	{
		this.npc = npcIn;
		this.npch = npc.getNPCHouse();
		this.setMutexBits(8);
	}
	
	public boolean shouldExecute()
	{
		//get the top, bottom, north, south, east, and west sides of the house.
		double top = npch.getCorner(0).getY();
		double bot = npch.getCorner(4).getY();
		double east = npch.getCorner(1).getX();
		double west = npch.getCorner(0).getX();
		double north = npch.getCorner(0).getZ();
		double south = npch.getCorner(2).getZ();
		
		//now check to see if the current location is outside those bounds.
		if ((npc.posY < bot) || (npc.posY > top))
		{
			return true;
		}
		if ((npc.posZ < north) || (npc.posZ > south))
		{
			return true;
		}
		if ((npc.posX < west) || (npc.posZ > east))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean continueExecuting()
	{
		return !this.npc.getNavigator().noPath();
	}
	
	public void startExecuting()
	{
		// find point inside of door
		NPCHouse npchIn = this.npc.getNPCHouse();
		World worldIn = this.npc.worldObj;
		BlockPos door = BlockHelper.findDoor(worldIn, npchIn);
		BlockPos destination = door;
		if (door.equals(npchIn.getCorner(0)))
		{
			//find nearest NPCHouse and set it.
			//this.npc.setNPCHouse(newNpch);
			return;
		}
		if (!door.equals(npchIn.getCorner(0)))
		{
			//which wall is it on so we know which way is in
			int north = npchIn.getCorner(0).getZ();
			int south = npchIn.getCorner(2).getZ();
			int west = npchIn.getCorner(0).getX();
			int east = npchIn.getCorner(1).getX();
			if(door.getX() == west)
			{
				destination = door.down().east();
			}
			else if(door.getX() == east)
			{
				destination = door.down().west();
			}
			else if(door.getZ() == north)
			{
				destination = door.down().south();
			}
			else if(door.getZ() == south)
			{
				destination = door.down().north();
			}
		}
		//destination should now be inside the door by one block
        int i = destination.getX();
        int j = destination.getY();
        int k = destination.getZ();

        if (this.npc.getDistanceSq(destination) > 256.0D)
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.npc, 14, 3, new Vec3((double)i + 0.5D, (double)j, (double)k + 0.5D));

            if (vec3 != null)
            {
                this.npc.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
            }
        }
        else
        {
            this.npc.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
        }
	}
	
	public void resetTask()
	{
	}
}
