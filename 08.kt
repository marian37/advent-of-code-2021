import java.io.File
import kotlin.text.toCharArray

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    return input.sumOf {
      it.substringAfter("|").split(" ").count { listOf(2, 3, 4, 7).contains(it.length) }
    }
  }

  fun part2(input: List<String>): Int {
    return input.sumOf {
      val a1: Set<Char> =
          it.split(" ").find { it.length == 2 }?.toCharArray()?.map { it }?.toSet() ?: emptySet()
      val a4: Set<Char> =
          it.split(" ").find { it.length == 4 }?.toCharArray()?.map { it }?.toSet() ?: emptySet()

      it.substringAfter("|")
          .split(" ")
          .reduce { acc, n ->
            val m: String =
                when (n.length) {
                  2 -> "1"
                  3 -> "7"
                  4 -> "4"
                  5 -> {
                    when {
                      a1.all { n.contains(it) } -> "3"
                      a4.filterNot { a1.contains(it) }.all { n.contains(it) } -> "5"
                      else -> "2"
                    }
                  }
                  6 -> {
                    when {
                      a4.all { n.contains(it) } -> "9"
                      a1.all { n.contains(it) } -> "0"
                      else -> "6"
                    }
                  }
                  7 -> "8"
                  else -> ""
                }
            acc + m
          }
          .toInt()
    }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("08_test")
  check(part1(testInput) == 26)
  check(part2(testInput) == 61229)

  val input = readInput("08")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
