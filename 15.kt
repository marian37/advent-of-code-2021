import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun Dijkstra(input: List<String>): Int {
    val pos: MutableList<Pair<Int, Int>> = MutableList(input.size * input[0].length) { Pair(0, 0) }
    val d: MutableList<Int> = MutableList(pos.size) { Int.MAX_VALUE / 2 }
    d[0] = 0

    val adj = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    var s = 0
    for (r in input.indices) {
      for (c in input[r].indices) {
        pos[s++] = Pair(r, c)
      }
    }

    val q: MutableList<Pair<Int, Int>> = mutableListOf()
    q.addAll(pos)
    while (q.isNotEmpty()) {
      val value = q.minOf { (a, b) -> d[a * input[0].length + b] }
      val index = q.indexOfFirst { (a, b) -> d[a * input[0].length + b] == value }
      val v = q[index]
      for ((r, c) in adj) {
        if (v.first + r < 0 ||
                v.first + r >= input.size ||
                v.second + c < 0 ||
                v.second + c >= input[0].length
        )
            continue
        if (d.getOrElse((v.first + r) * input[0].length + v.second + c, { _ -> 0 }) >
                value + input[v.first + r][v.second + c].digitToInt()
        )
            d[(v.first + r) * input[0].length + v.second + c] =
                value + input[v.first + r][v.second + c].digitToInt()
      }
      q.removeAt(index)
    }

    return d[pos.lastIndex]
  }

  fun part1(input: List<String>): Int {
    return Dijkstra(input)
  }

  fun part2(input: List<String>): Int {
    val newInput: MutableList<String> = MutableList(input.size * 5) { "" }

    for (r in 0..4) {
      for (c in 0..4) {
        for (line in input.indices) {
          newInput[r * input.size + line] +=
              input[line]
                  .map {
                    val digit = it.digitToInt() + r + c
                    if (digit > 9) digit % 10 + 1 else digit
                  }
                  .joinToString(separator = "")
        }
      }
    }

    return Dijkstra(newInput)
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("15_test")
  check(part1(testInput) == 40)
  check(part2(testInput) == 315)

  val input = readInput("15")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
