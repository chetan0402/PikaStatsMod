package me.chetan.pikastats;

import com.google.gson.JsonObject;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//VersionChange
@Mod(modid="pikastatsmod",version = "1.0.6")
public class PikaStatsMod {
    //VersionChange
    public static final String mod_version="1.0.6";
    public static Logger logger;
    public static Config config;
    public static boolean updated=false;
    public static JsonObject update_response=null;
    public static UserCache userCache =new UserCache();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){}

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        ClientCommandHandler.instance.registerCommand(new PikaStatsCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        MinecraftForge.EVENT_BUS.register(new ScoreboardTracker());
        logger= LogManager.getLogger("PikaStatsMod");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        config=new Config();
        PikaAPI.INSTANCE.updateVars();
    }
}
