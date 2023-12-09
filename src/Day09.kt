fun main() {
    fun getDiff(input: List<Long>): List<Long>{
        return if(!input.all {it == 0L}){
            val diff = input.dropLast(1).mapIndexed({index, value -> input[index+1] - value})
            input.toMutableList().apply{ add(getDiff(diff).last() + input.last())}
        }
        else{
            input
        }
    }

    fun part1(input: List<String>): Long {
        val inputTransformed = input.map{it -> it.split(" ").map { it.toLong() }}
        return inputTransformed.sumOf { getDiff(it).last() }
    }

    fun part2(input: List<String>): Long {
        val inputTransformed = input.map{it -> it.split(" ").map { it.toLong() }}
        return inputTransformed.sumOf { getDiff(it.reversed()).last() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
