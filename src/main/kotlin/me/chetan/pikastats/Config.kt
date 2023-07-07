package me.chetan.pikastats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.minecraft.client.Minecraft
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*

class Config {
    private var dataDir: File = File(Minecraft.getMinecraft().mcDataDir.absolutePath + "/config")
    private var orderList: File = File(dataDir.absolutePath + "/PikaStatOrderList.DoNotDelete")
    private var eachOrder = JsonObject()
    val bwOptions = listOf("Final kills","Kills","Highest winstreak reached","Beds destroyed","Wins","Losses","Games played","Deaths","Bow kills","Arrows shot","Arrows hit","Melee kills","Void kills")
    val swOptions = listOf("Kills","Wins","Games played","Highest winstreak reached","Losses","Deaths","Bow kills","Arrows shot","Arrows hit","Melee kills","Void kills")
    val upracOptions= listOf("Highest winstreak reached","Hits dealt","Losses","Bow kills","Kills","Void kills","Games played","Wins","Hits taken")
    init {
        if (!orderList.exists()) {
            try {
                init("fresh")
            } catch (e: IOException) {
                PikaStatsMod.logger.error("Error in creating a new orderList file.", e)
            }
        } else {
            var version: String? = null
            var typeInit = "fresh"
            try {
                version = getVersion()
            } catch (e: IOException) {
                PikaStatsMod.logger.error("Error reading file for version.", e)
                e.printStackTrace()
            }
            if (version == null) {
                typeInit = "fresh"
            } else if (version == "1.0.0") {
                typeInit = "1.0.0"
            }
            try {
                init(typeInit)
            } catch (e: IOException) {
                PikaStatsMod.logger.error("Error in creating a new orderList file.", e)
                e.printStackTrace()
            }
        }
    }

    private fun init(type: String) {
        try{
            when(type){
                "fresh" -> {
                    orderList.delete()
                    orderList.createNewFile()
                    val bwOrder = listOf("Final kills","Kills","Highest winstreak reached","Beds destroyed","Wins","Losses","Games played","Deaths")
                    val swOrder = listOf("Kills","Wins","Games played","Highest winstreak reached","Losses","Deaths")
                    val upracOrder = listOf("Wins","Losses","Games played","Kills","Highest winstreak reached")
                    eachOrder.remove("bw")
                    eachOrder.remove("sw")
                    eachOrder.remove("uprac")
                    eachOrder.addProperty("bw", bwOrder.toString())
                    eachOrder.addProperty("sw", swOrder.toString())
                    eachOrder.addProperty("uprac",upracOrder.toString())
                }
                "1.0.0" -> {
                    val upracOrder = listOf("Wins","Losses","Games played","Kills","Highest winstreak reached")
                    eachOrder.remove("uprac")
                    eachOrder.addProperty("uprac",upracOrder.toString())
                }
            }
            eachOrder.remove("version")
            eachOrder.addProperty("version", "1.0.1")
            writeToFile()
        } catch (e:Exception){
            PikaAPI.error("Error in creating/migrating config file.")
            e.printStackTrace()
        }
    }

    fun getGameConfig(gamemode: String): MutableList<String> {
        try {
            val mainString = String(Files.readAllBytes(orderList.toPath()))
            val parser = JsonParser()
            eachOrder = parser.parse(mainString).asJsonObject
            var orderString = eachOrder[gamemode].asString
            orderString = orderString.substring(1, orderString.length - 1)
            val list = orderString.split(", ").toMutableList()
            var i=0
            for (ele in list) {
                if (ele[0] == ' ') {
                    list[i] = ele.substring(1)
                }
                i += 1
            }
            return list
        } catch (e: IOException) {
            PikaAPI.error("Couldn't read the config file.")
        } catch (e: JsonSyntaxException) {
            PikaAPI.error("Corrupted or old config.")
            init("fresh")
        } catch (e: Exception){
            PikaAPI.error("Unknown error occured. Contact chetan0402 on github/pika-forums")
            e.printStackTrace()
        }
        return LinkedList()
    }

    fun writeToFile() {
        try {
            Files.write(orderList.toPath(), eachOrder.toString().toByteArray())
        } catch (e: IOException) {
            PikaAPI.error("Couldn't write to config file.")
        }
    }

    fun updateOrder(gamemode: String, from: String, to: Int): Boolean {
        val list = getGameConfig(gamemode)
        if (to >= list.size) {
            PikaAPI.error("Not a valid Integer input...")
            return false
        }
        val fromIndex = list.indexOf(from)
        if (fromIndex == -1) {
            PikaAPI.error("Not a valid Field...")
            return false
        }
        val toField = list[to]
        list[fromIndex] = toField
        list[to] = from
        eachOrder.remove(gamemode)
        eachOrder.addProperty(gamemode, list.toString())
        writeToFile()
        return true
    }

    fun updateOrder(gamemode: String, from: String, to: String): Boolean {
        try {
            return updateOrder(gamemode, from, to.toInt())
        } catch (e: NumberFormatException) {
            PikaAPI.error("$to is not an integer")
            e.printStackTrace()
        }
        return false
    }

    fun toggleStat(gamemode: String, stat: String) {
        val list = getGameConfig(gamemode)
        if (list.contains(stat)) {
            list.remove(stat)
        } else {
            list.add(stat)
        }
        eachOrder.remove(gamemode)
        eachOrder.addProperty(gamemode, list.toString())
        writeToFile()
    }

    fun getVersion(): String? {
        try {
            val mainString = String(Files.readAllBytes(orderList.toPath()))
            val parser = JsonParser()
            eachOrder = parser.parse(mainString).asJsonObject
            return eachOrder["version"].toString()
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
}