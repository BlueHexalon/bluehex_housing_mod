package com.bluehex.bh_housing.proxy;

import com.bluehex.bh_housing.client.model.ModelNPC;
import com.bluehex.bh_housing.client.renderer.RenderNPC;
import com.bluehex.bh_housing.common.entity.EntityNPC;
import com.bluehex.bh_housing.common.init.BHH_Blocks;
import com.bluehex.bh_housing.common.init.BHH_Items;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

/*
 * ClientProxy is only run on the client side, never the server side
 * any and all render registers need to be done here because the
 * server doesn't do any rendering, which makes my dedicated servers crash.
 * 
 * Currently using a depreciated method for rendering my custom Entity
 * while I'm working to move to a method using IFactoryRenderer or something
 * along those lines.  Once that's running, registering entity renders will be
 * done in BHH_Entities.java
 */

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		super.init();
		BHH_Blocks.registerRenderBlocks();
		BHH_Items.registerRenderItems();
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC(new ModelNPC(), 0.5F));
	}
}
