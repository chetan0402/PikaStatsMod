package me.chetan.pikastats

import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class ScoreboardTracker {
    @SubscribeEvent
    fun scoreboardTracker(e:ClientTickEvent){
        if(Minecraft.getMinecraft().theWorld==null) {
            gamemodeForAccess=""
            return
        }
        val scoreboard= Minecraft.getMinecraft().theWorld.scoreboard
        val scoreboardObj=scoreboard.getObjectiveInDisplaySlot(1)
        if (scoreboardObj==null){
            gamemodeForAccess=""
            return
        }
        val gamemode=PikaAPI.removeFormatting(scoreboardObj.displayName).lowercase().replace("\\s".toRegex(),"")
        when(gamemode){
            "bedwars","skywars" -> {}
            else -> {
                gamemodeForAccess=""
                return
            }
        }
        var onPika=false
        var inNormalLobby=false
        for (item in scoreboard.getSortedScores(scoreboardObj)){
            val text=PikaAPI.removeFormatting(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(item.playerName),item.playerName)).replace("\\s".toRegex(),"")
            when(text){
                "play.pika-network.net" -> onPika=true
            }
            try{
                if (text.substring(0,6).equals("online",ignoreCase = true)){
                    inNormalLobby=true
                }
            }catch (_:Exception){

            }
        }
        if (onPika && !inNormalLobby){
            gamemodeForAccess=gamemode
        }else{
            gamemodeForAccess=""
        }
    }

    companion object{
        var gamemodeForAccess=""
    }
}