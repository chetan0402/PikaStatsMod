package me.chetan.pikastats.map

class RGB {
    private val min=0x33
    private val max=0xFF
    private val rate=2
    private var rgb= listOf(PartColor(max,0), PartColor(min,rate), PartColor(min,0))
    private var index=1

    fun update(){
        val prevIndex=when(index){
            0 -> 2
            1 -> 0
            2 -> 1
            else -> null
        }
        val nextIndex=when(index){
            0 -> 1
            1 -> 2
            2 -> 0
            else -> null
        }
        if (prevIndex==null || nextIndex==null) return
        when(rgb[index].part){
            min -> rgb[index].delta=rate
            max-1,max,max+1 -> {
                if (rgb[prevIndex].part<=min){
                    rgb[prevIndex].part=min
                    rgb[prevIndex].delta=0
                    index=nextIndex
                }else{
                    rgb[index].delta=0
                    rgb[prevIndex].delta=-rate
                }
            }
        }

        for(color in rgb){
            color.part+=color.delta
            if (color.part<min){
                color.part=min
            }
        }
    }

    fun getHex():Int{
        return (rgb[0].part shl 16) or (rgb[1].part shl 8) or rgb[2].part
    }

    class PartColor(part:Int, delta:Int){
        var part:Int
        var delta:Int

        init {
            this.part=part
            this.delta=delta
        }
    }
}