package me.chetan.pikastats

class UserCache {
    private var bwCache= mutableMapOf<String,Float>()
    private var swCache= mutableMapOf<String,Float>()
    private var threadList= mutableListOf<requestNode>()

    fun getCache(username: String):String{
        if (PikaAPI.tempDisable) return ""
        val gamemode=ScoreboardTracker.gamemodeForAccess.lowercase().replace("\\s".toRegex(),"")
        val tabField=PikaStatsMod.config.eachOrder.get("tab").asString.replace("\"","")
        if (tabField=="disable") return ""
        var stats=when(gamemode){
            "bedwars" -> bwCache[username]
            "skywars" -> swCache[username]
            else -> return ""
        }
        if(stats==null) {
            val requestThread=requestNode(username, gamemode, tabField)
            threadList.add(requestThread)
            requestThread.start()
            when(gamemode){
                "bedwars" -> bwCache[username]=-1f
                "skywars" -> swCache[username]=-1f
            }
            return " | "
        }else if(stats==-1f){
            var iter=threadList.listIterator()
            while(iter.hasNext()){
                val thread=iter.next()
                if (thread.getUsername()==username && thread.getGamemode()==gamemode){
                    stats=thread.getValue()
                    if (stats==-1f) return " | W"
                    when(gamemode){
                        "bedwars" -> bwCache[username]=stats
                        "skywars" -> swCache[username]=stats
                    }
                    iter.remove()
                }
            }
        }
        return " | $stats"
    }

    fun reset(){
        bwCache=mutableMapOf()
        swCache=mutableMapOf()
        try{
            val iter=threadList.listIterator()
            while (iter.hasNext()){
                val thread=iter.next()
                thread.interrupt()
                iter.remove()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        threadList= mutableListOf()
    }

    class requestNode(username: String, gamemode: String, stat:String): Thread() {
        private var username=""
        private var gamemode=""
        private var field=""
        private var stats=-1f

        init {
            this.username=username
            this.gamemode=gamemode
            this.field=stat
        }

        override fun run() {
            val userInfo=PikaAPI.minigames(username,"total","ALL_MODES",gamemode)
            when(field){
                "FKDR" -> this.stats=PikaAPI.getFkdr(userInfo)
                "WLR" -> this.stats=PikaAPI.getWLR(userInfo)
                else -> {
                    val fieldValue:Float?= PikaAPI.getField(userInfo,field)?.get("value")?.asFloat
                    if (fieldValue != null) {
                        this.stats=fieldValue
                    }else{
                        this.stats=0f
                    }
                }
            }
        }

        fun getValue():Float{
            return this.stats
        }

        fun getUsername():String{
            return this.username
        }

        fun getGamemode():String{
            return this.gamemode
        }
    }
}