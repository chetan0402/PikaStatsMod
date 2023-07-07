package me.chetan.pikastats

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.EnumChatFormatting

class PikaStatsCommand: CommandBase() {

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandAliases(): List<String> {
        return listOf("ps","pikastats")
    }

    override fun getCommandName(): String {
        return "pikastat"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/pikastat <player_name> <gamemode>"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>) {
        val obj = ProcessStatsCommand(args)
        obj.start()
    }


    internal class ProcessStatsCommand(private val args: Array<out String>) : Thread() {
        override fun run() {
            if (args.isEmpty() || args.size == 1) {
                PikaAPI.help()
            } else if (2<=args.size){
                var gamemode=""
                var otherGamemode=""
                when(args[1].lowercase()){
                    "bw","bws","bedwar","bedwars" -> {gamemode="bedwars";otherGamemode="bw"}
                    "sw","sws","skywar","skywars" -> {gamemode="skywars";otherGamemode="sw"}
                    "uprac","unprac","unrankprac","unrankedprac" -> {gamemode="unrankedpractice";otherGamemode="uprac"}
                    else -> PikaAPI.sendText(""+EnumChatFormatting.GOLD+" PikaStatsMod | "+EnumChatFormatting.RED+"No such gamemode: "+args[1])
                }

                var time="total"
                if (3<=args.size){
                    time = when(args[2].lowercase()){
                        "week","weekly" -> "weekly"
                        "month","monthly","mon" -> "monthly"
                        else -> "total"
                    }
                }
                var mode="ALL_MODES"
                if (4<=args.size){
                    mode=when(args[3].lowercase()){
                        "solo","solos","1" -> "SOLO"
                        "duo","dou","doubles","double","2","duos","dous" -> "DOUBLES"
                        "tri","trios","triples","triple","3" -> "TRIPLES"
                        "quad","quads","four","fours","4" -> "QUAD"
                        "nodebuff","node" -> "NODEBUFF"
                        "debuff" -> "DEBUFF"
                        "boxing" -> "BOXING"
                        "gap","gapple" -> "GAPPLE"
                        "combo" -> "COMBO"
                        "soup" -> "SOUP"
                        "axe","axe pvp","axepvp" -> "AXE_PVP"
                        "build uhc","uhc","builduhc" -> "BUILD_UHC"
                        "bridge","the bridge","thebridge" -> "THE_BRIDGE"
                        "bed fight","bedfight" -> "BED_FIGHT"
                        "void","void fight","voidfight" -> "VOID_FIGHT"
                        "peral","peral fight","peralfight" -> "PEARL_FIGHT"
                        "battle","battle rush","rush","battlerush" -> "BATTLE_RUSH"
                        "sumo" -> "SUMO"
                        "archer" -> "ARCHER"
                        "parkour" -> "PARKOUR"
                        "spleef" -> "SPLEEF"
                        else -> "ALL_MODES"
                    }
                }
                PikaAPI.iterateListSend(PikaAPI.minigames(args[0],time,mode,gamemode),otherGamemode)

            } else {
                PikaAPI.help()
            }
            if (!PikaStatsMod.updated) {
                PikaAPI.updateVars()
                if (!PikaStatsMod.updated && PikaStatsMod.update_response != null) {
                    PikaAPI.sendTextClickHover(
                            EnumChatFormatting.GOLD.toString() + " PikaStatsMod | " + EnumChatFormatting.RED + " " + PikaStatsMod.update_response["update"].toString(), HoverEvent.Action.SHOW_TEXT, "Click to download updated mod jar.",
                            ClickEvent.Action.OPEN_URL, PikaStatsMod.update_response["url"].toString()
                    )
                }
            }
        }
    }


}