//import java.awt.Robot
import kotlin.math.*
import kotlin.random.Random

//125% jstris!!

val board = Board()
val queue = Queue()
//var robot = Robot()
val tetrisAI = TetrisAI()
val player = Individual()

/*fun game(){
    robot.focus()
    robot.restart()
    Thread.sleep(3000)
    while (true){
        Thread.sleep(10)
        queue.read()
        if (queue.current == "") error("queue is empty!")
        var move = tetrisAI.findMove(board,queue,2)
        if (move.hold) tetrisAI.hold(queue)
        val mino = Tetromino(queue.current,move.offset,move.rotations,20,board)
        mino.boardPlace()
        board.clearLines()
        robot.place(move,mino)
    }
}*/

fun main(){
    //game()
    val GeneticAlgorithm = geneticAlgo(mutableListOf())
    GeneticAlgorithm.evolve()
}