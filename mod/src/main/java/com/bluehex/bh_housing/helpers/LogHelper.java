package com.bluehex.bh_housing.helpers;

import org.apache.logging.log4j.Level;

import com.bluehex.bh_housing.BH_ModInfo;

import net.minecraftforge.fml.common.FMLLog;

/*
 * Not currently in use.  Will be depreciated, removed, or
 * moved into use once I start learning to use it properly.
 * Got this method from a tutorial and have not tested or 
 * used it yet.
 */

public class LogHelper
{
	public static void log(Level logLevel, Object object)
	{
		FMLLog.log(BH_ModInfo.MOD_NAME, logLevel, String.valueOf(object));
	}
	public static void all(Object object) {log(Level.ALL, object);}
	public static void debug(Object object) {log(Level.DEBUG, object);}
	public static void error(Object object) {log(Level.ERROR, object);}
	public static void fatal(Object object) {log(Level.FATAL, object);}
	public static void info(Object object) {log(Level.INFO, object);}
	public static void off(Object object) {log(Level.OFF, object);}
	public static void trace(Object object) {log(Level.TRACE, object);}
	public static void warn(Object object) {log(Level.WARN, object);}
}
