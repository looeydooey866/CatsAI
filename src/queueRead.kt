import java.awt.Color
import java.awt.Robot

data class Point(var x: Int = 0, var y: Int = 0)

data class Queue(var hold: String = "", var queue: MutableList<String> = mutableListOf(), var current: String = "") {
    private val holdCoordinate = Point(105, 300)
    private val holdCoordinate2 = Point(105, 306)
    private val queueCoordinateTop = Point(584, 300)
    private val queueCoordinateBottom = Point(584, 306)
    private val currentCoordinate = Point(329,259)
    private val currentCoordinateDistance = 30
    private val queueCoordinateDistance = 90
    private val colorPieceMap = mapOf(
        Color(215, 15, 55) to "Z",
        Color(227, 91, 2) to "L",
        Color(227, 159, 2) to "O",
        Color(89, 177, 1) to "S",
        Color(15, 155, 215) to "I",
        Color(33, 65, 198) to "J",
        Color(175, 41, 138) to "T"
    )

    fun deepCopy():Queue{
        return Queue(hold, queue.toMutableList(), current)
    }

    private fun colorToPiece(color: Color): String {
        return colorPieceMap.getOrDefault(color, "")
    }

    private fun getColor(point: Point): Color {
        val robot = Robot()
        return robot.getPixelColor(point.x, point.y)
    }

    fun read() {
        hold = colorToPiece(getColor(holdCoordinate))
            .takeIf { it.isNotEmpty() } ?: colorToPiece(getColor(holdCoordinate2))
            .takeIf { it.isNotEmpty() } ?: ""

        queue.clear()
        queue = MutableList(5) { i ->
            val coord = queueCoordinateTop.copy(y = queueCoordinateTop.y + queueCoordinateDistance * i)
            val coord2 = queueCoordinateBottom.copy(y = queueCoordinateBottom.y + queueCoordinateDistance * i)
            colorToPiece(getColor(coord)).takeIf { it.isNotEmpty() }
                ?: colorToPiece(getColor(coord2)).takeIf { it.isNotEmpty() } ?: ""
        }

        current = colorToPiece(getColor(currentCoordinate))
            .takeIf { it.isNotEmpty() } ?:
                colorToPiece(getColor(currentCoordinate.copy(y = currentCoordinate.y + currentCoordinateDistance)))
                    .takeIf { it.isNotEmpty() } ?:
                colorToPiece(getColor(currentCoordinate.copy(y = currentCoordinate.y + currentCoordinateDistance * 2))) ?: "HEREISTHEPROBLEM"
    }

    fun gen(){
        if (queue.size <= 7){
            var bag = mutableListOf("L","J","T","I","S","Z","O")
            bag.shuffle()
            queue.addAll(bag)
        }
        if (current.isEmpty()){
            var bag = mutableListOf("L","J","T","I","S","Z","O")
            bag.shuffle()
            current = bag[0]
        }
    }

    fun pop(){
        current = queue.first()
        queue.removeFirst()
    }
}


