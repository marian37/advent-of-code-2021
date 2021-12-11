import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val adjacent =
      listOf(
          Pair(-1, -1),
          Pair(-1, 0),
          Pair(-1, 1),
          Pair(0, -1),
          Pair(0, 1),
          Pair(1, -1),
          Pair(1, 0),
          Pair(1, 1)
      )

  fun simulateStep(grid: List<List<Int>>): Pair<Int, List<List<Int>>> {
    val flashed = MutableList(grid.size) { MutableList(grid[0].size) { false } }
    val newGrid = MutableList(grid.size) { MutableList(grid[0].size) { 0 } }

    for (r in grid.indices) {
      for (c in grid[r].indices) {
        newGrid[r][c] = grid[r][c] + 1
      }
    }

    val toBeChecked = MutableList(0) { Pair(0, 0) }
    for (r in grid.indices) {
      for (c in grid[r].indices) {
        toBeChecked.add(Pair(r, c))
      }
    }

    while (toBeChecked.isNotEmpty()) {
      val (cr, cc) = toBeChecked.removeLast()
      if (newGrid[cr][cc] > 9 && !flashed[cr][cc]) {
        flashed[cr][cc] = true
        adjacent.forEach { (r, c) ->
          newGrid.getOrNull(cr + r)?.getOrNull(cc + c)?.let {
            newGrid[cr + r][cc + c]++
            toBeChecked.add(Pair(cr + r, cc + c))
          }
        }
      }
    }

    for (r in newGrid.indices) {
      for (c in newGrid[r].indices) {
        if (newGrid[r][c] > 9) newGrid[r][c] = 0
      }
    }

    return Pair(flashed.sumOf { it.count { it == true } }, newGrid)
  }

  fun part1(input: List<String>): Int {
    var grid: List<List<Int>> =
        input.map { line ->
          val lineList = MutableList(0) { 0 }
          line.forEach { lineList.add(it.digitToInt()) }
          lineList
        }
    var sum = 0
    repeat(100) {
      val (n, g) = simulateStep(grid)
      sum += n
      grid = g
    }

    return sum
  }

  fun part2(input: List<String>): Int {
    var grid: List<List<Int>> =
        input.map { line ->
          val lineList = MutableList(0) { 0 }
          line.forEach { lineList.add(it.digitToInt()) }
          lineList
        }
    var i = 0
    var j = 0
    while (j != 100) {
      val (n, g) = simulateStep(grid)
      j = n
      grid = g
      i++
    }

    return i
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("11_test")
  check(part1(testInput) == 1656)
  check(part2(testInput) == 195)

  val input = readInput("11")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
