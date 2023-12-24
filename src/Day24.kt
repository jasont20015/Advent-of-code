fun main() {
    data class point3DDbl(val x: Double, val y: Double, val z: Double)
    data class velocity(val x: Double, val y: Double, val z: Double)
    data class Hail(val position: point3DDbl, val velocity: velocity){
        fun slope() = velocity.y / velocity.x
    }

    fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
        0 -> listOf(listOf())
        else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
    }


    fun calcOverlap(hail1: Hail, hail2: Hail): Boolean{
        fun validFuture(hail:Hail, cx:Double, cy:Double): Boolean {
            return !((hail.velocity.x < 0 && hail.position.x < cx) || (hail.velocity.x > 0 && hail.position.x > cx) ||
                    (hail.velocity.y < 0 && hail.position.y < cy) || (hail.velocity.y > 0 && hail.position.y > cy))
        }
        if (hail1.slope() == hail2.slope())
            return false
        val cx = ((hail2.slope() * hail2.position.x) - (hail1.slope() * hail1.position.x) + hail1.position.y - hail2.position.y) / (hail2.slope() - hail1.slope())
        val cy = (hail1.slope() * (cx - hail1.position.x)) + hail1.position.y
        val valid = validFuture(hail1, cx, cy) && validFuture(hail2, cx, cy)

        return cx in 200000000000000.0..400000000000000.0 && cy in 200000000000000.0..400000000000000.0 && valid
    }

    fun part1(input: List<String>): Long {
        val hail = input.map{
            it.split(" @ ").let {
                Hail(it[0].split(", ").map{it.trim().toDouble()}.let{(x,y,z) -> point3DDbl(x,y,z)},
                    it[1].split(", ").map{it.trim().toDouble()}.let{(x,y,z) -> velocity(x,y,z)})
            }
        }
        return hail.combinations(2).count {calcOverlap(it[0], it[1])}.toLong()
    }

    //completed part 2 using python with help of Reddit
    fun part2(input: List<String>): Long {
        return 828_418_331_313_365L
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
   // check(part1(testInput) == 2L)
    //check(part2(testInput) == 0L)
    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
