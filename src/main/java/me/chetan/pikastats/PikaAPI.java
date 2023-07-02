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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PikaAPI {
    public static final String right_point_tri="\u25B6 ";
    public static JsonObject bedwars(String player_name,String interval,String mode){
        return getJson("https://stats.pika-network.net/api/profile/"+player_name+"/leaderboard?type=bedwars&interval="+interval+"&mode="+mode);
    }

    public static void handleArgs(String player_name,String gamemode,String time,String type){
        if(gamemode.equalsIgnoreCase("bw") || gamemode.equalsIgnoreCase("bws") || gamemode.equalsIgnoreCase("bedwar") || gamemode.equalsIgnoreCase("bedwars")){
            String mode="ALL_MODES";

            if(type.equalsIgnoreCase("solo") || type.equalsIgnoreCase("one") || type.equalsIgnoreCase("1")){
                mode="SOLO";
            }else if(type.equalsIgnoreCase("dou") || type.equalsIgnoreCase("doubles") || type.equalsIgnoreCase("double") || type.equalsIgnoreCase("duo") || type.equalsIgnoreCase("two") || type.equalsIgnoreCase("2")){
                mode="DOUBLES";
            }else if(type.equalsIgnoreCase("trio") || type.equalsIgnoreCase("tri") || type.equalsIgnoreCase("triple") || type.equalsIgnoreCase("triples") || type.equalsIgnoreCase("3")){
                mode="TRIPLES";
            }else if(type.equalsIgnoreCase("quad") || type.equalsIgnoreCase("4")){
                mode="QUAD";
            }

            if(time.equalsIgnoreCase("lifetime") || time.equalsIgnoreCase("lt") || time.equalsIgnoreCase("full") || time.equalsIgnoreCase("life")){
                iterateListSend(bedwars(player_name,"total",mode),"bw");
            }else if(time.equalsIgnoreCase("monthly") || time.equalsIgnoreCase("month")){
                iterateListSend(bedwars(player_name,"monthly",mode),"bw");
            }else if(time.equalsIgnoreCase("weekly") || time.equalsIgnoreCase("week")){
                iterateListSend(bedwars(player_name,"weekly",mode),"bw");
            }else{
                iterateListSend(bedwars(player_name,"total",mode),"bw");
            }
        }
    }

    public static void handleArgs(String player_name,String gamemode){
        handleArgs(player_name,gamemode,"lifetime");
    }

    public static void handleArgs(String player_name, String gamemode,String time){
        handleArgs(player_name,gamemode,time,"ALL_MODES");
    }

    public static void help(){
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.DARK_RED+" Use /pikastats <player_name> <gamemode> [weekly/monthly/lifetime] [all/solo/dou/trio/quad]");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.RED+" NOTE:- <> means mandatory and [] means optional");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 bw");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 bw weekly");
        sendText(EnumChatFormatting.YELLOW+" PikaStatsMod | "+EnumChatFormatting.GREEN+"e.g. /pikastats Chetan0402 bw weekly all");
    }

    public static void sendText(String text){
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }

    public static void sendText(IChatComponent e){
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(e);
    }

    private static JsonObject getJson(String url_string){
        try {
            URL url = new URL(url_string);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            connection.disconnect();
            String jsonResponse = responseBuilder.toString();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonResponse);
            return jsonElement.getAsJsonObject();
        } catch(MalformedURLException ignored){
            error("Notify the chetan0402 on discord about this error.");
        } catch (IOException ignored) {
            error("Network error.");
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
        sendText(EnumChatFormatting.YELLOW+"PikaStatsMod | "+EnumChatFormatting.RED+to_tell);
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
}


/*
* {
  "Bow kills": {
    "metadata": {
      "total": 122887
    },
    "entries": [
      {
        "place": 40616,
        "value": "3",
        "id": "Chetan0402"
      }
    ]
  },
  "Kills": {
    "metadata": {
      "total": 1440483
    },
    "entries": [
      {
        "place": 5656,
        "value": "6677",
        "id": "Chetan0402"
      }
    ]
  },
  "Games played": {
    "metadata": {
      "total": 1725032
    },
    "entries": [
      {
        "place": 5204,
        "value": "2541",
        "id": "Chetan0402"
      }
    ]
  },
  "Arrows shot": {
    "metadata": {
      "total": 518614
    },
    "entries": [
      {
        "place": 37762,
        "value": "358",
        "id": "Chetan0402"
      }
    ]
  },
  "Highest winstreak reached": {
    "metadata": {
      "total": 358525
    },
    "entries": [
      {
        "place": 4357,
        "value": "31",
        "id": "Chetan0402"
      }
    ]
  },
  "Beds destroyed": {
    "metadata": {
      "total": 952549
    },
    "entries": [
      {
        "place": 8870,
        "value": "1261",
        "id": "Chetan0402"
      }
    ]
  },
  "Losses": {
    "metadata": {
      "total": 1539383
    },
    "entries": [
      {
        "place": 12152,
        "value": "1165",
        "id": "Chetan0402"
      }
    ]
  },
  "Arrows hit": {
    "metadata": {
      "total": 438952
    },
    "entries": [
      {
        "place": 51665,
        "value": "63",
        "id": "Chetan0402"
      }
    ]
  },
  "Melee kills": {
    "metadata": {
      "total": 820382
    },
    "entries": [
      {
        "place": 12773,
        "value": "2353",
        "id": "Chetan0402"
      }
    ]
  },
  "Final kills": {
    "metadata": {
      "total": 1064665
    },
    "entries": [
      {
        "place": 5217,
        "value": "2351",
        "id": "Chetan0402"
      }
    ]
  },
  "Deaths": {
    "metadata": {
      "total": 1599061
    },
    "entries": [
      {
        "place": 7064,
        "value": "10161",
        "id": "Chetan0402"
      }
    ]
  },
  "Void kills": {
    "metadata": {
      "total": 1192556
    },
    "entries": [
      {
        "place": 4870,
        "value": "2322",
        "id": "Chetan0402"
      }
    ]
  },
  "Wins": {
    "metadata": {
      "total": 847656
    },
    "entries": [
      {
        "place": 2465,
        "value": "906",
        "id": "Chetan0402"
      }
    ]
  }
}
*
*
*
* {"Bow kills":{"metadata":{"total":122887},"entries":[{"place":40616,"value":"3","id":"Chetan0402"}]},"Kills":{"metadata":{"total":1440483},"entries":[{"place":5656,"value":"6677","id":"Chetan0402"}]},"Games played":{"metadata":{"total":1725032},"entries":[{"place":5204,"value":"2541","id":"Chetan0402"}]},"Arrows shot":{"metadata":{"total":518614},"entries":[{"place":37762,"value":"358","id":"Chetan0402"}]},"Highest winstreak reached":{"metadata":{"total":358525},"entries":[{"place":4357,"value":"31","id":"Chetan0402"}]},"Beds destroyed":{"metadata":{"total":952549},"entries":[{"place":8870,"value":"1261","id":"Chetan0402"}]},"Losses":{"metadata":{"total":1539383},"entries":[{"place":12152,"value":"1165","id":"Chetan0402"}]},"Arrows hit":{"metadata":{"total":438952},"entries":[{"place":51665,"value":"63","id":"Chetan0402"}]},"Melee kills":{"metadata":{"total":820382},"entries":[{"place":12773,"value":"2353","id":"Chetan0402"}]},"Final kills":{"metadata":{"total":1064665},"entries":[{"place":5217,"value":"2351","id":"Chetan0402"}]},"Deaths":{"metadata":{"total":1599061},"entries":[{"place":7064,"value":"10161","id":"Chetan0402"}]},"Void kills":{"metadata":{"total":1192556},"entries":[{"place":4870,"value":"2322","id":"Chetan0402"}]},"Wins":{"metadata":{"total":847656},"entries":[{"place":2465,"value":"906","id":"Chetan0402"}]}}
* */