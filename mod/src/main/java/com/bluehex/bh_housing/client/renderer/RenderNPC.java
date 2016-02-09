package com.bluehex.bh_housing.client.renderer;

import com.bluehex.bh_housing.BH_ModInfo;
import com.bluehex.bh_housing.client.model.ModelNPC;
import com.bluehex.bh_housing.common.entity.EntityNPC;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/*
 * Every custom Model requires a custom Render class, as best I can tell.
 * 
 * The commented out code was another method I was trying.  Still testing
 * to see if I can move to that method properly.  If so, I can cleanly
 * move any future render registration from my client proxy into my 
 * registerhelper.java
 */

public class RenderNPC extends RenderLiving<EntityNPC>
{
	//public static final Factory FACTORY = new Factory();
	
	private ModelNPC npcModel;
	protected ResourceLocation npcTexture = new ResourceLocation(BH_ModInfo.MOD_ID + ":textures/entity/timmy_text.png");
	
	/*public RenderNPC(RenderManager man)
	*{
	*	super(man, new ModelNPC(), 0.5F);
	*	npcModel = (ModelNPC) super.mainModel;
	*}
	*/
	
	public RenderNPC(ModelBase modBase, float shade)
	{
		super(Minecraft.getMinecraft().getRenderManager(), modBase, shade);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNPC entity)
	{
		return npcTexture;
	}
	
	@Override
	public void doRender(EntityNPC entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	/*public static class Factory implements IRenderFactory<EntityNPC>
	*{
	*	@Override
	*	public Render<? super EntityNPC> createRenderFor(RenderManager manager)
	*	{
	*		return new RenderNPC(manager);
	*	}
	*}
	*/
}
