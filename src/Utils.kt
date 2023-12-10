import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import java.lang.Math.multiplyExact

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
//day 1
fun getFirstDigit(line: String): Int{
    for(c in line) {
        if (c.isDigit()){
            return c.digitToInt()
        }
    }
    return 0
}
fun getLastDigit(line: String): Int{
    return getFirstDigit(line.reversed())
}
fun getCalibrationValue(line: String): Int{
    val firstNumber = getFirstDigit(line)*10
    val lastNumber = getLastDigit(line)
    return firstNumber+lastNumber
}
//day 3
val <E> List<E>.lastX: Int
    get() = this.size - 1

fun Char.isSpecial(c: Char? = null): Boolean =
        if (c != null) this == c else this != ' ' && this != '.' && !isDigit()

fun <E> List<List<E>>.adjacentToCoords(position: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val (x, y) = position
    val validPositions = mutableListOf<Pair<Int, Int>>()
    for (dy in -1..1) {
        for (dx in -1..1) {
            if (dx == 0 && dy == 0) continue
            val newX = x + dx
            val newY = y + dy
            if (newX < 0 || newY < 0) continue
            if (newY >= size || newX >= this[newY].size) continue
            validPositions.add(Pair(newX, newY))
        }
    }
    return validPositions.toSet()
}


fun <E> List<List<E>>.adjacentTo(position: Pair<Int, Int>): Set<E> {
    val coords = adjacentToCoords(position)
    return coords.map { (x, y) -> this[y][x] }.toSet()
}
//day 7

enum class POWER { HIGH_CARD, ONE_PAIR, TWO_PAIRS, THREE, FULL_HOUSE, FOUR, FIVE }
data class Hand(
        val cards: String,
        val rate: Int,
        val handPower: POWER,
        val cardsPower: List<Int> = cards.map { cardsOrder.indexOf(it) },
        val cardsPowerPart2: List<Int> = cards.map { cardsOrderPart2.indexOf(it) },
)

fun getHands(input: List<String>, isPart2: Boolean = false) = input.map { s ->
    s.split(" ").let { Hand(it[0], it[1].toInt(), if (isPart2) getHandPowerPart2(it[0]) else getPower(it[0])) }
}

fun getPower(cards: String): POWER = cards.groupBy { it }.let { map ->
    when (map.size) {
        1 -> POWER.FIVE
        2 -> if (map.values.any { it.size == 4 }) POWER.FOUR else POWER.FULL_HOUSE
        3 -> if (map.values.any { it.size == 3 }) POWER.THREE else POWER.TWO_PAIRS
        4 -> POWER.ONE_PAIR
        else -> POWER.HIGH_CARD
    }
}

fun getHandPowerPart2(cards: String): POWER {
    if ('J' !in cards) return getPower(cards)
    return cardsOrderPart2.asSequence().map { c -> getPower(cards.replace('J', c)) }.maxByOrNull { it.ordinal }
            ?: POWER.HIGH_CARD
}

fun getSortedList(hands: List<Hand>, isPart2: Boolean = false): List<Hand> {
    return hands.sortedWith(
            compareBy(
                    { it.handPower },
                    { if (isPart2) it.cardsPowerPart2[0] else it.cardsPower[0] },
                    { if (isPart2) it.cardsPowerPart2[1] else it.cardsPower[1] },
                    { if (isPart2) it.cardsPowerPart2[2] else it.cardsPower[2] },
                    { if (isPart2) it.cardsPowerPart2[3] else it.cardsPower[3] },
                    { if (isPart2) it.cardsPowerPart2[4] else it.cardsPower[4] },
            )
    )
}
fun List<Hand>.customSum() = this.withIndex().sumOf { (index, it) -> it.rate * (index + 1) }

const val cardsOrder = "23456789TJQKA"
const val cardsOrderPart2 = "J23456789TQKA"

//day 8
fun greatestCommonDenominator(a: Long, b: Long): Long {
    var x = if (a > 0) a else -a
    var y = if (b > 0) b else -b

    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}
fun leastCommonMultiple(a: Long, b: Long): Long = multiplyExact(a, b) / greatestCommonDenominator(a, b)


fun Iterable<Long>.leastCommonMultipleLong(): Long = reduce(::leastCommonMultiple)

fun Iterable<Int>.leastCommonMultipleInt(): Long = map(Int::toLong).leastCommonMultipleLong()

//Day 10
typealias Position = Pair<Int, Int>

enum class cardinalDirection {
    NORTH, EAST, SOUTH, WEST
}
fun cardinalDirection.toPosition(): Position = when (this){
    cardinalDirection.NORTH -> -1 to 0
    cardinalDirection.EAST -> 0 to 1
    cardinalDirection.SOUTH -> 1 to 0
    cardinalDirection.WEST -> 0 to -1
}
fun cardinalDirection.opposite(): cardinalDirection = when(this){
    cardinalDirection.NORTH -> cardinalDirection.SOUTH
    cardinalDirection.EAST -> cardinalDirection.WEST
    cardinalDirection.SOUTH -> cardinalDirection.NORTH
    cardinalDirection.WEST -> cardinalDirection.EAST
}
operator fun Position.plus(other: Position): Position =
        first + other.first to second + other.second