package me.chetan.pikastats

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.EnumChatFormatting

class ConfigCommand: CommandBase() {
    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandAliases(): List<String> {
        return listOf("pc","sc","statsconfig")
    }

    override fun getCommandName(): String {
        return "statconfig"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/statconfig"
    }

    override fun processCommand(sender: ICommandSender?, rawArgs: Array<out String>) {
        var start=0
        var singleArg=""
        var args= mutableListOf<String>()
        for(item in rawArgs){
            val i=rawArgs.indexOf(item)
            val startQoute=item.first()=='"'
            val endQoute=item.last()=='"'
            if (startQoute && endQoute){
                args+=item.substring(1,item.length-1)
            } else if(startQoute){
                singleArg+=item.substring(1)
                start=i
            } else if(endQoute){
                args+="$singleArg ${item.substring(0,item.length-1)}"
                start=0
            }else if(start==0){
                args+=item
            }
            if(i>start && start!=0){
                singleArg+=" $item"
            }
        }

        PikaStatsMod.logger.info(args)

        if (args.isEmpty()){
            PikaAPI.sendText("")
            PikaAPI.sendText(EnumChatFormatting.GOLD.toString() + PikaAPI.right_point_tri + " PikaStatsMod ")
            PikaAPI.sendText("")
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Bedwars",
                HoverEvent.Action.SHOW_TEXT, "Click to re-order bedwars stats",
                ClickEvent.Action.RUN_COMMAND, "/statconfig bw"
            )
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Skywars",
                HoverEvent.Action.SHOW_TEXT, "Click to re-order skywars stats",
                ClickEvent.Action.RUN_COMMAND, "/statconfig sw"
            )
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Unranked Practice",
                HoverEvent.Action.SHOW_TEXT, "Click to re-order unranked practice stats",
                ClickEvent.Action.RUN_COMMAND, "/statconfig uprac"
            )
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Ranked Practice",
                HoverEvent.Action.SHOW_TEXT, "Click to re-order ranked practice stats",
                ClickEvent.Action.RUN_COMMAND, "/statconfig rprac"
            )
            PikaAPI.sendText("")
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.AQUA.toString() + PikaAPI.right_point_tri + " Visibility",
                HoverEvent.Action.SHOW_TEXT, "Click to change visibility of stats in gamemodes",
                ClickEvent.Action.RUN_COMMAND, "/statconfig visibility"
            )
            PikaAPI.sendText("")
            PikaAPI.sendTextClickHover(
                EnumChatFormatting.GOLD.toString() + PikaAPI.right_point_tri + " Tab Stats",
                HoverEvent.Action.SHOW_TEXT, "Click to change tab stats",
                ClickEvent.Action.RUN_COMMAND, "/statconfig tab"
            )
            PikaAPI.sendText("")
            return
        }
        when(args[0]){
            "bw","sw","uprac","rprac" -> when(args.size){
                1 -> PikaAPI.sendOrderChange(args[0])
                3 -> {
                    if(PikaStatsMod.config.updateOrder(args[0],args[1],args[2])){
                        PikaAPI.sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatMod | " + EnumChatFormatting.GREEN + " Successfully changed.")
                    }
                }
            }
            "visibility" -> {
                when(args.size){
                    1 -> {
                        PikaAPI.sendText("")
                        PikaAPI.sendText(EnumChatFormatting.GREEN.toString() + " Set gamemode to change it's stats visibility.")
                        PikaAPI.sendText("")
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Bedwars",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide fields in bedwars stats",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility bw"
                        )
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Skywars",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide fields in skywars stats",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility sw"
                        )
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Unranked Practice",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide fields in unranked practice stats",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility uprac"
                        )
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " Ranked Practice",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide fields in ranked practice stats",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility rprac"
                        )
                        PikaAPI.sendText("")
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.AQUA.toString() + PikaAPI.right_point_tri + " FKDR",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility fkdr"
                        )
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.AQUA.toString() + PikaAPI.right_point_tri + " wlr",
                            HoverEvent.Action.SHOW_TEXT, "Click to hide/unhide ",
                            ClickEvent.Action.RUN_COMMAND, "/statsconfig visibility wlr"
                        )
                        PikaAPI.sendText("")
                    }
                    2 -> {
                        when(args[1].lowercase()){
                            "bw","sw","uprac","rprac" -> PikaAPI.sendVisiChange(args[1].lowercase())
                            "fkdr","wlr" -> PikaStatsMod.config.toggleExtra(args[1].lowercase())
                        }
                    }
                    3 -> {
                        if(listOf("bw","sw","uprac","rprac").contains(args[1])){
                            PikaStatsMod.config.toggleStat(args[1], args[2])
                            PikaAPI.sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatMod | " + EnumChatFormatting.GREEN + " Successfully changed.")
                            PikaAPI.sendVisiChange(args[1])
                        }
                    }
                    else -> PikaAPI.error("Wrong command.")
                }
            }
            "tab"->{
                when(args.size){
                    1 -> {
                        PikaAPI.sendText("")
                        for (stat in PikaStatsMod.config.tabOptions){
                            PikaAPI.sendTextClickHover(
                                EnumChatFormatting.YELLOW.toString() + PikaAPI.right_point_tri + " $stat",
                                HoverEvent.Action.SHOW_TEXT, "Click to set tab stats as $stat",
                                ClickEvent.Action.RUN_COMMAND, "/statconfig tab \"$stat\""
                            )
                        }
                        PikaAPI.sendText("")
                        PikaAPI.sendTextClickHover(
                            EnumChatFormatting.DARK_RED.toString() + PikaAPI.right_point_tri + " Disable",
                            HoverEvent.Action.SHOW_TEXT, "Disable tab stats",
                            ClickEvent.Action.RUN_COMMAND, "/statconfig tab disable"
                        )
                        PikaAPI.sendText("")
                    }
                    2 -> {
                        PikaStatsMod.config.setTab(args[1])
                        PikaAPI.sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatMod | " + EnumChatFormatting.GREEN + " Successfully changed.")
                    }
                }
            }
            else -> PikaAPI.error("Wrong command.")
        }
    }
}