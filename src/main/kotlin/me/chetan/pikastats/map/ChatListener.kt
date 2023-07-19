package me.chetan.pikastats.map

import me.chetan.pikastats.PikaAPI
import me.chetan.pikastats.PikaStatsMod
import me.chetan.pikastats.map.gui.HeightDisplayGUI
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class ChatListener {
    private var prev: IChatComponent=ChatComponentText("")
    @SubscribeEvent
    fun chatListenerForMap(event:ClientTickEvent){
        if (listenChatForMap && prev!=currentChatMsg){
            prev= currentChatMsg
            val message=PikaAPI.removeFormatting(prev.unformattedText)
            if (!message.startsWith("Currently")){return}
            val sorting=message.replace(" ","").lowercase()
            val moreSorting=sorting.replace("currentlyplayingonmap:","")
            val index=moreSorting.indexOf("(")
            if (index==-1) return
            val map=moreSorting.substring(0,index)
            HeightDisplayGUI.mapName=map
            HeightDisplayGUI.mapHeight=PikaStatsMod.mapInfo.getHeight(map)
            listenChatForMap=false
        }
    }

    companion object{
        var listenChatForMap=false
        var currentChatMsg: IChatComponent=ChatComponentText("")
    }
}