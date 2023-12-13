plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}


task("generateNextDay") {
    doLast {
        val prevDayNum = fileTree("$projectDir/src").matching {
            include("Day*.kt")
        }.maxOf {
            val (prevDayNum) = Regex("Day(\\d\\d)").find(it.name)!!.destructured
            prevDayNum.toInt()
        }
        val newDayNum = String.format("%02d", prevDayNum + 1)
        File("$projectDir/src", "Day$newDayNum.kt").writeText(
                """
fun main() {
    fun part1(input: List<String>): Long {
        return 0L
    }
    fun part2(input: List<String>): Long {
        return 0L
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${newDayNum}_test")
    check(part1(testInput) == 0L)
    check(part2(testInput) == 0L)
    val input = readInput("Day$newDayNum")
    println(part1(input))
    println(part2(input))
}
"""
        )
        File("$projectDir/src", "Day$newDayNum.txt").writeText("")
        File("$projectDir/src", "Day${newDayNum}_test.txt").writeText("")
    }
}