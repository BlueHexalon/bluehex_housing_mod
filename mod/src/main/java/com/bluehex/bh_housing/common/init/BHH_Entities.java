package com.bluehex.bh_housing.common.init;

import com.bluehex.bh_housing.common.entity.EntityNPC;
import com.bluehex.bh_housing.helpers.RegisterHelper;

/*
 * Class called in the proxy and used for registering anything
 * Entity related.
 * 
 * Commented function is part of a move to a new method where
 * I can use my register helper and call my render registration here
 * instead.  Once it's working, my code will be a lot cleaner and all
 * my calls to register entites will be in this file.
 */

public class BHH_Entities
{		
	static Class npc = EntityNPC.class;

	public static void registerEntities()
	{
		RegisterHelper.registerEntity(0, npc, "EntityNPC", 0x3F5505, 0x4E6414);
	}
	
	/*@SideOnly(Side.CLIENT)
	*public static void registerRenderEntities()
	*{
	*	RegisterHelper.registerRenderNPC(npc, RenderNPC.FACTORY);
	*}
	*/
}
