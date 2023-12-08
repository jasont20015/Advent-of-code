private data class Node(val id: String, val leftId: String, val rightId: String)

private class GhostMap(private val nodes: Map<String, Node>, private val instructions: String) {

    /** From the given node, at the given [step], decide which is the next visited node. */
    private fun Node.next(step: Int): Node = when (instructions[step % instructions.length]) {
        'L' -> nodes.getValue(leftId)
        'R' -> nodes.getValue(rightId)
        else -> error("Impossible state.")
    }
    private fun navigate(start: Node, destination: (Node) -> Boolean): Int {
        var node = start
        var step = 0

        while (destination(node).not()) {
            if (step < 0) return -1
            node = node.next(step)
            step += 1
        }

        return step
    }
    fun navigateAsGhost(): Long {
        fun navigateAsSuperpositionComponent(start: Node): Int {
            var node = start
            val result = navigate(node) { it.id.endsWith('Z') }

            // Check assumption that the step cycle will keep yielding destination nodes.
            if (result.times(2).rem(instructions.length) != 0) return -1
            for (i in 0..<result) node = node.next(i)
            return if (node.id.endsWith('Z')) result else -1
        }

        return nodes.values
                .filter { it.id.endsWith('A') }
                .map(::navigateAsSuperpositionComponent)
                .onEach { if (it == -1) return -1 }
                .leastCommonMultipleInt()
    }

    fun navigateAsHuman(): Int {
        val start = nodes["AAA"] ?: return -1
        return navigate(start) { it.id == "ZZZ" }
    }
}

private fun parseInput(input: String): GhostMap = runCatching {
    val nodeRegex = Regex("""^(\w{3}) = \((\w{3}), (\w{3})\)$""")
    val instructionRegex = Regex("""[LR]+""")

    val directions = input.substringBefore('\n')
    require(directions.matches(instructionRegex)) { "Invalid instruction set." }

    val nodes = input
            .lineSequence()
            .drop(2)
            .map { line -> nodeRegex.matchEntire(line)!!.destructured }
            .map { (id, left, right) -> Node(id, left, right) }
            .associateBy { it.id }

    GhostMap(nodes, directions)
}.getOrElse { cause -> throw IllegalArgumentException("Invalid input.", cause) }

fun main() {
    fun part1(input: List<String>): Int {
        return parseInput(input.joinToString("\n")).navigateAsHuman()
    }

    fun part2(input: List<String>): Long {
        return parseInput(input.joinToString("\n")).navigateAsGhost()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
