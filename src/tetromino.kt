import kotlin.math.min

data class Tetromino(var piece: String, var offset: Int, var rotations: Int, var y: Int = 20, var boardPiece:Board = board) {
    fun drop() {
        y--
    }

    fun getMatrix(): List<List<String>> {
        return when (piece) {
            "O" -> listOf(
                listOf("O", "O"), listOf("O", "O")
            )

            "S" -> when (rotations) {
                0, 2 -> listOf(
                    listOf("", "S", "S"), listOf("S", "S")
                )

                1, 3 -> listOf(
                    listOf("S"), listOf("S", "S"), listOf("", "S")
                )

                else -> error("teromino")
            }

            "Z" -> when (rotations) {
                0, 2 -> listOf(
                    listOf("Z", "Z"), listOf("", "Z", "Z")
                )

                1, 3 -> listOf(
                    listOf("", "Z"), listOf("Z", "Z"), listOf("Z")
                )

                else -> error("teromino")
            }

            "I" -> when (rotations) {
                0, 2 -> listOf(
                    listOf("I", "I", "I", "I")
                )

                1, 3 -> listOf(
                    listOf("I"), listOf("I"), listOf("I"), listOf("I")
                )

                else -> error("teromino")
            }

            "T" -> when (rotations) {
                0 -> listOf(
                    listOf("", "T"), listOf("T", "T", "T")
                )

                1 -> listOf(
                    listOf("T"), listOf("T", "T"), listOf("T")
                )

                2 -> listOf(
                    listOf("T", "T", "T"), listOf("", "T")
                )

                3 -> listOf(
                    listOf("", "T"), listOf("T", "T"), listOf("", "T")
                )

                else -> error("terino")
            }

            "L" -> when (rotations) {
                0 -> listOf(
                    listOf("", "", "L"),
                    listOf("L", "L", "L")
                )

                1 -> listOf(
                    listOf("L"),
                    listOf("L"),
                    listOf("L", "L")
                )

                2 -> listOf(
                    listOf("L", "L", "L"),
                    listOf("L")
                )

                3 -> listOf(
                    listOf("L", "L"),
                    listOf("", "L"),
                    listOf("", "L")
                )

                else -> error("terino")
            }

            "J" -> when (rotations) {
                0 -> listOf(
                    listOf("J"),
                    listOf("J", "J", "J")
                )

                1 -> listOf(
                    listOf("J", "J"),
                    listOf("J"),
                    listOf("J")
                )

                2 -> listOf(
                    listOf("J", "J", "J"),
                    listOf("", "", "J")
                )

                3 -> listOf(
                    listOf("", "J"),
                    listOf("", "J"),
                    listOf("J", "J")
                )

                else -> error("terino")
            }

            else -> error("terino")
        }
    }

    fun getOset():Int{
        val rots = when (piece){
            "I" -> listOf(3,5,3,4)
            "J" -> listOf(3,4,3,3)
            "L" -> listOf(3,4,3,3)
            "O" -> listOf(4,4,4,4)
            "S" -> listOf(3,4,3,3)
            "T" -> listOf (3,4,3,3)
            "Z" -> listOf(3,4,3,3)
            else -> error("piece not found")
        }
        return rots[rotations]
    }

    fun getCoords(): List<Point> {
        val mat = getMatrix()
        val points = mutableListOf<Point>()
        for (i in 0..mat.lastIndex) {
            for (j in 0..mat[i].lastIndex) {
                if (mat[i][j] == piece) {
                    points.add(Point(j + offset,mat.lastIndex-i + y ))
                }
            }
        }
        return points
    }

    fun isNotColliding(): Boolean {
        val points = getCoords()
        for (i in points) {
            if (i.x >= 10){
                return false
            }
            if (i.y < 0){
                return false
            }
            if (boardPiece.grid[min(i.y,19)][i.x] != "#") {
                return false
            }
        }
        return true
    }

    fun canDrop(): Boolean {
        return this.copy(y = y - 1).isNotColliding()
    }

    fun place(){
        for (coord in getCoords()){
            if (coord.x in 0..9 && coord.y >= 0 && coord.y < 20){
                boardPiece.grid[coord.y][coord.x] = piece
            }
        }
    }

    fun boardPlace(){
        while (canDrop()){
            drop()
        }
        place()
    }
}