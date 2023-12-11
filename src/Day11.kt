import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Long {
        val expR = input.indices.filterNot { input[it].contains("#") }.toSet()
        val expC = input[0].indices.filterNot { c -> input.map { it[c] }.joinToString("").contains("#") }.toSet()
        val galaxies = input.indices.flatMap { row -> input[row].indices.filter { input[row][it] == '#' }.map { col -> row to col }}
        var tdist = 0L
        for(i in galaxies.indices) {
            for(j in i+1..galaxies.lastIndex) {
                val g1 = galaxies[i]
                val g2 = galaxies[j]

                val exR = (minOf(g1.first, g2.first) .. maxOf(g1.first, g2.first)).count { expR.contains(it) }
                val exC = (minOf(g1.second, g2.second) .. maxOf(g1.second, g2.second)).count { expC.contains(it) }

                val dist = (g1.first - g2.first).absoluteValue + (g1.second - g2.second).absoluteValue  + (exR + exC)
                tdist += dist
            }
        }
        return tdist
    }
    fun part2(input: List<String>): Long {
        val expR = input.indices.filterNot { input[it].contains("#") }.toSet()
        val expC = input[0].indices.filterNot { c -> input.map { it[c] }.joinToString("").contains("#") }.toSet()
        val galaxies = input.indices.flatMap { row -> input[row].indices.filter { input[row][it] == '#' }.map { col -> row to col }}
        var tdist = 0L
        for(i in galaxies.indices) {
            for(j in i+1..galaxies.lastIndex) {
                val g1 = galaxies[i]
                val g2 = galaxies[j]

                val exR = (minOf(g1.first, g2.first) .. maxOf(g1.first, g2.first)).count { expR.contains(it) }
                val exC = (minOf(g1.second, g2.second) .. maxOf(g1.second, g2.second)).count { expC.contains(it) }

                val dist = (g1.first - g2.first).absoluteValue + (g1.second - g2.second).absoluteValue  + (exR + exC) * 999999L
                tdist += dist
            }
        }
        return tdist
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    
    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
