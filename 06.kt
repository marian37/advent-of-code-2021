import java.io.File
import kotlin.math.max

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    val initialState = input[0].split(",").map { it.toInt() }
    val state: MutableList<Int> = initialState.toMutableList()
    repeat(80) {
      var toAdd = 0
      state.indices.forEach { i ->
        when (state[i]) {
          0 -> {
            state[i] = 6
            toAdd++
          }
          in 1..9 -> state[i]--
        }
      }
      state.addAll(MutableList(toAdd) { 8 })
    }
    return state.size
  }

  fun part2(input: List<String>): Long {
    val initialState = input[0].split(",").map { it.toInt() }
    val days = 256
    val result =
        initialState.sumOf { n ->
          val produces = MutableList(days) { 0L }
          produces.indices.forEach { i ->
            produces[i] =
                when (i) {
                  in 0 until n -> 1
                  n -> 2
                  in (n + 1)..6 -> produces[i - 1]
                  in 7..9 -> max(produces[i - 1], produces[i - 7] + 1)
                  else ->
                      max(produces[i - 1], produces[i - 1] + produces[i - 7] - produces[i - 8]) +
                          produces[i - 9] - produces[i - 10]
                }
          }
          produces[days - 1]
        }
    return result
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("06_test")
  check(part1(testInput) == 5934)
  check(part2(testInput) == 26984457539)

  val input = readInput("06")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
