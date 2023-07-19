package me.chetan.pikastats;

import com.google.gson.JsonObject;
import me.chetan.pikastats.config.Config;
import me.chetan.pikastats.config.ConfigCommand;
import me.chetan.pikastats.map.ChatListener;
import me.chetan.pikastats.map.ForceUpdateMapInfoCommand;
import me.chetan.pikastats.map.GuiListener;
import me.chetan.pikastats.map.MapInfo;
import me.chetan.pikastats.util.ClientTick;
import me.chetan.pikastats.util.ScoreboardTracker;
import me.chetan.pikastats.util.UserCache;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid="pikastatsmod",version = PikaStatsMod.mod_version,clientSideOnly = true)
public class PikaStatsMod {
    //VersionChange
    public static final String mod_version="1.0.7";
    public static Logger logger;
    public static Config config;
    public static boolean updated=false;
    public static JsonObject update_response=null;
    public static UserCache userCache =new UserCache();
    public static MapInfo mapInfo;
    public static boolean guiOpen=false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){}

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        ClientCommandHandler.instance.registerCommand(new PikaStatsCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        ClientCommandHandler.instance.registerCommand(new ForceUpdateMapInfoCommand());
        MinecraftForge.EVENT_BUS.register(new ScoreboardTracker());
        MinecraftForge.EVENT_BUS.register(new GuiListener());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(new ClientTick());
        logger= LogManager.getLogger("PikaStatsMod");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        config=new Config();
        mapInfo=new MapInfo();
        PikaAPI.INSTANCE.updateVars();
    }
}
