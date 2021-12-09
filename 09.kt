import java.io.File

fun main() {
  val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    return input.indices.sumOf { r ->
      input[r].indices.sumOf { c ->
        val add: Int =
            if (directions.all { dir ->
                  dir.first + r < 0 ||
                      dir.first + r >= input.size ||
                      dir.second + c < 0 ||
                      dir.second + c >= input[r].length ||
                      input[r][c] < input[r + dir.first][c + dir.second]
                }
            )
                input[r][c].digitToInt() + 1
            else 0
        add
      }
    }
  }

  fun search(
      input: List<String>,
      basinsMap: MutableList<MutableList<Int>>,
      basinsCounter: Int,
      r: Int,
      c: Int
  ): Int {
    var acc = 0
    directions.forEach { (y, x) ->
      if (r + y >= 0 && r + y < input.size && c + x >= 0 && c + x < input[0].length) {
        if (input[r + y][c + x] != '9' && basinsMap[r + y][c + x] != basinsCounter) {
          basinsMap[r + y][c + x] = basinsCounter
          acc += 1 + search(input, basinsMap, basinsCounter, r + y, c + x)
        }
      }
    }
    return acc
  }

  fun part2(input: List<String>): Int {
    var basinsCounter = 0
    val basins: MutableList<Int> = MutableList(0) { 0 }
    val basinsMap: MutableList<MutableList<Int>> =
        MutableList(input.size) { MutableList(input[0].length) { 0 } }
    input.indices.forEach { r ->
      input[r].indices.forEach { c ->
        if (directions.all { dir ->
              dir.first + r < 0 ||
                  dir.first + r >= input.size ||
                  dir.second + c < 0 ||
                  dir.second + c >= input[r].length ||
                  input[r][c] < input[r + dir.first][c + dir.second]
            }
        ) {
          basinsMap[r][c] = ++basinsCounter
          basins.add(1 + search(input, basinsMap, basinsCounter, r, c))
        }
      }
    }
    basins.sortDescending()
    return basins[0] * basins[1] * basins[2]
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("09_test")
  check(part1(testInput) == 15)
  check(part2(testInput) == 1134)

  val input = readInput("09")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
