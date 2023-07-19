package me.chetan.pikastats.util

import me.chetan.pikastats.map.ChatListener
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class ClientTick {
    private var prev=false
    @SubscribeEvent
    fun clientTick(event:ClientTickEvent){
        if (ScoreboardTracker.inGameForAccess){
            if (prev) return
            prev=true
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/map")
            ChatListener.listenChatForMap=true
        }else{
            prev=false
        }
    }
}