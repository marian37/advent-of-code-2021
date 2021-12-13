import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Pair<List<List<Boolean>>, List<Pair<Char, Int>>> {
    val dots = MutableList(0) { Pair(0, 0) }
    val folds = MutableList(0) { Pair('x', 0) }

    var emptyLine = false
    input.forEach { line ->
      if (line == "") {
        emptyLine = true
      } else {
        if (emptyLine) {
          val (before, after) = line.split("=")
          folds.add(Pair(before[before.length - 1], after.toInt()))
        } else {
          val (x, y) = line.split(",")
          dots.add(Pair(x.toInt(), y.toInt()))
        }
      }
    }

    val maxX = dots.maxOf { it.first }
    val maxY = dots.maxOf { it.second }

    val paper = MutableList(maxY + 1) { MutableList(maxX + 1) { false } }

    dots.forEach { (x, y) -> paper[y][x] = true }

    return Pair(paper, folds)
  }

  fun foldPaper(paper: List<List<Boolean>>, instruction: Pair<Char, Int>): List<List<Boolean>> {
    if (instruction.first == 'x') {
      val newPaper = MutableList(paper.size) { MutableList(instruction.second) { false } }
      for (r in paper.indices) {
        for (c in 0 until instruction.second) {
          newPaper[r][c] = paper[r][c]
        }
      }
      for (r in paper.indices) {
        for (c in instruction.second + 1 until paper[r].size) {
          val newColumn = instruction.second - (c - instruction.second)
          newPaper[r][newColumn] = paper[r][c] || newPaper[r][newColumn]
        }
      }
      return newPaper
    } else {
      val newPaper = MutableList(instruction.second) { MutableList(paper[0].size) { false } }
      for (r in 0 until instruction.second) {
        for (c in paper[r].indices) {
          newPaper[r][c] = paper[r][c]
        }
      }
      for (r in instruction.second + 1 until paper.size) {
        val newRow = instruction.second - (r - instruction.second)
        for (c in paper[r].indices) {
          newPaper[newRow][c] = paper[r][c] || newPaper[newRow][c]
        }
      }
      return newPaper
    }
  }

  fun printPaper(paper: List<List<Boolean>>) {
    for (r in paper.indices) {
      for (c in paper[r].indices) {
        print(if (paper[r][c]) '#' else '.')
      }
      println()
    }
  }

  fun part1(input: List<String>): Int {
    val (paper, folds) = parseInput(input)
    val foldedPaper = foldPaper(paper, folds[0])
    return foldedPaper.sumOf { it.count { it == true } }
  }

  fun part2(input: List<String>) {
    val (paper, folds) = parseInput(input)
    var p = paper
    folds.forEach { f -> p = foldPaper(p, f) }
    printPaper(p)
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("13_test")
  check(part1(testInput) == 17)
  part2(testInput)

  val input = readInput("13")
  println(part1(input))
  part2(input)
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
