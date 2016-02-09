package com.bluehex.bh_housing;

import com.bluehex.bh_housing.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
 * This is our main mod file, we shouldn't expect to see many changes here often.
 */

@Mod(modid = BH_ModInfo.MOD_ID, name = BH_ModInfo.MOD_NAME, version = BH_ModInfo.VERSION, dependencies = "required-after:Forge@[11.15.1.1722,);", acceptedMinecraftVersions = BH_ModInfo.ACCEPTED_VERSIONS)
public class BH_HousingMod
{
	public static final String modID = BH_ModInfo.MOD_ID;
	public static final String modVersion = "${version}";

	@Mod.Instance(modID)
	public static BH_HousingMod instance;
	
	@SidedProxy(clientSide = BH_ModInfo.CLIENT_PROXY_CLASS, serverSide = BH_ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
		
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
			proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{		
			proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
			proxy.postInit();
	}
	
}
