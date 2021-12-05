import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
  companion object {
    private val regex = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")
    fun parseLine(line: String): Line {
      return regex.matchEntire(line)!!.destructured.let { (x1, y1, x2, y2) ->
        Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
      }
    }
  }
}

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    val lines = input.map(Line::parseLine)
    val maxX = lines.maxOf { max(it.x1, it.x2) } + 1
    val maxY = lines.maxOf { max(it.y1, it.y2) } + 1

    val grid: MutableList<MutableList<Int>> = MutableList(maxY) { MutableList(maxX) { 0 } }
    lines.forEach { line ->
      when {
        line.x1 == line.x2 -> {
          val ym = min(line.y1, line.y2)
          val yM = max(line.y1, line.y2)
          for (y in ym..yM) {
            grid[y][line.x1]++
          }
        }
        line.y1 == line.y2 -> {
          val xm = min(line.x1, line.x2)
          val xM = max(line.x1, line.x2)
          for (x in xm..xM) {
            grid[line.y1][x]++
          }
        }
      }
    }

    return grid.sumOf { it.count { it > 1 } }
  }

  fun part2(input: List<String>): Int {
    val lines = input.map(Line::parseLine)
    val maxX = lines.maxOf { max(it.x1, it.x2) } + 1
    val maxY = lines.maxOf { max(it.y1, it.y2) } + 1

    val grid: MutableList<MutableList<Int>> = MutableList(maxY) { MutableList(maxX) { 0 } }
    lines.forEach { line ->
      when {
        line.x1 == line.x2 -> {
          val ym = min(line.y1, line.y2)
          val yM = max(line.y1, line.y2)
          for (y in ym..yM) {
            grid[y][line.x1]++
          }
        }
        line.y1 == line.y2 -> {
          val xm = min(line.x1, line.x2)
          val xM = max(line.x1, line.x2)
          for (x in xm..xM) {
            grid[line.y1][x]++
          }
        }
        line.x1 - line.y1 == line.x2 - line.y2 -> {
          val xm = min(line.x1, line.x2)
          val xM = max(line.x1, line.x2)
          val yD = line.x1 - line.y1
          for (i in xm..xM) {
            grid[i - yD][i]++
          }
        }
        line.x1 - line.y2 == line.x2 - line.y1 -> {
          val xm = min(line.x1, line.x2)
          val xM = max(line.x1, line.x2)
          val yD = line.x1 - line.y2
          for (i in xm..xM) {
            grid[xM - i + xm - yD][i]++
          }
        }
      }
    }

    return grid.sumOf { it.count { it > 1 } }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("05_test")
  check(part1(testInput) == 5)
  check(part2(testInput) == 12)

  val input = readInput("05")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
