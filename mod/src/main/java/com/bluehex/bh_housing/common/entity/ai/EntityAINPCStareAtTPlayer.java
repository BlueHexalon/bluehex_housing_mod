package com.bluehex.bh_housing.common.entity.ai;

import com.bluehex.bh_housing.common.entity.EntityNPC;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAINPCStareAtTPlayer extends EntityAIWatchClosest
{
	public final EntityNPC npc;
	
	public EntityAINPCStareAtTPlayer(EntityNPC npcIn)
	{
		super(npcIn, EntityPlayer.class, 8.0F);
		this.npc = npcIn;
	}
	
	public boolean shouldExecute()
	{
		if (this.npc.isTrading())
		{
			this.closestEntity = this.npc.getCustomer();
			return true;
		}
		else
		{
			return false;
		}
	}
}
