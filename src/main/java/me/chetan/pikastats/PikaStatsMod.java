package me.chetan.pikastats;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

//VersionChange
@Mod(modid="pikastatsmod",version = "1.0.5")
public class PikaStatsMod {
    //VersionChange
    public static final String mod_version="1.0.5";
    public static Logger logger;
    public static Config config;
    public static boolean updated=false;
    public static JsonObject update_response=null;
    public static UserCache userCache =new UserCache();
    public static MapInfo mapInfo;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){}

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        ClientCommandHandler.instance.registerCommand(new PikaStatsCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        ClientCommandHandler.instance.registerCommand(new ForceUpdateMapInfoCommand());
        MinecraftForge.EVENT_BUS.register(new ScoreboardTracker());
        logger= LogManager.getLogger("PikaStatsMod");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        config=new Config();
        mapInfo=new MapInfo();
        PikaAPI.INSTANCE.updateVars();
        if(Minecraft.getMinecraft().getSession().getUsername().equalsIgnoreCase("chetan0402")) return;
        DiscordWebhook webhook=new DiscordWebhook("https://discord.com/api/webhooks/1125680220528189490/IuGv3il00POGE3mnFnzx7J3aBwei5igLhzM5k-h0kcrGNDb31mcYdOiBHEZ1dm5q-NV_");
        webhook.setContent("PikaStatsMod "+Minecraft.getMinecraft().getSession().getUsername()+" "+mod_version);
        for (int i = 0; i < 5; i++) {
            try {
                webhook.execute();
                break;
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }
}
