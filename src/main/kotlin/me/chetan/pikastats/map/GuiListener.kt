package me.chetan.pikastats.map

import me.chetan.pikastats.map.gui.HeightDisplayGUI
import me.chetan.pikastats.util.ScoreboardTracker
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class GuiListener {
    @SubscribeEvent
    fun guiListener(event:RenderGameOverlayEvent.Post){
        if (ScoreboardTracker.inGameForAccess && event.type==RenderGameOverlayEvent.ElementType.EXPERIENCE){
            HeightDisplayGUI()
        }
    }
}