package me.chetan.pikastats.util

import me.chetan.pikastats.PikaAPI
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class ScoreboardTracker {
    @SubscribeEvent
    fun scoreboardTracker(ignored:ClientTickEvent){
        if(Minecraft.getMinecraft().theWorld==null) {
            gamemodeForAccess =""
            return
        }
        val scoreboard= Minecraft.getMinecraft().theWorld.scoreboard
        val scoreboardObj=scoreboard.getObjectiveInDisplaySlot(1)
        if (scoreboardObj==null){
            gamemodeForAccess =""
            return
        }
        val gamemode= PikaAPI.removeFormatting(scoreboardObj.displayName).lowercase().replace("\\s".toRegex(),"")
        when(gamemode){
            "bedwars","skywars" -> {}
            else -> {
                gamemodeForAccess =""
                return
            }
        }
        var onPika=false
        var inNormalLobby=false
        var inWaitLobby=false
        for (item in scoreboard.getSortedScores(scoreboardObj)){
            val text= PikaAPI.removeFormatting(
                ScorePlayerTeam.formatPlayerName(
                    scoreboard.getPlayersTeam(item.playerName),
                    item.playerName
                )
            ).replace("\\s".toRegex(),"")
            when(text){
                "play.pika-network.net" -> onPika=true
            }
            try{
                if (text.substring(0,6).equals("online",ignoreCase = true)){
                    inNormalLobby=true
                }
            }catch (_:Exception){}
            try{
                if (text.substring(0,7).equals("players",ignoreCase = true)){
                    inWaitLobby=true
                }
            }catch (_:Exception){

            }
        }
        if (onPika && !inNormalLobby){
            gamemodeForAccess =gamemode
        }else{
            gamemodeForAccess =""
        }
        inGameForAccess = (onPika && !inWaitLobby && !inNormalLobby)
    }

    companion object{
        var gamemodeForAccess=""
        var inGameForAccess=false
    }
}