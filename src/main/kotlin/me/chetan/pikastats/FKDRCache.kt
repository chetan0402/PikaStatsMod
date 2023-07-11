package me.chetan.pikastats

class FKDRCache {
    private var bwFKDRCache= mutableMapOf<String,Float>()
    private var swFKDRCache= mutableMapOf<String,Float>()
    private var threadList= mutableListOf<requestNode>()

    fun getFKDRCache(username: String):String{
        val gamemode=ScoreboardTracker.gamemodeForAccess.lowercase().replace("\\s".toRegex(),"")
        var fkdr=when(gamemode){
            "bedwars" -> bwFKDRCache[username]
            "skywars" -> swFKDRCache[username]
            else -> return ""
        }
        if(fkdr==null) {
            val requestThread=requestNode(username, gamemode)
            threadList.add(requestThread)
            requestThread.start()
            when(gamemode){
                "bedwars" -> bwFKDRCache[username]=-1f
                "skywars" -> swFKDRCache[username]=-1f
            }
            return " | "
        }else if(fkdr==-1f){
            var iter=threadList.listIterator()
            while(iter.hasNext()){
                val thread=iter.next()
                if (thread.getUsername()==username && thread.getGamemode()==gamemode){
                    fkdr=thread.getValue()
                    if (fkdr==-1f) return " | W"
                    when(gamemode){
                        "bedwars" -> bwFKDRCache[username]=fkdr
                        "skywars" -> swFKDRCache[username]=fkdr
                    }
                    iter.remove()
                }
            }
        }
        return " | $fkdr"
    }

    class requestNode(username: String, gamemode: String): Thread() {
        private var username=""
        private var gamemode=""
        private var fkdr=-1f

        init {
            this.username=username
            this.gamemode=gamemode
        }

        override fun run() {
            this.fkdr=PikaAPI.getFkdr(PikaAPI.minigames(username,"total","ALL_MODES",gamemode))
        }

        fun getValue():Float{
            return this.fkdr
        }

        fun getUsername():String{
            return this.username
        }

        fun getGamemode():String{
            return this.gamemode
        }
    }
}