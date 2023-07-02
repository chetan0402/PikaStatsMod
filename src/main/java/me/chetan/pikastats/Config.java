package me.chetan.pikastats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;

import static me.chetan.pikastats.PikaAPI.error;

public class Config {
    private File dataDir;
    private final File orderList;
    private JsonObject each_order=new JsonObject();
    public Config(){
        this.dataDir= new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()+"/config");
        this.orderList=new File(dataDir.getAbsolutePath()+"/PikaStatOrderList.DoNotDelete");
        if(!this.orderList.exists()){
            try{
                init();
            } catch(IOException e){
                PikaStatsMod.logger.error("Error in creating a new orderList file",e);
            }
        }
    }

    public void init() throws IOException{
        this.orderList.createNewFile();
        LinkedList<String> bw_order=new LinkedList<>();
        bw_order.add("Final kills");
        bw_order.add("Kills");
        bw_order.add("Highest winstreak reached");
        bw_order.add("Beds destroyed");
        bw_order.add("Wins");
        each_order.addProperty("bw", String.valueOf(bw_order));
        writeToFile();
    }

    public LinkedList<String> getGameConfig(String gamemode){
        try {
            String main_string=new String(Files.readAllBytes(this.orderList.toPath()));
            JsonParser parser=new JsonParser();
            JsonObject main_order=parser.parse(main_string).getAsJsonObject();
            this.each_order=main_order;
            String order_string=main_order.get(gamemode).getAsString();
            order_string=order_string.substring(1,order_string.length()-1);
            LinkedList<String> list=new LinkedList<>(Arrays.asList(order_string.split(",")));
            int i=0;
            for(String ele:list) {
                if(ele.charAt(0)==' '){
                    ele=ele.substring(1);
                    list.set(i,ele);
                }
                i=i+1;
            }
            return list;
        } catch (IOException e) {
            error("Couldn't read the config file.");
        } catch(JsonSyntaxException e){
            error("Corrupte or old config.");
            try {
                init();
            } catch (IOException ex) {
                error("Couldn't write to config.");
            }
        }
        return new LinkedList<>();
    }

    public void writeToFile(){
        try {
            Files.write(this.orderList.toPath(), this.each_order.toString().getBytes());
        } catch(IOException e){
            error("Couldn't write to config file.");
        }
    }

    public void updateOrder(String gamemode,String from,int to){
        LinkedList<String> list=getGameConfig(gamemode);
        if(to>=list.size()){
            error("Not a valid Integer input...");
            return;
        }
        int from_index=list.indexOf(from);
        if(from_index == -1){
            error("Not a valid Field...");
            return;
        }
        String to_field=list.get(to);
        list.set(from_index,to_field);
        list.set(to,from);
        each_order.remove(gamemode);
        each_order.addProperty(gamemode,list.toString());
        writeToFile();
    }
}
