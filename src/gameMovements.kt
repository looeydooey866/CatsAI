import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.random.Random

fun Robot.place(move: Move,piece:Tetromino){
    if (move.hold) move("HOLD")
    pause()
    when (move.rotations){
        1 -> move("CW")
        2 -> move("180")
        3 -> move("CCW")
        else -> {}
    }
    val oset = piece.getOset()
    when {
        move.offset < oset -> repeat(oset-move.offset) {move("L");pause()}
        move.offset > oset -> repeat(move.offset-oset) {move("R");pause()}
        else -> {}
    }
    move("HD")
}

fun Robot.hitKey(key:Int){
    keyPress(key)
    keyRelease(key)
}

fun Robot.move(movement:String){
    when (movement){
        "HD" -> hitKey(KeyEvent.VK_SPACE)
        "SD" -> hitKey(KeyEvent.VK_DOWN)
        "L" -> hitKey(KeyEvent.VK_LEFT)
        "R" -> hitKey(KeyEvent.VK_RIGHT)
        "CW" -> hitKey(KeyEvent.VK_UP)
        "CCW" -> hitKey(KeyEvent.VK_S)
        "180" -> hitKey(KeyEvent.VK_A)
        "HOLD" -> hitKey(KeyEvent.VK_D)
    }
}

fun Robot.focus(){
    mouseMove(333,666)
    repeat(2) {
        mousePress(MouseEvent.BUTTON1_DOWN_MASK)
        mouseRelease(MouseEvent.BUTTON1_DOWN_MASK)
    }
}

fun Robot.restart(){
    hitKey(KeyEvent.VK_G)
}

fun pause(){
    Thread.sleep(1)
}