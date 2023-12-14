
object Day14{
    enum class TileType(val c: Char) {
        ROLLER('O'), FIXED('#'), EMPTY('.')
    }

    data class Tile(
            val type: TileType,
            val pos: Pair<Int, Int>,
    )

    fun parse(input: List<String>): MutableList<MutableList<Tile>> {
        return input.mapIndexed { row, s ->
            s.mapIndexedNotNull { col, c ->
                when (c) {
                    '#' -> {
                        Tile(
                                TileType.FIXED,
                                row to col
                        )
                    }
                    'O' -> {
                        Tile(
                                TileType.ROLLER,
                                row to col
                        )
                    }
                    '.' -> {
                        Tile(
                                TileType.EMPTY,
                                row to col
                        )
                    }
                    else -> {
                        null
                    }
                }
            }.toMutableList()
        }.toMutableList()
    }
}

fun main() {
    fun transpose(section: List<String>): List<String>{
        val newList = mutableListOf<String>()
        for (i in section[0].indices) {
            var row = ""
            for (j in section.indices) {
                row += section[j][i]
            }
            newList.add(row)
        }
        return newList
    }
    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return List(first().size) { rowIdx ->
            List(size) { colIdx ->
                this[colIdx][rowIdx]
            }
        }
    }

    fun swapIndex(word: String, index1: Int, index2: Int): String{
        var tempWord = word.toCharArray()
        val tempChar = tempWord[index1]
        tempWord[index1] = tempWord[index2]
        tempWord[index2] = tempChar
        return tempWord.joinToString("")
    }
    fun moveRocksLeft(input: List<String>): List<String>{
        val newLines = mutableListOf<String>()
        input.forEachIndexed { lineIndex, line ->
            var currentIndex = 0
            newLines.add(lineIndex, line)
            line.forEachIndexed { index, char ->
                when (char) {
                    'O' -> {
                        newLines[lineIndex] = swapIndex(newLines[lineIndex], index, currentIndex)
                        currentIndex++
                    }
                    '#' -> {
                        currentIndex = index + 1
                    }
                }
            }
        }
        return newLines
    }

    fun part1(input: List<String>): Long {
        val transposed = transpose(input)
        val indexes = mutableListOf<Long>()
        val newLines = moveRocksLeft(transposed);
        for(line in newLines){
            line.reversed().forEachIndexed{ index, char ->
                if(char == 'O'){
                    indexes.add(index+1L)
                }
            }
        }
        return indexes.sum()
    }
    fun rocksToString(rocks: MutableList<MutableList<Day14.Tile>>) = rocks.joinToString("\n") { row ->
        row.joinToString("") { it.type.c.toString() }
    }
    fun load(row: List<Day14.Tile>): Int {
        return row.indices.reversed().zip(row).filter { it.second.type == Day14.TileType.ROLLER }.sumOf { it.first + 1 }
    }
    fun Pair<Int, Int>.coerce(maxes: Pair<Int, Int>): Pair<Int, Int> = first.coerceIn(0 until maxes.first) to second.coerceIn(0 until maxes.second)

    fun roll(pos: Pair<Int, Int>, dir: Cardinal, rocks: MutableList<MutableList<Day14.Tile>>) {
        val newPos = dir.of(pos).coerce(rocks.size to rocks[0].size)
        if (rocks[newPos.first][newPos.second].type != Day14.TileType.EMPTY) {
            return
        }
        rocks[newPos.first][newPos.second] = Day14.Tile(Day14.TileType.ROLLER, newPos)
        rocks[pos.first][pos.second] = Day14.Tile(Day14.TileType.EMPTY, pos)
        roll(newPos, dir, rocks)
    }

    fun part2(input: List<String>): Long {
        val rocks = Day14.parse(input)
        val cycle = listOf(
                Cardinal.NORTH to { it: List<Day14.Tile> -> it.sortedBy { it.pos.first } },
                Cardinal.WEST to { it: List<Day14.Tile> -> it.sortedBy { it.pos.second } },
                Cardinal.SOUTH to { it: List<Day14.Tile> -> it.sortedByDescending { it.pos.first } },
                Cardinal.EAST to { it: List<Day14.Tile> -> it.sortedByDescending { it.pos.second } },
        )
        val stateHistory = mutableListOf(
                rocksToString(rocks) to rocks.transpose().sumOf {
                    load(it)
                }
        )
        while (stateHistory.last() !in stateHistory.subList(0, stateHistory.size - 1)) {
            cycle.forEach { (dir, sort) ->
                sort(rocks.flatten().filter { it.type == Day14.TileType.ROLLER }).forEach {
                    roll(it.pos, dir, rocks)
                }
            }
            stateHistory.add(rocksToString(rocks) to rocks.transpose().sumOf {
                load(it)
            })
        }
        val cycleStart = stateHistory.indexOfFirst { it == stateHistory.last() }
        val cycleLength = stateHistory.size - cycleStart - 1
        val targetIdx = (1_000_000_000 - cycleStart) % cycleLength + cycleStart
        return stateHistory[targetIdx].second.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136L)
    check(part2(testInput) == 64L)
    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))

}
