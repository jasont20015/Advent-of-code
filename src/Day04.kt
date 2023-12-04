fun main() {
    fun addToScore(currentScore: Int, winningNumbers: List<Int>, numberToCheck: Int): Int{
        var score = currentScore
        if(numberToCheck in winningNumbers) {
            if (score == 0)
                score = 1
            else
                score *= 2
        }

        return score
    }
    fun part1(input: List<String>): Int {
        var outcomes = mutableListOf<Int>()
        for(line in input){
            var currentValue = 0
            val numbers = line.split(':')[1].split('|')
            val winning = numbers[0].split(' ').filterNot { it.equals("") }.map { it.toInt() }
            val myNumbers = numbers[1].split(' ').filterNot { it.equals("") }.map { it.toInt() }
            for(num in myNumbers){
                currentValue = addToScore(currentValue, winning, num)
            }
            outcomes.add(currentValue)
        }
        return outcomes.sum()
    }

    fun part2(input: List<String>): Int {
        val scoreMap =
                (0..input.count())
                        .zip(List(input.count()) { 1 })
                        .toMap().toMutableMap()

        input.map { card ->
            card.substringAfter(":")
                    .split("|")
                    .map { x ->
                        x.trim().replace("  ", " ").split(" ")
                                .map { y -> y.toInt() }
                    }.zipWithNext()
                    .sumOf { (winning, scratch) ->
                        winning.intersect(scratch.toSet()).count()
                    }
        }.forEachIndexed { index, i ->
            for (x in 1..i) {
                scoreMap[index + x] = scoreMap[index + x]!! + scoreMap[index]!!
            }
        }
        return scoreMap.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
