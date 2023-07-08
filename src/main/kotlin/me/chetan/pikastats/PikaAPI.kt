package me.chetan.pikastats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.MalformedURLException
import java.net.URL
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object PikaAPI {
    val right_point_tri = "\u25B6 "
    fun minigames(playerName: String, interval: String, mode: String, type: String): JsonObject? {
        var control = true
        var toReturn: JsonObject? = null
        var i = 0
        while (control) {
            toReturn = getJson(
                "https://stats.pika-network.net/api/profile/$playerName/leaderboard?type=$type&interval=$interval&mode=$mode",
                "pika"
            )
            if (toReturn != null) {
                control = false
            }
            i += 1
            if (i == 5) {
                control = false
            }
        }
        return toReturn
    }

    private fun getJson(urlString: String, site: String): JsonObject? {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate")
            connection.addRequestProperty("Pragma", "no-cache")
            connection.addRequestProperty("Expires", "0")
            connection.addRequestProperty("User-Agent", "Java")
            if (site == "pika") {
                val certificate = CertificateFactory.getInstance("X.509").generateCertificate(
                    PikaStatsMod::class.java.classLoader.getResourceAsStream("R3.crt")
                )
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null, null)
                keyStore.setCertificateEntry("R3", certificate)
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(keyStore)
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustManagerFactory.trustManagers, null)
                connection.sslSocketFactory = sslContext.socketFactory
            }
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            val response = StringBuilder()
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            connection.disconnect()
            val parser = JsonParser()
            val jsonElement = parser.parse(response.toString())
            return jsonElement.asJsonObject
        } catch (e: MalformedURLException) {
            error("Notify this error with logs to chetan0402 on pika forums")
            e.printStackTrace()
        } catch (e: IOException) {
            error("Network error.")
            e.printStackTrace()
        } catch (err: IllegalStateException) {
            error("Weird response from website.")
            err.printStackTrace()
        } catch (e: CertificateException) {
            error("Certificate creation failed.")
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            error("Certificate creation failed.")
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            error("Certificate creation failed.")
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            error("Certificate creation failed.")
            e.printStackTrace()
        } catch (e:Exception){
            error("Unknown error.. Contact chetan0402 on discord/pika-forums related with logs")
            e.printStackTrace()
        }
        return null
    }

    fun error(toTell: String) {
        PikaStatsMod.logger.error(toTell)
        if (Minecraft.getMinecraft().theWorld != null) {
            sendText(EnumChatFormatting.YELLOW.toString() + "PikaStatsMod | " + EnumChatFormatting.RED + toTell)
        }
    }

    fun sendText(text: String) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(text))
    }

    fun help() {
        sendText("")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.DARK_RED + " Use /pikastats <player_name> <gamemode> [weekly/monthly/lifetime] [all/solo/dou/trio/quad]")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.RED + " NOTE:- <> means mandatory and [] means optional")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.GREEN + "e.g. /pikastats Chetan0402 bw")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.GREEN + "e.g. /pikastats Chetan0402 sw weekly")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.GREEN + "e.g. /pikastats Chetan0402 bw monthly duo")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.GREEN + "e.g. /pikastats Chetan0402 uprac lifetime uhc")
        sendText(EnumChatFormatting.YELLOW.toString() + " PikaStatsMod | " + EnumChatFormatting.GREEN + "e.g. /pikastats Chetan0402 rprac lifetime sumo")
        sendText("")
    }

    private fun getField(requested: JsonObject?, field: String): JsonObject? {
        try {
            if (requested != null) {
                var fieldVar = requested[field].asJsonObject["entries"].toString()
                if (fieldVar.equals("null", ignoreCase = true)) {
                    val toReturn = JsonObject()
                    toReturn.addProperty("value", 0)
                    toReturn.addProperty("place", 0)
                    return toReturn
                }
                fieldVar = fieldVar.substring(1, fieldVar.length - 1)
                val parser = JsonParser()
                return parser.parse(fieldVar).asJsonObject
            }
            return null
        } catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    fun sendTextClickHover(
        text: String,
        ha: HoverEvent.Action,
        htext: String,
        ca: ClickEvent.Action,
        cmd: String
    ) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
            ChatComponentText(text).setChatStyle(
                ChatStyle().setChatHoverEvent(
                    HoverEvent(ha, ChatComponentText(htext))
                ).setChatClickEvent(ClickEvent(ca, cmd))
            )
        )
    }

    private fun sendStatInfo(field: String, value: String, rank: String) {
        sendText((""+EnumChatFormatting.YELLOW + right_point_tri + EnumChatFormatting.BOLD) + field + ": " + EnumChatFormatting.RESET + EnumChatFormatting.AQUA + value + EnumChatFormatting.RESET + EnumChatFormatting.DARK_AQUA + " #" + rank)
    }

    fun iterateListSend(main: JsonObject?, gamemode: String) {
        if (main==null) {return}
        sendText("")
        val allowedFkdr=PikaStatsMod.config.eachOrder["fkdr"].toString().toBoolean()
        if(allowedFkdr) {
            var finalKill = getField(main, "Final kills")
            if(finalKill==null){
                finalKill= getField(main,"Kills")
            }
            val finalDeath = getField(main, "Losses")
            if (finalKill != null && finalDeath != null) {
                if(finalKill["value"].asString.toInt()!=0 && finalDeath["value"].asString.toInt()!=0){
                    val fkdr = finalKill["value"].asString.toFloat() / finalDeath["value"].asString.toFloat()
                    sendStatInfo("FKDR", BigDecimal(fkdr.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString(),"0")
                }
            }
        }
        val allowedWlr=PikaStatsMod.config.eachOrder["wlr"].toString().toBoolean()
        if(allowedWlr){
            val wins= getField(main,"Wins")
            var played= getField(main,"Games played")
            if(played==null){
                played= getField(main,"Losses")
            }
            if(wins!=null && played!=null){
                if(wins["value"].asString.toInt()!=0 && played["value"].asString.toInt()!=0){
                    val wlr=wins["value"].asString.toFloat() / played["value"].asString.toFloat()
                    sendStatInfo("WLR", BigDecimal(wlr.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString(),"0")
                }
            }
        }
        sendText("")
        for (field in PikaStatsMod.config.getGameConfig(gamemode)) {
            val fieldData = getField(main, field) ?: continue
            sendStatInfo(field, fieldData["value"].asString, fieldData["place"].asString)
        }
        sendText("")
    }

    fun sendOrderChange(gamemode: String) {
        sendText("")
        sendText((""+EnumChatFormatting.YELLOW + right_point_tri) + " PikaStatsMod ")
        sendText(EnumChatFormatting.GREEN.toString() + " Click the field to change it's position...")
        sendText(EnumChatFormatting.GREEN.toString() + " Put the number of it's new position...")
        sendText("")
        var i = 0
        for (field in PikaStatsMod.config.getGameConfig(gamemode)) {
            sendTextClickHover(
                (""+EnumChatFormatting.YELLOW + right_point_tri + i) + ". " + field + " ",
                HoverEvent.Action.SHOW_TEXT, "Click to change it's order..",
                ClickEvent.Action.SUGGEST_COMMAND, "/statconfig $gamemode \"$field\" $i"
            )
            i += 1
        }
        sendText("")
    }

    fun sendVisiChange(gamemode: String) {
        sendText("")
        sendText(EnumChatFormatting.YELLOW.toString() + " Shown")
        for (stat in PikaStatsMod.config.getGameConfig(gamemode)) {
            sendTextClickHover(
                (""+EnumChatFormatting.GREEN + right_point_tri) + " " + stat,
                HoverEvent.Action.SHOW_TEXT,
                "Click to hide",
                ClickEvent.Action.RUN_COMMAND,
                "/statsconfig visibility $gamemode \"$stat\""
            )
        }
        sendText(EnumChatFormatting.YELLOW.toString() + " Hidden")
        var list = ArrayList<String>()
        when(gamemode.lowercase()){
            "bw" -> list = ArrayList(PikaStatsMod.config.bwOptions)
            "sw" -> list = ArrayList(PikaStatsMod.config.swOptions)
            "uprac" -> list = ArrayList(PikaStatsMod.config.upracOptions)
            "rprac" -> list = ArrayList(PikaStatsMod.config.rpracOptions)
        }
        for (stat in list) {
            if (!PikaStatsMod.config.getGameConfig(gamemode).contains(stat)) {
                sendTextClickHover(
                    (""+EnumChatFormatting.GRAY + right_point_tri) + " " + stat,
                    HoverEvent.Action.SHOW_TEXT,
                    "Click to show",
                    ClickEvent.Action.RUN_COMMAND,
                    "/statsconfig visibility $gamemode \"$stat\""
                )
            }
        }
        sendText("")
    }

    fun updateVars() {
        PikaStatsMod.update_response =
            getJson("https://raw.githubusercontent.com/chetan0402/PikaStatsChecker/master/msg.json", "github")
        if (PikaStatsMod.update_response == null) {
            PikaStatsMod.updated = false
        } else if (PikaStatsMod.update_response["version"].toString().replace("\"", "") == "1.0.2") {
            PikaStatsMod.updated = true
        } else {
            PikaStatsMod.updated = false
        }
    }
}