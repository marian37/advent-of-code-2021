import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Map<String, List<String>> {
    val result: MutableMap<String, MutableList<String>> = mutableMapOf()
    input.forEach { line ->
      val s = line.substringBefore("-")
      val e = line.substringAfter("-")
      if (result[s] != null) result[s]?.add(e) else result[s] = mutableListOf(e)
      if (result[e] != null) result[e]?.add(s) else result[e] = mutableListOf(s)
    }
    return result
  }

  fun search(graph: Map<String, List<String>>, visited: List<String>, current: String): Int {
    if (current == "end") return 1

    val newVisited = if (current.uppercase() == current) visited else visited + current
    return graph[current]?.sumOf { next ->
      var sum = 0
      if (!visited.contains(next)) {
        sum += search(graph, newVisited, next)
      }
      sum
    }
        ?: 0
  }

  fun search2(
      graph: Map<String, List<String>>,
      visited: List<String>,
      visitedTwice: Boolean,
      current: String,
  ): Int {
    if (current == "end") {
      return 1
    }

    val newVisited = if (current.uppercase() == current) visited else visited + current
    return graph[current]?.sumOf { next ->
      var sum = 0
      if (visitedTwice || next.uppercase() == next || next == "start") {
        if (!visited.contains(next)) sum += search2(graph, newVisited, visitedTwice, next)
      } else {
        if (visited.contains(next)) sum += search2(graph, newVisited, true, next)
        else sum += search2(graph, newVisited, false, next)
      }
      sum
    }
        ?: 0
  }

  fun part1(input: List<String>): Int {
    val graph = parseInput(input)
    val visited: List<String> = listOf("start")
    return search(graph, visited, "start")
  }

  fun part2(input: List<String>): Int {
    val graph = parseInput(input)
    val visited: List<String> = listOf("start")
    return search2(graph, visited, false, "start")
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("12_test")
  check(part1(testInput) == 10)
  check(part2(testInput) == 36)

  val input = readInput("12")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
