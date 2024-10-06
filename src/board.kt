data class Board(var grid: MutableList<MutableList<String>> = MutableList(20){MutableList(10){"#" }}){
    fun print(){
        println(grid.reversed().joinToString("\n"))
    }

    fun deepCopy(): Board{
        return Board(grid.map{it.toMutableList()}.toMutableList())
    }

    fun fullLines():Int{
        var ctr = 0
        grid.forEach{row ->
            if (row.none{it=="#"})ctr++
        }
        return ctr
    }

    fun clearLines(){
        for (i in 18 downTo 0){
            if (grid[i].none{it=="#"}){
                for (j in i..19){
                    if (j == 19){
                        grid[j] = MutableList(10){"#"}
                    }
                    else grid[j] = grid[j+1]
                }
            }
        }
    }

    fun getHighestArray():MutableList<Int>{
        val ans = MutableList(10){0}
        for (x in 0 until 10){
            for (y in 19 downTo 0){
                if (grid[y][x] != "#"){
                    ans[x]=y+1
                    break
                }
            }
        }
        return ans
    }

    fun gameOver():Boolean{
        if (getHighestArray().max()>=19){
            return true
        }
        else
            return false
    }
}