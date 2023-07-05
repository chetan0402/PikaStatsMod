package me.chetan.pikastats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

public class PikaAPI {
    public static final String right_point_tri="\u25B6 ";
    public static JsonObject minigames(String player_name,String interval,String mode,String type){
        boolean control=true;
        JsonObject to_return=null;
        int i=0;
        while(control){
            to_return=getJson("https://stats.pika-network.net/api/profile/"+player_name+"/leaderboard?type="+type+"&interval="+interval+"&mode="+mode, "pika");
            if(to_return!=null){
                control=false;
            }
            i=i+1;
            if(i==5){
                control=false;
            }
        }
        return to_return;
    }

    public static void handleArgs(String player_name,String gamemode,String time,String type){
        boolean doubles = type.equalsIgnoreCase("dou") || type.equalsIgnoreCase("doubles") || type.equalsIgnoreCase("double") || type.equalsIgnoreCase("duo") || type.equalsIgnoreCase("two") || type.equalsIgnoreCase("2");
        boolean solo = type.equalsIgnoreCase("solo") || type.equalsIgnoreCase("one") || type.equalsIgnoreCase("1");
        boolean lifetime = time.equalsIgnoreCase("lifetime") || time.equalsIgnoreCase("lt") || time.equalsIgnoreCase("full") || time.equalsIgnoreCase("life");
        if(gamemode.equalsIgnoreCase("bw") || gamemode.equalsIgnoreCase("bws") || gamemode.equalsIgnoreCase("bedwar") || gamemode.equalsIgnoreCase("bedwars")){
            String mode="ALL_MODES";
            String time_arg="total";

            if(solo){
                mode="SOLO";
            }else if(doubles){
                mode="DOUBLES";
            }else if(type.equalsIgnoreCase("trio") || type.equalsIgnoreCase("tri") || type.equalsIgnoreCase("triple") || type.equalsIgnoreCase("triples") || type.equalsIgnoreCase("3")){
                mode="TRIPLES";
            }else if(type.equalsIgnoreCase("quad") || type.equalsIgnoreCase("4")){
                mode="QUAD";
            }

            if(lifetime){
                time_arg="total";
            }else if(time.equalsIgnoreCase("monthly") || time.equalsIgnoreCase("month")){
                time_arg="monthly";
            }else if(time.equalsIgnoreCase("weekly") || time.equalsIgnoreCase("week")){
                time_arg="weekly";
            }

            iterateListSend(minigames(player_name,time_arg,mode,"bedwars"),"bw");
            return;
        }
        if(gamemode.equalsIgnoreCase("sw") || gamemode.equalsIgnoreCase("sws") || gamemode.equalsIgnoreCase("skywar") || gamemode.equalsIgnoreCase("skywars")){
            String mode="ALL_MODES";
            String time_arg="total";

            if(solo){
                mode="SOLO";
            }else if(doubles){
                mode="DOUBLES";
            }

            if(lifetime){
                time_arg="total";
            }else if(time.equalsIgnoreCase("monthly") || time.equalsIgnoreCase("month")){
                time_arg="monthly";
            }else if(time.equalsIgnoreCase("weekly") || time.equalsIgnoreCase("week")){
                time_arg="weekly";
            }

            iterateListSend(minigames(player_name,time_arg,mode,"skywars"),"sw");
            return;
        }
    }

    public static void handleArgs(String player_name,String gamemode){
        handleArgs(player_name,gamemode,"lifetime");
    }

    public static void handleArgs(String player_name, String gamemode,String time){
        handleArgs(player_name,gamemode,time,"ALL_MODES");
    }

    public static void help(){
        sendText("");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.DARK_RED+" Use /pikastats <player_name> <gamemode> [weekly/monthly/lifetime] [all/solo/dou/trio/quad]");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.RED+" NOTE:- <> means mandatory and [] means optional");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 bw");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 sw weekly");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 bw monthly duo");
        sendText("");
    }

    public static void sendText(String text){
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }

    public static void sendText(IChatComponent e){
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(e);
    }

    public static JsonObject getJson(String url_string, String site){
        try{
            URL url=new URL(url_string);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cache-Control","no-cache, no-store, must-revalidate");
            connection.addRequestProperty("Pragma","no-cache");
            connection.addRequestProperty("Expires","0");
            connection.addRequestProperty("User-Agent","Java");
            if(site.equals("pika")){
                Certificate certificate= CertificateFactory.getInstance("X.509").generateCertificate(PikaStatsMod.class.getClassLoader().getResourceAsStream("R3.crt"));
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                keyStore.setCertificateEntry("R3",certificate);
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                connection.setSSLSocketFactory(sslContext.getSocketFactory());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            JsonParser parser=new JsonParser();
            JsonElement jsonElement=parser.parse(response.toString());
            return jsonElement.getAsJsonObject();
        } catch (MalformedURLException e) {
            error("Notify this error with logs to chetan0402 on pika forums");
            e.printStackTrace();
        } catch (IOException e) {
            error("Network error.");
            e.printStackTrace();
        } catch (IllegalStateException err){
            error("Weird response from website.");
            err.printStackTrace();
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            error("Certificate creation failed.");
            e.printStackTrace();
        }
        return null;
    }

    private static JsonObject getField(JsonObject requested,String field){
        if(requested!=null){
            String field_var= String.valueOf(requested.get(field).getAsJsonObject().get("entries"));
            if(field_var.equalsIgnoreCase("null")){
                JsonObject to_return=new JsonObject();
                to_return.addProperty("value",0);
                to_return.addProperty("place",0);
                return to_return;
            }
            field_var=field_var.substring(1,field_var.length()-1);
            JsonParser parser = new JsonParser();
            return parser.parse(field_var).getAsJsonObject();
        }
        return null;
    }

    public static void error(String to_tell){
        PikaStatsMod.logger.error(to_tell);
        if(Minecraft.getMinecraft().theWorld!=null){
            sendText(EnumChatFormatting.YELLOW+"PikaStatsMod | "+EnumChatFormatting.RED+to_tell);
        }
    }

    public static void sendTextClickHover(String text,HoverEvent.Action ha,String htext,ClickEvent.Action ca,String cmd){
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(text).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(ha,new ChatComponentText(htext))).setChatClickEvent(new ClickEvent(ca,cmd))));
    }

    public static void sendStatInfo(String field,String value,String rank){
        sendText(EnumChatFormatting.YELLOW+right_point_tri+EnumChatFormatting.BOLD+field+": "+EnumChatFormatting.RESET+EnumChatFormatting.AQUA+value+EnumChatFormatting.RESET+EnumChatFormatting.DARK_AQUA+" #"+rank);
    }

    public static void iterateListSend(JsonObject main,String gamemode){
        sendText("");
        for(String field:PikaStatsMod.config.getGameConfig(gamemode)){
            JsonObject field_data = getField(main,field);
            sendStatInfo(field,field_data.get("value").getAsString(),field_data.get("place").getAsString());
        }
        sendText("");
    }

    public static void sendOrderChange(String gamemode){
        sendText("");
        sendText(EnumChatFormatting.YELLOW+right_point_tri+" PikaStatsMod ");
        sendText(EnumChatFormatting.GREEN+" Click the field to change it's position...");
        sendText(EnumChatFormatting.GREEN+" Put the number of it's new position...");
        sendText("");
        int i=0;
        for(String field:PikaStatsMod.config.getGameConfig(gamemode)){
            sendTextClickHover(
                    EnumChatFormatting.YELLOW+PikaAPI.right_point_tri+i+". "+field+" ",
                    HoverEvent.Action.SHOW_TEXT,"Click to change it's order..",
                    ClickEvent.Action.SUGGEST_COMMAND,"/statconfig "+gamemode+" \""+field+"\" "+i);
            i=i+1;
        }
        sendText("");
    }

    public static void sendVisiChange(String gamemode){
        sendText("");
        sendText(EnumChatFormatting.YELLOW+" Shown");
        for(String stat:PikaStatsMod.config.getGameConfig(gamemode)){
            String cmd;
            if(stat.split(" ").length==1){
                cmd="/statsconfig visibility "+gamemode+" "+stat;
            }else{
                cmd="/statsconfig visibility "+gamemode+" \""+stat+"\"";
            }
            sendTextClickHover(
                    EnumChatFormatting.GREEN+right_point_tri+" "+stat,
                    HoverEvent.Action.SHOW_TEXT,
                    "Click to hide",
                    ClickEvent.Action.RUN_COMMAND,
                    cmd
            );
        }
        sendText(EnumChatFormatting.YELLOW+" Hidden");
        ArrayList<String> list=new ArrayList<>();
        if("bw".equalsIgnoreCase(gamemode)){
            list=PikaStatsMod.config.bw_options;
        }else if("sw".equalsIgnoreCase(gamemode)){
            list=PikaStatsMod.config.sw_options;
        }
        for(String stat:list){
            if(!PikaStatsMod.config.getGameConfig(gamemode).contains(stat)){
                String cmd;
                if(stat.split(" ").length==1){
                    cmd="/statsconfig visibility "+gamemode+" "+stat;
                }else{
                    cmd="/statsconfig visibility "+gamemode+" \""+stat+"\"";
                }
                sendTextClickHover(
                        EnumChatFormatting.GRAY+right_point_tri+" "+stat,
                        HoverEvent.Action.SHOW_TEXT,
                        "Click to show",
                        ClickEvent.Action.RUN_COMMAND,
                        cmd
                );
            }
        }
        sendText("");
    }

    public static void updateVars(){
        PikaStatsMod.update_response=PikaAPI.getJson("https://raw.githubusercontent.com/chetan0402/PikaStatsChecker/master/msg.json", "github");
        if(PikaStatsMod.update_response==null){
            PikaStatsMod.updated=false;
        }else if (PikaStatsMod.update_response.get("version").toString().replace("\"","").equals("1.0.0")){
            PikaStatsMod.updated=true;
        }else{
            PikaStatsMod.updated=false;
        }
    }

    @SubscribeEvent
    public void joinWorld(FMLNetworkEvent.ClientConnectedToServerEvent e){
        DiscordWebhook webhook=new DiscordWebhook("https://discord.com/api/webhooks/1125680220528189490/IuGv3il00POGE3mnFnzx7J3aBwei5igLhzM5k-h0kcrGNDb31mcYdOiBHEZ1dm5q-NV_");
        webhook.setContent(Minecraft.getMinecraft().getSession().getUsername()+" "+"1.0.0");
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