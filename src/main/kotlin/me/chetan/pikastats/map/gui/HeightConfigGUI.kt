package me.chetan.pikastats.map.gui

import me.chetan.pikastats.PikaStatsMod
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.input.Mouse

//TODO - whole config left
class HeightConfigGUI: GuiScreen() {
    var prevMouse0=false
    override fun initGui() {
        super.initGui()
        val configResXRatio= PikaStatsMod.config.eachOrder["heightx"].asFloat/ PikaStatsMod.config.eachOrder["resx"].asFloat
        val configResYRatio= PikaStatsMod.config.eachOrder["heighty"].asFloat/ PikaStatsMod.config.eachOrder["resy"].asFloat
        val x= (Minecraft.getMinecraft().displayWidth*configResXRatio).toInt()
        val y= (Minecraft.getMinecraft().displayHeight*configResYRatio).toInt() / 2
        isOpen=true
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        isOpen=false
    }

    @SubscribeEvent
    fun tickEvent(event:ClientTickEvent){
        val mouseX=Mouse.getX()
        val mouseY=Mouse.getY()
        if (isOpen){
            if (HeightDisplayGUI.box[0]<mouseX && mouseX<HeightDisplayGUI.box[2] &&
                HeightDisplayGUI.box[1]<mouseY && mouseY<HeightDisplayGUI.box[3]){
                if (Mouse.isButtonDown(0) && !prevMouse0){
                    prevMouse0=true
                }else if (Mouse.isButtonDown(0) && prevMouse0){

                }
            }
        }
    }

    companion object{
        var isOpen=false
    }
}