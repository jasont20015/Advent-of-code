import kotlin.math.abs


fun main() {
    var input = readInput("Day10_test")

    fun gridPositionToCardinalDirection(position: Position): List<cardinalDirection> =
            when(input[position.first][position.second]) {
                '|' -> listOf(cardinalDirection.NORTH, cardinalDirection.SOUTH)
                '-' -> listOf(cardinalDirection.EAST, cardinalDirection.WEST)
                'L' -> listOf(cardinalDirection.NORTH, cardinalDirection.EAST)
                'J' -> listOf(cardinalDirection.NORTH, cardinalDirection.WEST)
                '7' -> listOf(cardinalDirection.SOUTH, cardinalDirection.WEST)
                'F' -> listOf(cardinalDirection.SOUTH, cardinalDirection.EAST)
                'S' -> listOf(cardinalDirection.NORTH, cardinalDirection.SOUTH, cardinalDirection.EAST, cardinalDirection.WEST)
                '.' -> listOf()
                else -> listOf()
            }
    fun getNext(pos: Position, dir: cardinalDirection): Pair<Position, cardinalDirection>? {
        val nextPos = pos + dir.toPosition()
        val nextDirs = gridPositionToCardinalDirection(nextPos)
        if (dir.opposite() !in nextDirs) return null
        return nextPos to nextDirs.minus(dir.opposite()).first()
    }
    fun getLoop(): List<Position>{
        val x = input.indexOfFirst { 'S' in it }
        val startPosition = x to input[x].indexOf('S')
        val tmpLoop = mutableListOf<Position>()
        for (startDir in cardinalDirection.entries) {
            tmpLoop.clear()
            tmpLoop.add(startPosition)
            val start = getNext(startPosition, startDir) ?: continue
            var curPos = start.first
            var curDir = start.second
            while (input[curPos.first][curPos.second] != 'S') {
                tmpLoop.add(curPos)
                val (nextPos, nextDir) = getNext(curPos, curDir) ?: break
                curPos = nextPos
                curDir = nextDir
            }
            if (input[curPos.first][curPos.second] == 'S') break
        }
        return tmpLoop
    }

    fun part1(): Int {
        val loop = getLoop()

        return loop.size/2
    }
    fun part2(): Int {
        val loop = getLoop()
        return (1 ..< input.size - 1).sumOf { x ->
            val index = input[x].indices.filter { y ->
                val index1 = loop.indexOf(x to y)
                val index2 = loop.indexOf(x + 1 to y)
                index1 != -1 && index2 != -1 && (abs(index1 - index2) == 1 || index1 in listOf(0, loop.lastIndex) && index2 in listOf(0, loop.lastIndex))
            }
            (index.indices step 2).sumOf { i ->
                (index[i] .. index[i + 1]).count { y -> x to y !in loop }
            }
        }
    }
    // test if implementation meets criteria from the description, like:
    check(part2() == 4)


    input = readInput("Day10")
    println(part1())
    println(part2())

}
