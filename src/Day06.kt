fun main() {
    fun part1(input: List<String>): Int {
        val timeSplit = input[0].split(' ')
        val distanceSplit = input[1].split(' ')
        val timeNumbers = timeSplit.filter { it.all{it.isDigit()} }.filter{it.isNotEmpty()}.map { it.toInt() }
        val distanceNumbers = distanceSplit.filter { it.all{it.isDigit()} }.filter{it.isNotEmpty()}.map { it.toInt() }
        var results = mutableListOf<Int>()
        for(timenumberIndex in timeNumbers.indices){
            var j = 0
            for(millisecondsPressed in 0..timeNumbers[timenumberIndex]){
                var distanceMoved = millisecondsPressed *(timeNumbers[timenumberIndex]-millisecondsPressed)
                if(distanceMoved > distanceNumbers[timenumberIndex]){
                    j = millisecondsPressed
                    break
                }
            }
            if(j == 0){
                println("No solution found")
            }
            val amountOfAnswers = (timeNumbers[timenumberIndex] - (j*2)+1)
            results.add(amountOfAnswers)
        }
        return results.reduce { acc, i ->  acc * i }
    }

    fun part2(input: List<String>): Long {
        val timeSplit = input[0].split(' ')
        val distanceSplit = input[1].split(' ')
        val timeNumbers = timeSplit.filter { it.all{it.isDigit()} }.filter{it.isNotEmpty()}.joinToString(separator = "").toLong()
        val distanceNumbers = distanceSplit.filter { it.all{it.isDigit()} }.filter{it.isNotEmpty()}.joinToString(separator = "").toLong()


        var j = 0L
        for(millisecondsPressed in 0..timeNumbers){
            var distanceMoved = millisecondsPressed *(timeNumbers-millisecondsPressed)
            if(distanceMoved > distanceNumbers){
                j = millisecondsPressed
                break
            }
        }
        if(j == 0L){
            println("No solution found")
        }
        val amountOfAnswers = (timeNumbers - (j*2)+1)

        return amountOfAnswers
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
