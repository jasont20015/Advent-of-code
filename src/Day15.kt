
fun main() {
    fun String.hash(): Int {
        return this.fold(0) {carry, char ->
            ((carry + char.code)*17).rem(256)
        }
    }
    fun part1(input: List<String>): Long {
        val inputConcat = input[0]
        val splitOnComma = inputConcat.split(",")
        return splitOnComma.sumOf { it.hash() }.toLong()
    }
    fun part2(input: List<String>): Long {
        val boxes = List<MutableMap<String, Int>>(256) { mutableMapOf() }

        val inputConcat = input[0]
        val splitOnComma = inputConcat.split(",")

        for(item in splitOnComma){
            if(item.endsWith("-")){
                val label = item.substringBeforeLast("-")
                boxes[label.hash()].remove(label)
            }else{
                val label = item.substringBeforeLast("=")
                boxes[label.hash()].compute(label) { _, _ -> item.substringAfter("=").toInt()}
            }
        }

        return boxes.withIndex().sumOf { (index, items) ->
            items.values.withIndex().sumOf { (itemIndex, item)  ->
                (index + 1) * (itemIndex + 1) * item
            }
        }.toLong()

        return 0L
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)
    check(part2(testInput) == 145L)
    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
