data class Rating(val categories: Map<Char, Int>) {
    companion object {
        fun from(str: String): Rating {
            val categories = str.drop(1).dropLast(1).split(",").associate {
                it.substringBefore("=").single() to it.substringAfter("=").toInt()
            }
            return Rating(categories)
        }
    }
}
data class Workflow(val name: String, val rules: List<Rule>) {
    companion object {
        fun from(str: String): Workflow {
            val name = str.substringBefore("{")
            val rules = str.substringAfter("{").substringBefore("}").split(",").map { Rule.from(it) }
            return Workflow(name, rules)
        }
    }
}

sealed class Rule {
    abstract val result: String

    data class Conditional(val left: Char, val operator: Char, val right: Int, override val result: String) : Rule() {
        fun range(): IntRange = if (operator == '<') (1..< right) else (right + 1..4000)
        fun reversedRange(): IntRange = if (operator == '<') (right..4000) else (1..right)
    }

    data class Unconditional(override val result: String) : Rule()

    fun matches(rating: Rating): Boolean {
        return when (this) {
            is Unconditional -> true
            is Conditional   -> {
                when (operator) {
                    '>'  -> rating.categories.getValue(left) > right
                    '<'  -> rating.categories.getValue(left) < right
                    else -> error("")
                }
            }
        }
    }

    companion object {
        fun from(str: String): Rule {
            return if (':' in str) {
                val condition = str.substringBefore(":")
                val result = str.substringAfter(":")
                Conditional(condition[0], condition[1], condition.substring(2).toInt(), result)
            } else {
                Unconditional(str)
            }
        }
    }
}

fun main() {
    fun IntRange.length() = last - start + 1

    fun IntRange.merge(other: IntRange) = (maxOf(first, other.first)..minOf(last, other.last))

    fun combinations(result: String, ranges: Map<Char, IntRange>, workflows: Map<String, Workflow>): Long {
        return when (result) {
            "R"  -> 0
            "A"  -> ranges.values.map { it.length().toLong() }.reduce(Long::times)
            else -> {
                val newRanges = ranges.toMutableMap()

                workflows.getValue(result).rules.sumOf { rule ->
                    when (rule) {
                        is Rule.Unconditional -> combinations(rule.result, newRanges, workflows)
                        is Rule.Conditional   -> {
                            val newRange = newRanges.getValue(rule.left).merge(rule.range())
                            val newReversed = newRanges.getValue(rule.left).merge(rule.reversedRange())
                            newRanges[rule.left] = newRange
                            combinations(rule.result, newRanges, workflows).also { newRanges[rule.left] = newReversed }
                        }
                    }
                }
            }
        }
    }

    fun Rating.score(workflow: Workflow, workflows: Map<String, Workflow>): Int {
        val rule = workflow.rules.first { it.matches(this) }
        return when (rule.result) {
            "R"  -> 0
            "A"  -> categories.values.sum()
            else -> score(workflows.getValue(rule.result), workflows)
        }
    }

    fun part1(input: List<String>): Long {
        val workflows = input.joinToString("\n").substringBefore("\n\n").lines().map { Workflow.from(it) }.associateBy { it.name }
        val ratings = input.joinToString("\n").substringAfter("\n\n").lines().map{Rating.from(it)}

        return ratings.sumOf { it.score(workflows.getValue("in"), workflows) }.toLong()
    }
    fun part2(input: List<String>): Long {
        val workflows = input.joinToString("\n").substringBefore("\n\n").lines().map { Workflow.from(it) }.associateBy { it.name }

        return combinations(
                result = "in",
                ranges = mapOf(
                        'x' to (1..4000),
                        'm' to (1..4000),
                        'a' to (1..4000),
                        's' to (1..4000)
                ),
                workflows
        )
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114L)
    check(part2(testInput) == 167409079868000L)
    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
