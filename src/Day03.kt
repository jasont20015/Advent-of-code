fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.filter { it.isNotEmpty() }.map { line -> line.toCharArray().toList() }.toList()
        val numbers = mutableListOf<Int>()
        val digits = mutableListOf<Char>()
        val positions = mutableListOf<Pair<Int, Int>>()
        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    digits.add(char)
                    positions.add(Pair(x, y))
                }
                if (!char.isDigit() || line.lastX == x) {
                    val hasSpecialNeighbor = positions.any { position ->
                        grid.adjacentTo(position).any { it.isSpecial() }
                    }
                    if (hasSpecialNeighbor) {
                        numbers.add(digits.joinToString("").toInt())
                    }
                    digits.clear()
                    positions.clear()
                }
            }
        }
        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.filter { it.isNotEmpty() }.map { line -> line.toCharArray().toList() }.toList()
        val numbers = mutableMapOf<Set<Pair<Int, Int>>, Int>()
        val digits = mutableListOf<Char>()
        val positions = mutableListOf<Pair<Int, Int>>()
        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    digits.add(char)
                    positions.add(Pair(x, y))
                }
                if (!char.isDigit() || line.lastX == x) {
                    if (positions.isNotEmpty()) {
                        numbers[positions.toSet()] = digits.joinToString("").toInt()
                    }

                    digits.clear()
                    positions.clear()
                }
            }
        }
        val gearRatios = mutableListOf<Int>()

        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isSpecial('*')) {
                    val adjacent = grid.adjacentToCoords(Pair(x, y))
                    val numbersAdjacentToIt = numbers.filter { (positions, _) ->
                        positions.intersect(adjacent).isNotEmpty()
                    }.values.toList()
                    if (numbersAdjacentToIt.size == 2) {
                        val (first, second) = numbersAdjacentToIt
                        gearRatios.add(first * second)
                    }
                }
            }
        }

        return gearRatios.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
