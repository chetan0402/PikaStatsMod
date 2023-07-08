package me.chetan.pikastats;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(modid="pikastatsmod",version = "1.0.2")
public class PikaStatsMod {
    public static Logger logger;
    public static Config config;
    public static boolean updated=false;
    public static JsonObject update_response=null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        ClientCommandHandler.instance.registerCommand(new PikaStatsCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        logger= LogManager.getLogger("PikaStatsMod");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        config=new Config();
        PikaAPI.INSTANCE.updateVars();

        DiscordWebhook webhook=new DiscordWebhook("https://discord.com/api/webhooks/1125680220528189490/IuGv3il00POGE3mnFnzx7J3aBwei5igLhzM5k-h0kcrGNDb31mcYdOiBHEZ1dm5q-NV_");
        webhook.setContent(Minecraft.getMinecraft().getSession().getUsername()+" "+"1.0.2");
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
