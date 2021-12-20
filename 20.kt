import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun getInput(input: List<String>, r: Int, c: Int, default: Int): Int {
    if (r < 0 || r >= input.size || c < 0 || c >= input[0].length) return default
    return if (input[r][c] == '#') 1 else 0
  }

  fun applyIEA(iea: String, input: List<String>, default: Int): List<String> {
    val output = MutableList(input.size + 2) { MutableList(0) { '#' } }
    val powerOf2 = listOf(1, 2, 4, 8, 16, 32, 64, 128, 256)
    val grid =
        listOf(
            Pair(1, 1),
            Pair(1, 0),
            Pair(1, -1),
            Pair(0, 1),
            Pair(0, 0),
            Pair(0, -1),
            Pair(-1, 1),
            Pair(-1, 0),
            Pair(-1, -1),
        )
    for (r in 0..input.size + 1) for (c in 0..input[0].length + 1) {
      var idx = 0
      for (i in grid.indices) {
        idx += powerOf2[i] * getInput(input, r + grid[i].first - 1, c + grid[i].second - 1, default)
      }
      output[r].add(iea[idx])
    }

    return output.map { it.joinToString(separator = "") { it.toString() } }
  }

  fun part1(input: List<String>): Int {
    val iea = input[0]
    var image = input.slice(2..input.lastIndex)
    repeat(2) { iteration ->
      image = applyIEA(iea, image, if (iea[0] == '#' && iteration % 2 == 1) 1 else 0)
    }
    return image.sumOf { it.count { it == '#' } }
  }

  fun part2(input: List<String>): Int {
    val iea = input[0]
    var image = input.slice(2..input.lastIndex)
    repeat(50) { iteration ->
      image = applyIEA(iea, image, if (iea[0] == '#' && iteration % 2 == 1) 1 else 0)
    }
    return image.sumOf { it.count { it == '#' } }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("20_test")
  check(part1(testInput) == 35)
  check(part2(testInput) == 3351)

  val input = readInput("20")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
