fun main() {


    fun part1(input: List<String>): Int {
        return getSortedList(getHands(input)).customSum()
    }

    fun part2(input: List<String>): Int {
        return getSortedList(getHands(input, true), true).customSum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
