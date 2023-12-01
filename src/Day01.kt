fun main() {

    /**
     * The instruction are interpreted as follows:
     * Each line has at least 1 numeric value
     * The first digit and last digit of the number make up a calibration value
     * I need to give back the sum of all calibration values.
     *
     * If no number is found, I just ignore it and go to the next number
     */
    fun part1(input: List<String>): Int {
        val numbers = mutableListOf<Int>()
        for (line in input) {
            var firstNumber = 0
            var secondNumber = 0
            for(c in line) {
                if (c.isDigit()){
                    firstNumber = c.digitToInt()*10
                    break;
                }
            }
            for(c in line.reversed()){
                if(c.isDigit()){
                    secondNumber = c.digitToInt()
                    break;
                }
            }
            numbers.add(firstNumber+secondNumber)
        }
        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
