import java.io.File

fun main() {
    val blocks = File("src/Day05.txt").readText().split("\n\n")
    val seeds = blocks.first().substringAfter(": ").split(" ").map { it.toLong() }
    val seedRanges = seeds.chunked(2).map { (start, length) -> start..start + length }
    val rangeMaps = blocks.filter { it.contains("map") }.map { mapBlock ->
        mapBlock.split("\n").drop(1).map { line ->
            val (dst, src, length) = line.split(" ").map { it.toLong() }
            (src until src + length) to (dst until dst + length)
        }.toMap()
    }

    fun mapRange(input: LongRange,
                 rangeMap: Map<LongRange, LongRange>): List<LongRange> = buildList {
        val outputs = rangeMap.mapNotNull { (src, dst) ->
            val start = maxOf(src.first, input.first)
            val end = minOf(src.last, input.last)
            val shift = dst.first - src.first
            if (end < start) null else (start + shift)..(end + shift)
        }.ifEmpty { listOf(input.first..input.last) }
        addAll(outputs)
        if (size > 1) {
            val theirMin = rangeMap.minOf { it.key.first }
            val theirMax = rangeMap.maxOf { it.key.last }
            if (input.first < theirMin) add(input.first until theirMin)
            if (input.last > theirMax) add(theirMax + 1..input.last)
        }
    }

    fun List<LongRange>.mapRangesIteratively(): Long = flatMap { startingRange ->
        rangeMaps.fold(listOf(startingRange)) { ranges, rangeMap ->
            ranges.flatMap { range -> mapRange(range, rangeMap) }
        }
    }.minOf { it.first }

    fun part1(): Int {
        val mapped = seeds.map { it..it }.mapRangesIteratively()
        return mapped.toInt()
    }

    fun part2( ): Int {
        return seedRanges.mapRangesIteratively().toInt()
    }

    // test if implementation meets criteria from the description, like:
    //check(part1() == 35)

    part1().println()
    part2().println()
}
