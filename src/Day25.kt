
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph


fun main() {
    fun part1(input: List<String>): Long {
        //so much for only using java/kotlin base libraries.
        val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        input.forEach { line ->
            val (name, others) = line.split(": ")
            graph.addVertex(name)
            others.split(" ").forEach { other ->
                graph.addVertex(other)
                graph.addEdge(name, other)
            }
        }

        val oneSide = StoerWagnerMinimumCut(graph).minCut()
        return ((graph.vertexSet().size - oneSide.size) * oneSide.size).toLong()
    }
    fun part2(input: List<String>): Long {
        return 0L
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 54L)
    check(part2(testInput) == 0L)
    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
