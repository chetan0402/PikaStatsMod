package me.chetan.pikastats;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static me.chetan.pikastats.PikaAPI.*;

public class ConfigCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases=new ArrayList<>();
        aliases.add("pc");
        aliases.add("sc");
        aliases.add("statsconfig");
        return aliases;
    }

    @Override
    public String getCommandName() {
        return "statconfig";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/statconfig";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] premitive_args){
        int start=0;
        StringBuilder the_string=new StringBuilder();
        LinkedList<String> args=new LinkedList<>();

        for (int i = 0; i < premitive_args.length; i++) {
            if(premitive_args[i].charAt(0)=='"'){
                start=i;
                the_string.append(premitive_args[i].substring(1)).append(" ");
            }
            if(premitive_args[i].charAt(premitive_args[i].length()-1) == '"'){
                the_string.append(premitive_args[i].replace("\"",""));
                args.add(the_string.toString());
                start=0;
            }else if(start==0){
                args.add(premitive_args[i]);
            }
            if(i>start && start!=0){
                the_string.append(premitive_args[i]).append(" ");
            }
        }

        System.out.println(args.size());
        System.out.println(args);

        if(args.size()==0){
            sendText("");
            sendText(EnumChatFormatting.YELLOW+right_point_tri+" PikaStatsMod ");
            sendText(new ChatComponentText(EnumChatFormatting.YELLOW+right_point_tri+" Bedwars").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/statconfig bw"))));
            sendText("");
        }else if(args.size()==1){
            if(args.get(0).equalsIgnoreCase("bw")){
                sendText("");
                sendText(EnumChatFormatting.YELLOW+right_point_tri+" PikaStatsMod ");
                sendText(EnumChatFormatting.GREEN+" Click the field to change it's position...");
                sendText(EnumChatFormatting.GREEN+" Put the number of it's new position...");
                sendText(EnumChatFormatting.AQUA+" | /statsconfig bw Final ");
                sendText("");
                int i=0;
                for(String field:PikaStatsMod.config.getGameConfig("bw")){
                    sendTextClickHover(
                            EnumChatFormatting.YELLOW+PikaAPI.right_point_tri+i+". "+field+" ",
                            HoverEvent.Action.SHOW_TEXT,"Click to change it's order..",
                            ClickEvent.Action.SUGGEST_COMMAND,"/statconfig bw \""+field+"\" "+i);
                    i=i+1;
                }
                sendText("");
                return;
            }

            sendText(EnumChatFormatting.YELLOW+" PikaStatMod | "+EnumChatFormatting.RED+"Wrong command.");
        }else if(args.size()==3){
            if(args.get(0).equalsIgnoreCase("bw")){
                if(PikaStatsMod.config.updateOrder("bw", args.get(1), args.get(2))){
                    sendText(EnumChatFormatting.YELLOW+" PikaStatMod | "+EnumChatFormatting.GREEN+" Successfully changed.");
                }
            }
        }else{
            sendText(EnumChatFormatting.YELLOW+" PikaStatMod | "+EnumChatFormatting.RED+"Wrong command.");
        }
    }
}
