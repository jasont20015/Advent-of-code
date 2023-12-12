
fun main() {

    val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    fun count(text: String, numbers: List<Int>): Long{
        if(numbers.isEmpty()) return if ('#' in text) 0 else 1
        if(text.isEmpty()) return 0

        return cache.getOrPut(text to numbers) {
            var result = 0L
            if (text.first() in ".?")
                result += count(text.substring(1), numbers)
            if (text.first() in "#?" && numbers[0] <= text.length && !("." in text.take(numbers.first())) &&
                    (numbers.first() == text.length || text[numbers.first()] != '#'))
                result += count(text.drop(numbers.first() + 1), numbers.drop(1))
            result
        }
    }

    fun part1(input: List<String>): Long {
        var total = 0L
        for(line in input){
            val text = line.split(' ')[0]
            val numbers = line.split(' ')[1].split(',').map{it.toInt()}
            total += count(text, numbers)
        }
        return total
    }
    fun part2(input: List<String>): Long {
        var total = 0L
        for(line in input){
            var text = line.split(' ')[0]
            text = "${text}?"
            text = text.repeat(5).dropLast(1)

            val numbers = line.split(' ')[1]
            val numbersCleaned = "${numbers},".repeat(5).dropLast(1).split(',').map{it.toInt()}

            total += count(text, numbersCleaned)
        }
        return total
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part2(testInput) == 525152L)
    
    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
