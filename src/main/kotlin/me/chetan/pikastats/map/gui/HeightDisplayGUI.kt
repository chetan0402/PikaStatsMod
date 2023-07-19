package me.chetan.pikastats.map.gui

import me.chetan.pikastats.PikaStatsMod
import me.chetan.pikastats.map.RGB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager

class HeightDisplayGUI: Gui() {
    init {
        val configResXRatio=PikaStatsMod.config.eachOrder["heightx"].asFloat/PikaStatsMod.config.eachOrder["resx"].asFloat
        val configResYRatio=PikaStatsMod.config.eachOrder["heighty"].asFloat/PikaStatsMod.config.eachOrder["resy"].asFloat

        val fontObj=Minecraft.getMinecraft().fontRendererObj
        val xPadding=7
        val yPadding=7
        var xExtra=0
        var yExtra=0
        val x= (Minecraft.getMinecraft().displayWidth*configResXRatio).toInt()
        val y= (Minecraft.getMinecraft().displayHeight*configResYRatio).toInt() / 2
        GlStateManager.enableAlpha()
        rgbEffect.update()
        val linesColored= mapOf(
            "PikaStatsMod" to rgbEffect.getHex(),
            "Map Name:- $mapName" to 0x00FF00,
            "Build Height:- $mapHeight" to 0x00FF00
        )
        for (line in linesColored){
            drawString(fontObj,line.key,x+xPadding,y+yPadding+yExtra,line.value)
            if (fontObj.getStringWidth(line.key)>xExtra){
                xExtra=fontObj.getStringWidth(line.key)
            }
            yExtra += fontObj.FONT_HEIGHT+yPadding
        }
        drawRect(x,y,x+xExtra+xPadding, y+yExtra+yPadding, 0x60000000)
        box[0]=x
        box[1]=y
        box[2]=x+xExtra+xPadding
        box[3]=y+yExtra+yPadding
    }

    companion object{
        var mapName=""
        var mapHeight=-1
        private var rgbEffect= RGB()
        var box= mutableListOf(0,0,0,0)
    }

}