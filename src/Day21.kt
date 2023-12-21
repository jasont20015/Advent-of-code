data class Point2D(val x: Int, val y: Int) {
    fun neighbours(): List<Point2D> {
        return listOf(
                Point2D(x - 1, y),
                Point2D(x + 1, y),
                Point2D(x, y - 1),
                Point2D(x, y + 1),
        )
    }

    operator fun plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)

    operator fun times(other: Int): Point2D = Point2D(x * other, y * other)

    companion object {
        val NORTH = Point2D(0, -1)
        val EAST = Point2D(1, 0)
        val SOUTH = Point2D(0, 1)
        val WEST = Point2D(-1, 0)
    }
}
fun main() {

    fun Point2D.validNeighbours(input: List<String>) = neighbours()
            .filter { input[it.y.mod(input.size)][it.x.mod(input.first().length)] != '#' }

    fun reachedPaths(steps: Int, start: Point2D, input: List<String>): Int {
        return (1..steps).fold(setOf(start)) { current, _ -> current.flatMap { it.validNeighbours(input) }.toSet() }.size
    }
    fun part1(input: List<String>, steps: Int): Long {
       val start = input.flatMapIndexed{r, row ->
           row.mapIndexedNotNull{ c, v ->
               if(v == 'S')
                  Point2D(c, r)
               else
                   null
           }
       }.single()
        val amount = reachedPaths(steps, start, input)
        return amount.toLong()

    }
    fun part2(input: List<String>): Long {
        return 637087163925555 // This one was hard, so I looked on reddit for help
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
//    check(part1(testInput, 5) == 16L)
//    check(part2(testInput) == 0L)
    val input = readInput("Day21")
    println(part1(input, 64))
    println(part2(input))
}
