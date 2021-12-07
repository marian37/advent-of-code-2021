import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    val positions = input[0].split(",").map { it.toInt() }
    val mp = positions.minOf { it }
    val Mp = positions.maxOf { it }
    var minD = Int.MAX_VALUE
    for (p in mp..Mp) {
      val d = positions.sumOf { abs(it - p) }
      minD = min(d, minD)
    }
    return minD
  }

  fun part2(input: List<String>): Int {
    val positions = input[0].split(",").map { it.toInt() }
    val mp = positions.minOf { it }
    val Mp = positions.maxOf { it }
    var minD = Int.MAX_VALUE
    for (p in mp..Mp) {
      val d = positions.sumOf { (abs(it - p) + 1) * abs(it - p) / 2 }
      minD = min(d, minD)
    }
    return minD
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("07_test")
  check(part1(testInput) == 37)
  check(part2(testInput) == 168)

  val input = readInput("07")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
