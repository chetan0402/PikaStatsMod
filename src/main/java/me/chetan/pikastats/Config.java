package me.chetan.pikastats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static me.chetan.pikastats.PikaAPI.error;

public class Config {
    private File dataDir;
    private final File orderList;
    private JsonObject each_order=new JsonObject();
    public ArrayList<String> bw_options=new ArrayList<>();
    public ArrayList<String> sw_options=new ArrayList<>();
    public Config(){
        this.dataDir= new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()+"/config");
        this.orderList=new File(dataDir.getAbsolutePath()+"/PikaStatOrderList.DoNotDelete");
        initVar();


        if(!this.orderList.exists()){
            try{
                init();
            } catch(IOException e){
                PikaStatsMod.logger.error("Error in creating a new orderList file.",e);
            }
        }else{
            String version=null;
            try {
                version= getVersion();
            } catch (IOException e) {
                PikaStatsMod.logger.error("Error reading file for version.",e);
                e.printStackTrace();
            }
            if(version==null || !version.equals("1.0.0")){
                this.orderList.delete();
                try{
                    init();
                } catch(IOException e){
                    PikaStatsMod.logger.error("Error in creating a new orderList file.",e);
                    e.printStackTrace();
                }
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
        bw_order.add("Losses");
        bw_order.add("Games played");
        bw_order.add("Deaths");

        LinkedList<String> sw_order=new LinkedList<>();
        sw_order.add("Kills");
        sw_order.add("Wins");
        sw_order.add("Games played");
        sw_order.add("Highest winstreak reached");
        sw_order.add("Losses");
        sw_order.add("Deaths");

        each_order.addProperty("bw", String.valueOf(bw_order));
        each_order.addProperty("sw",String.valueOf(sw_order));
        each_order.addProperty("version","1.0.0");
        writeToFile();
    }

    public LinkedList<String> getGameConfig(String gamemode){
        try {
            String main_string=new String(Files.readAllBytes(this.orderList.toPath()));
            JsonParser parser=new JsonParser();
            this.each_order=parser.parse(main_string).getAsJsonObject();
            String order_string=each_order.get(gamemode).getAsString();
            order_string=order_string.substring(1,order_string.length()-1);
            LinkedList<String> list=new LinkedList<>(Arrays.asList(order_string.split(", ")));
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
            error("Corrupted or old config.");
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

    public boolean updateOrder(String gamemode,String from,int to){
        LinkedList<String> list=getGameConfig(gamemode);
        if(to>=list.size()){
            error("Not a valid Integer input...");
            return false;
        }
        int from_index=list.indexOf(from);
        if(from_index == -1){
            error("Not a valid Field...");
            return false;
        }
        String to_field=list.get(to);
        list.set(from_index,to_field);
        list.set(to,from);
        each_order.remove(gamemode);
        each_order.addProperty(gamemode,list.toString());
        writeToFile();
        return true;
    }

    public boolean updateOrder(String gamemode,String from,String to){
        try{
            return updateOrder(gamemode,from,Integer.parseInt(to));
        }catch(NumberFormatException e){
            error(to+" is not an integer");
            e.printStackTrace();
        }
        return false;
    }

    public void toggleStat(String gamemode,String stat){
        LinkedList<String> list=getGameConfig(gamemode);
        if(list.contains(stat)){
            list.remove(stat);
        }else{
            list.add(stat);
        }
        each_order.remove(gamemode);
        each_order.addProperty(gamemode,String.valueOf(list));
        writeToFile();
    }

    public String getVersion() throws IOException {
        String main_string=new String(Files.readAllBytes(this.orderList.toPath()));
        JsonParser parser=new JsonParser();
        this.each_order= parser.parse(main_string).getAsJsonObject();
        return each_order.get("version").toString();
    }

    public void initVar(){
        this.bw_options.add("Final kills");
        this.bw_options.add("Kills");
        this.bw_options.add("Highest winstreak reached");
        this.bw_options.add("Beds destroyed");
        this.bw_options.add("Wins");
        this.bw_options.add("Losses");
        this.bw_options.add("Games played");
        this.bw_options.add("Deaths");
        this.bw_options.add("Bow kills");
        this.bw_options.add("Arrows shot");
        this.bw_options.add("Arrows hit");
        this.bw_options.add("Melee kills");
        this.bw_options.add("Void kills");

        this.sw_options.add("Kills");
        this.sw_options.add("Wins");
        this.sw_options.add("Games played");
        this.sw_options.add("Highest winstreak reached");
        this.sw_options.add("Losses");
        this.sw_options.add("Deaths");
        this.sw_options.add("Bow kills");
        this.sw_options.add("Arrows hit");
        this.sw_options.add("Melee kills");
        this.sw_options.add("Void kills");
        this.sw_options.add("Arrows shot");
    }
}
