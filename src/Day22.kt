import kotlin.math.max
import kotlin.math.min

data class Point3d(var x: Int, var y: Int, var z: Int) {
    val fall: Point3d get() = Point3d(x, y, z - 1)

    companion object {
        fun of(input: String): Point3d {
            val (x, y, z) = input.split(',').map { it.trim().toInt() }
            return Point3d(x, y, z)
        }
    }
}


fun main() {
    fun calcSupports(bricks: List<Pair<Point3d, Point3d>>): Pair<Map<Int, Set<Int>>, Map<Int, Set<Int>>> {
        val supports = hashMapOf<Int, HashSet<Int>>()
        val supportedBy = hashMapOf<Int, HashSet<Int>>()
        bricks.indices.forEach { j ->
            bricks.take(j).indices.forEach { i ->
                if ((bricks[i].first.x..bricks[i].second.x).intersect(bricks[j].first.x..bricks[j].second.x).isNotEmpty() &&
                        (bricks[i].first.y..bricks[i].second.y).intersect(bricks[j].first.y..bricks[j].second.y).isNotEmpty() &&
                        bricks[j].first.z == bricks[i].second.z + 1) {
                    supports.computeIfAbsent(i) { hashSetOf() }.add(j)
                    supportedBy.computeIfAbsent(j) { hashSetOf() }.add(i)
                }
            }
        }
        return supports to supportedBy
    }
    fun dropBricks(bricks: List<Pair<Point3d, Point3d>>): List<Pair<Point3d, Point3d>>{
        val fallenBricks = bricks.toMutableList()
        var changed: Boolean
        do {
            changed = false
            fallenBricks.indices.forEach {
                val brick = fallenBricks[it]
                if (fallenBricks.none { max(brick.first.x, it.first.x) <= min(brick.second.x, it.second.x) && max(brick.first.y, it.first.y) <= min(brick.second.y, it.second.y) && brick.first.z == it.second.z + 1 } && brick.first.z != 1) {
                    fallenBricks[it] = brick.first.fall to brick.second.fall
                    changed = true
                }
            }
        } while (changed)
        return fallenBricks
    }
    fun findDropping(supports: Map<Int, Set<Int>>, supportedBy: Map<Int, Set<Int>>, index: Int): Set<Int> {
        val queue = ArrayDeque(supports[index]?.filter { j -> supportedBy[j]?.size == 1 } ?: listOf())
        val falling = mutableSetOf(index).apply { addAll(queue) }
        while (queue.isNotEmpty()) {
            val j = queue.removeFirst()
            supports[j]?.filterNot { it in falling }?.forEach { k ->
                if (supportedBy[k]?.all { it in falling } == true) {
                    queue.add(k)
                    falling.add(k)
                }
            }
        }
        return falling
    }
    fun parseBricks(input: List<String>): List<Pair<Point3d, Point3d>>{
        return input.map{
            val(first, second) = it.split('~')
            Point3d.of(first) to Point3d.of(second)
        }.sortedBy { it.first.z }
    }
    fun part1(input: List<String>): Long {
        val bricks = dropBricks(parseBricks(input))
        val (supports, supportedBy) = calcSupports(bricks)
        return bricks.indices.count { i -> supports[i]?.all { j -> (supportedBy[j]?.size ?: 3) >= 2 } ?: true }.toLong()
    }
    fun part2(input: List<String>): Long {
        val bricks = dropBricks(parseBricks(input))
        val (supports, supportedBy) = calcSupports(bricks)
        return bricks.indices.sumOf { i -> findDropping(supports, supportedBy, i).size - 1 }.toLong()
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5L)
    check(part2(testInput) == 7L)
    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
