
fun main() {
    fun Point2D.validNeighbours(grid: List<String>) = neighbours()
            .filter { it.y in grid.indices && it.x in grid.first().indices && grid[it.y][it.x] in ".<>^v" }

    fun findMax(
            current: Point2D,
            visited: Array<BooleanArray>,
            distance: Int = 0,
            getNeighbours: (Point2D) -> List<Pair<Point2D, Int>>,
            end: Point2D
    ): Int {
        if (current == end) {
            return distance
        }

        visited[current.y][current.x] = true
        val max = getNeighbours(current)
                .filter { (neighbour, _) -> !visited[neighbour.y][neighbour.x] }
                .maxOfOrNull { (neighbour, weight) ->
                    findMax(neighbour, visited, distance + weight, getNeighbours, end)
                }
        visited[current.y][current.x] = false

        return max ?: 0
    }


    fun part1(input: List<String>): Long {
        val start = Point2D(1,0)
        val end = Point2D(input.first().lastIndex - 1, input.lastIndex)
        val visited = Array(input.size) { BooleanArray(input.first().length) }
        return findMax(
                current = start,
                visited = visited,
                getNeighbours = { current ->
                    when (input[current.y][current.x]) {
                        '>' -> listOf(current.copy(x = current.x + 1) to 1)
                        '<' -> listOf(current.copy(x = current.x - 1) to 1)
                        'v' -> listOf(current.copy(y = current.y + 1) to 1)
                        '^' -> listOf(current.copy(y = current.y - 1) to 1)
                        else -> {
                            current.validNeighbours(input).map { it to 1 }
                        }
                    }
                },
                end = end
        ).toLong()
    }
    fun part2(input: List<String>): Long {
        val start = Point2D(1,0)
        val end = Point2D(input.first().lastIndex - 1, input.lastIndex)
        val visited = Array(input.size) { BooleanArray(input.first().length) }

        val junctions = mutableMapOf(
                start to mutableListOf<Pair<Point2D, Int>>(),
                end to mutableListOf(),
        )

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '.') {
                    val point = Point2D(col, row)
                    if (point.validNeighbours(input).size > 2) {
                        junctions[point] = mutableListOf()
                    }
                }
            }
        }
        for (junction in junctions.keys) {
            var current = setOf(junction)
            val visitedInLoop = mutableSetOf(junction)
            var distance = 0

            while (current.isNotEmpty()) {
                ++distance
                current = buildSet {
                    for (c in current) {
                        c.validNeighbours(input).filter { it !in visitedInLoop }.forEach { n ->
                            if (n in junctions) {
                                junctions.getValue(junction).add(n to distance)
                            } else {
                                add(n)
                                visitedInLoop.add(n)
                            }
                        }
                    }
                }
            }
        }

        return findMax(
                current = start,
                visited = visited,
                getNeighbours = { current -> junctions.getValue(current) },
                end = end
        ).toLong()
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94L)
    check(part2(testInput) == 154L)
    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
