package me.chetan.pikastats;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
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
    public void processCommand(ICommandSender sender, String[] args){
        if(args.length==0){
            sendText(new ChatComponentText(EnumChatFormatting.YELLOW+"Bedwars").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/statconfig bw"))));
        }else if(args.length==1){
            if(args[0].equalsIgnoreCase("bw")){
                int i=0;
                for(String field:PikaStatsMod.config.getGameConfig("bw")){
                    sendTextClickHover(
                            EnumChatFormatting.YELLOW+PikaAPI.right_point_tri+i+". "+field+" ",
                            HoverEvent.Action.SHOW_TEXT,"Click to change it's order..",
                            ClickEvent.Action.SUGGEST_COMMAND,"/statconfig bw "+field+" "+i);
                    i=i+1;
                }
            }
        }else if(args.length==3){
            if(args[0].equalsIgnoreCase("bw")){
                try {
                    PikaStatsMod.config.updateOrder("bw", args[1], Integer.parseInt(args[2]));
                    sendText(EnumChatFormatting.YELLOW+" PikaStatMod | "+EnumChatFormatting.GREEN+" Successfully changed.");
                } catch(NumberFormatException e){
                    error(args[2]+" is not an integer");
                }
            }
        }else{
            sendText(EnumChatFormatting.YELLOW+" PikaStatMod | "+EnumChatFormatting.RED+"Wrong command.");
        }
    }
}
