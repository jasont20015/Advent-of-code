import kotlin.math.min

fun main() {

    fun getSplitPart2(section: List<String>): Int{
        section.indices.forEach {
            var left = section.subList(0, it).reversed()
            var right = section.subList(it, section.size)

            val zipped = left.zip(right)
            var totalDifferences = zipped.sumOf { (b, a) -> if (a == b) 0 else a.getDifferences(b) }

            if(totalDifferences == 1) return it
        }
        return 0
    }

    fun getSplitPoint(section: List<String>): Long{
        val splitPoints = (0..section[0].length).toMutableList()
        for(line in section){
            val iterator = splitPoints.iterator()
            while(iterator.hasNext()){
                val point = iterator.next()
                var left = line.substring(0, point)
                var right = line.substring(point)
                val shortestSide = min(left.length, right.length)
                if(shortestSide == 0){
                    iterator.remove()
                    continue
                }
                left = left.reversed().take(shortestSide)
                right = right.take(shortestSide)
                if(!left.equals(right)) {
                    iterator.remove()
                }
            }
        }
        if(splitPoints.size == 0){
            return 0L
        }
        return (splitPoints.sum()).toLong()
    }
    fun transpose(section: List<String>): String{
        val newList = mutableListOf<String>()
        for (i in section[0].indices) {
            var row = ""
            for (j in section.indices) {
                row += section[j][i]
            }
            newList.add(row)
        }
        return newList.joinToString("\n")
    }

    fun part1(input: String): Long {
        val sectionsSplit = input.split("\n\n")
        val transposed = mutableListOf<String>()
        var total = 0L
        for(section in sectionsSplit){
            val sectionList = section.split("\n")
            total += getSplitPoint(sectionList)
            transposed.add(transpose(sectionList))
        }
        for(section in transposed){
            val sectionList = section.split("\n")
            total += getSplitPoint(sectionList)*100
        }
        return total
    }
    fun part2(input: String): Long {
        val sectionsSplit = input.split("\n\n")
        val transposed = mutableListOf<String>()
        var total = 0L
        for(section in sectionsSplit){
            val sectionList = section.split("\n")
            total += getSplitPart2(sectionList)*100
            transposed.add(transpose(sectionList))
        }
        for(section in transposed){
            val sectionList = section.split("\n")
            total += getSplitPart2(sectionList)
        }
        return total
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test").joinToString("\n");
    check(part2(testInput) == 400L)
    
    val input = readInput("Day13").joinToString("\n");
    println(part1(input))
    println(part2(input))
}
