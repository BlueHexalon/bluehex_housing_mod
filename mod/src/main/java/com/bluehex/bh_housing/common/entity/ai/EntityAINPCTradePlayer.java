package com.bluehex.bh_housing.common.entity.ai;

import com.bluehex.bh_housing.common.entity.EntityNPC;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAINPCTradePlayer extends EntityAIBase
{
	private EntityNPC npc;
	
	public EntityAINPCTradePlayer(EntityNPC npcIn)
	{
		this.npc = npcIn;
		this.setMutexBits(8);
	}
	
	public boolean shouldExecute()
	{
		if (!this.npc.isEntityAlive())
		{
			return false;
		}
		else if (this.npc.isInWater())
		{
			return false;
		}
		else if (!this.npc.onGround)
		{
			return false;
		}
		else if (this.npc.velocityChanged)
		{
			return false;
		}
		else
		{
			EntityPlayer entityplayer = this.npc.getCustomer();
			return entityplayer == null ? false : (this.npc.getDistanceSqToEntity(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container);
		}
	}
	
	public void startExecuting()
	{
		this.npc.getNavigator().clearPathEntity();
	}
	
	public void resetTask()
	{
		this.npc.setCustomer((EntityPlayer)null);
	}
	
}
