package me.chetan.pikastats;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

import static me.chetan.pikastats.PikaAPI.handleArgs;
import static me.chetan.pikastats.PikaAPI.help;

public class PikaStatsCommand extends CommandBase{
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
        aliases.add("ps");
        return aliases;
    }

    @Override
    public String getCommandName() {
        return "pikastat";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pikastat <player_name> <gamemode>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if(args.length==0 || args.length==1){
            help();
        } else if(args.length==2){
            handleArgs(args[0],args[1]);
        } else if(args.length==3){
            handleArgs(args[0],args[1],args[2]);
        } else{
            help();
        }
    }
}
