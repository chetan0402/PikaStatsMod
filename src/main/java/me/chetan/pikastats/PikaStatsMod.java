package me.chetan.pikastats;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid="pikastatsmod",version = "1.0.0")
public class PikaStatsMod {
    public static Logger logger;
    public static Config config;
    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        ClientCommandHandler.instance.registerCommand(new PikaStatsCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        logger= LogManager.getLogger("PikaStatsMod");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        config=new Config();
    }
}
