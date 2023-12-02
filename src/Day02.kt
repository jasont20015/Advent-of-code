fun main() {
    fun getColour(input: String, colour: String): Int{
        val counter = input.split(",")
        var returnValue = 0
        for(item in counter){
            try {
                if (item.contains(colour)) {
                    val number = item.trim().split(' ')[0]
                    returnValue = number.toInt()
                    break
                } else {
                    continue;
                }
            }
            catch (ex: NumberFormatException){
                returnValue = 0
            }
        }
        return returnValue

    }

    fun getBlue(input: String): Int{
        return getColour(input, "blue")
    }
    fun getRed(input: String): Int{
        return getColour(input, "red")
    }
    fun getGreen(input: String): Int{
        return getColour(input, "green")
    }

    fun part1(input: List<String>): Int {
        val gameNumbers = mutableListOf<Int>()
        for(line in input ){
            val gameAndNumber = line.split(':')[0]
            val gameNumber = gameAndNumber.split(' ')[1].toInt()
            val inputCSV = line.split(':')[1].split(';')
            var highestRed = 0
            var highestBlue = 0
            var highestGreen = 0
            for(element in inputCSV) {
                val blue = getBlue(element)
                val green = getGreen(element)
                val red = getRed(element)
                if(blue > highestBlue){
                    highestBlue = blue
                }
                if(green > highestGreen){
                    highestGreen = green
                }
                if(red > highestRed){
                    highestRed = red
                }
            }
            if(!(highestBlue > 14 || highestGreen > 13 || highestRed > 12)){
                gameNumbers.add(gameNumber)
            }
        }
        return gameNumbers.sum()
    }

    fun part2(input: List<String>): Int {
        val powers = mutableListOf<Int>()
        for(line in input ) {
            val inputCSV = line.split(':')[1].split(';')
            var highestRed = 0
            var highestBlue = 0
            var highestGreen = 0
            for (element in inputCSV) {
                val blue = getBlue(element)
                val green = getGreen(element)
                val red = getRed(element)
                if (blue > highestBlue) {
                    highestBlue = blue
                }
                if (green > highestGreen) {
                    highestGreen = green
                }
                if (red > highestRed) {
                    highestRed = red
                }
            }
            powers.add(highestBlue*highestGreen*highestRed)
        }
        return powers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
