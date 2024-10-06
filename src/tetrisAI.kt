import kotlin.math.abs

data class Move(val offset: Int = -1, val rotations: Int = -1, val hold: Boolean = false, val score:Float = 0f)

class TetrisAI{
    fun findMove(board: Board, queue:Queue, depth: Int,player:Individual):Move{
        if (depth == 0){
            return Move()
        }
        var best = -Float.MAX_VALUE
        var ans = Move()
        for (offset in 0..9){
            for (rotation in 0..3){
                run{
                    var boardCopy = board.deepCopy()
                    var queueCopy = queue.deepCopy()
                    var score = tryMove(offset,rotation,boardCopy,queueCopy,player)
                    if (score != null) {
                        queueCopy.queue.removeFirst()
                        boardCopy.clearLines()
                        var next = findMove(boardCopy,queueCopy, depth - 1,player)
                        score += next.score
                        if (score > best) {
                            best = score; ans = Move(offset, rotation, false, score)
                        }
                    }
                }
                run{
                    var boardCopy = board.deepCopy()
                    var queueCopy = queue.deepCopy().apply{hold(this)}
                    var score = tryMove(offset,rotation,boardCopy,queueCopy,player)
                    if (score != null) {
                        queueCopy.queue.removeFirst()
                        boardCopy.clearLines()
                        var next = findMove(boardCopy,queueCopy, depth - 1,player)
                        score += next.score
                        if (score > best) {
                            best = score; ans = Move(offset, rotation, true, score)
                        }
                    }
                }
            }
        }
        return ans
    }

    fun hold(queue: Queue) {
        if (queue.hold == "") {
            queue.hold = queue.current
            queue.current = queue.queue.first()
            queue.queue.removeFirst()
        } else {
            queue.hold = queue.current.also {
                queue.current = queue.hold
            }
        }
    }

    fun tryMove(offset: Int, rotation: Int,board:Board,queue:Queue,player:Individual):Float?{
        var piece = Tetromino(queue.current,offset,rotation,20,board)
        if (!piece.isNotColliding()){
            return null
        }
        else{
            piece.boardPlace()
            return moveScore(board,queue,piece,player)
        }
    }

    fun moveScore(board: Board, queue: Queue, piece: Tetromino, player:Individual):Float{
        var score = calculateScore(board,queue,player)
        var blocksOnRight = 0
        for (j in 0..19){
            if (board.grid[j][9] != "#"){
                blocksOnRight++
            }
        }
        if (queue.current == "I" && board.fullLines() == 4){
            score += player.params[0]
        }
        else{
            score += if (board.getHighestArray().max()>=12){
                board.fullLines() * player.params[1]
            } else{
                board.fullLines() * player.params[2] +
                        blocksOnRight * player.params[3]
            }
        }

        return score
    }

    fun calculateScore(board: Board, queue: Queue, player:Individual):Float{
        val boardHeights = board.getHighestArray()
        val minHeight = boardHeights.dropLast(1).min()
        val maxHeight = boardHeights.dropLast(1).max()
        val heightDiff = maxHeight - minHeight
        var holes = 0
        for (i in 0 until 10){
            for (j in 19 downTo 0){
                if (board.grid[j][i] == "#" && j < boardHeights[i] - 1){
                    holes++
                }
            }
        }
        val jags = boardHeights.zipWithNext{a,b -> abs(a-b)}.count{it==1}
        //if queue has t then this is ok

        var wells = 0
        for (i in 0 .. 6){
            if (boardHeights[i] - boardHeights[i+1] == 2 && boardHeights[i+2] - boardHeights[i+1]==2){
                wells++
            }
        }

        val spikes = boardHeights.dropLast(1).zipWithNext{a,b->abs(a-b)}.count{it>=3}



        return (
                maxHeight * player.params[4] +
                heightDiff * player.params[5] +
                holes * player.params[6] +
                (if (jags > 5 && !queue.queue.take(2).contains("T") && queue.hold != "T") jags * player.params[7] else player.params[8])+
                        (if ((queue.queue.take(3).intersect(setOf("J","I","L")).isEmpty()) && !setOf("J","I","L").contains(queue.hold)) wells*wells*player.params[9] else player.params[10]) +
                spikes * player.params[11]
                )
    }
}