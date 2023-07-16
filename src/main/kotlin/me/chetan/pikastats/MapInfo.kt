package me.chetan.pikastats

import com.google.gson.JsonObject

class MapInfo {
    private var mapResponse:JsonObject= JsonObject()

    init {
        update()
    }

    fun update(){
        val map=PikaAPI.getJson("https://raw.githubusercontent.com/chetan0402/PikaStatsChecker/master/map.json","github")
        if (map==null){
            this.mapResponse=JsonObject()
        }else{
            this.mapResponse=map
        }
    }

    fun getMapResponse():JsonObject?{
        return mapResponse.asJsonObject
    }

    fun getHeight(mapName:String):Int{
        return mapResponse.get(mapName)?.asInt ?: -1
    }
}