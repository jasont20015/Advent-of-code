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


    /**
     * Now numbers written out need to be equal to numbers. The easiest way would just be a replace all occurences.
     * Problem, if I use the replace function something like 'eightwothree' will only see 'eight' or 'two', depending on order.
     * Fix, change it to the value with the text appended to start and end.
     *
     * Not the most efficient (probably) but it works
     */
    fun part2(input: List<String>): Int {
        val numbers = mutableListOf<Int>()
        for(line in input){
            var newLine = line
            mapOf(
                    "one" to "one1one",
                    "two" to "two2two",
                    "three" to "three3three",
                    "four" to "four4four",
                    "five" to "five5five",
                    "six" to "six6six",
                    "seven" to "seven7seven",
                    "eight" to "eight8eight",
                    "nine" to "nine9nine",
            ).forEach { (k: String, v: String) ->
                newLine = newLine.replace(k, v)
            }

            var firstNumber = 0
            var secondNumber = 0
            for(c in newLine) {
                if (c.isDigit()){
                    firstNumber = c.digitToInt()*10
                    break;
                }
            }
            for(c in newLine.reversed()){
                if(c.isDigit()){
                    secondNumber = c.digitToInt()
                    break;
                }
            }
            numbers.add(firstNumber+secondNumber)
        }
        return numbers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 443)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
