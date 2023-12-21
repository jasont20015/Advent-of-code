data class Module(val label: String, val type: Char, var state: String, var children: List<String>, val parents: MutableList<Pair<String, String>>)

fun main() {
    fun parseModule(input: String): Module {
        val sides = input.split(" -> ")
        val type = sides[0][0]
        var label = sides[0]
        if (type != 'b')
            label = label.drop(1)
        val children = sides[1].split(", ")
        return Module(label, type, "low", children, mutableListOf())
    }

    fun connectChildren(modules: List<Module>) {
        for (module in modules) {
            for (child in module.children) {
                val node = modules.find { it.label == child }
                node?.parents?.add(Pair(module.label, "low"))
            }
        }
    }

    fun part1(input: List<String>): Long {
        val modules = input.map { parseModule(it) }
                .plus(Module("output", 'o', "low", emptyList(), mutableListOf()))
                .plus(Module("rx", 'o', "low", emptyList(), mutableListOf()))
        connectChildren(modules)
        var low = 0L
        var high = 0L

        repeat(1000){
            val active = mutableListOf(Triple("button", "broadcaster", "low"))
            while (active.isNotEmpty()) {
                val currentSignal = active.removeFirst()
                if (currentSignal.third == "low") low++ else high++
                val currentModule = modules.find { it.label == currentSignal.second }!!
                when(currentModule.type) {
                    '%' -> {
                        if (currentSignal.third == "low") {
                            currentModule.state = if (currentModule.state == "low") "high" else "low"
                            for (child in currentModule.children) {
                                active.add(Triple(currentModule.label, child, currentModule.state))
                            }
                        }
                    }
                    '&' -> {
                        val parent = currentModule.parents.find { it.first == currentSignal.first }!!
                        currentModule.parents.remove(parent)
                        currentModule.parents.add(Pair(parent.first, currentSignal.third))
                        val signalOut = if (currentModule.parents.any { it.second == "low" }) "high" else "low"
                        for (child in currentModule.children) {
                            active.add(Triple(currentModule.label, child, signalOut))
                        }
                    }
                    'b' -> {
                        for (child in currentModule.children) {
                            active.add(Triple(currentModule.label, child, currentSignal.third))
                        }
                    }
                }
            }
        }
        return low * high
    }
    fun pruneChildren(modules: List<Module>, keep: String): List<Module> {
        val broadcaster = modules.find { it.label == "broadcaster" }!!
        val newBroadcaster = Module(broadcaster.label, 'b', "low", listOf(keep), mutableListOf())
        val newModules = mutableSetOf(newBroadcaster)
        var count = 0
        while (count != newModules.size) {
            count = newModules.size
            for (module in newModules.toList()) {
                for (c in module.children) {
                    newModules.add(modules.find { it.label == c }!!)
                }
            }
        }
        return newModules.toList()
    }


    fun part2(input: List<String>): Long {
        val allModules = input.map { parseModule(it) }
                .plus(Module("output", 'o', "low", emptyList(), mutableListOf()))
                .plus(Module("rx", 'o', "low", emptyList(), mutableListOf()))

        val broadcaster = allModules.find { it.label == "broadcaster" }!!
        // broadcaster -> fm, hv, kc, bv
        val lcms = mutableListOf<Long>()
        for (cycle in broadcaster.children) {
            val modules = pruneChildren(allModules, cycle)
            for (module in modules) {
                module.parents.clear()
            }
            connectChildren(modules)

            var count = 0L
            var found = false
            while (!found) {
                count++
                val active = mutableListOf(Triple("button", "broadcaster", "low"))
                var rx = 0
                while (active.isNotEmpty()) {
                    val currentSignal = active.removeFirst()
                    if (currentSignal.second == "rx" && currentSignal.third == "low")
                        rx = 1
                    val currentModule = modules.find { it.label == currentSignal.second }!!
                    when (currentModule.type) {
                        '%' -> {
                            if (currentSignal.third == "low") {
                                currentModule.state = if (currentModule.state == "low") "high" else "low"
                                for (child in currentModule.children) {
                                    active.add(Triple(currentModule.label, child, currentModule.state))
                                }
                            }
                        }

                        '&' -> {
                            val parent = currentModule.parents.find { it.first == currentSignal.first }!!
                            currentModule.parents.remove(parent)
                            currentModule.parents.add(Pair(parent.first, currentSignal.third))
                            val signalOut = if (currentModule.parents.any { it.second == "low" }) "high" else "low"
                            for (child in currentModule.children) {
                                active.add(Triple(currentModule.label, child, signalOut))
                            }
                        }

                        'b' -> {
                            for (child in currentModule.children) {
                                active.add(Triple(currentModule.label, child, currentSignal.third))
                            }
                        }
                    }
                }
                if (rx == 1) {
                    lcms.add(count)
                    found = true
                }
            }
        }
        return leastCommonMultiple(lcms)
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 11687500L)
    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
