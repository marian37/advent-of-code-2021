import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Pair<List<Int>, List<List<List<Int>>>> {
    val numbers = input[0].split(",").map { it.toInt() }
    val boards = mutableListOf<List<List<Int>>>()
    for (i in 2 until input.size step 6) {
      val board = mutableListOf<List<Int>>()
      for (j in 0 until 5) {
        board.add(input[i + j].trim().split("\\s+".toRegex()).map { it.toInt() })
      }
      boards.add(board)
    }
    return Pair(numbers, boards)
  }

  fun printChecked(checked: List<List<Boolean>>) {
    for (r in 0 until checked.size) {
      for (c in 0 until checked[r].size) {
        print(if (checked[r][c]) "X" else "O")
      }
      println()
    }
  }

  fun checkBoard(
      number: Int,
      board: List<List<Int>>,
      checked: MutableList<MutableList<Boolean>>
  ): Boolean {
    for (r in 0 until board.size) {
      for (c in 0 until board[r].size) {
        if (board[r][c] == number) {
          checked[r][c] = true
          if (checked.all { it[c] == true } || checked[r].all { it == true }) return true
        }
      }
    }
    return false
  }

  fun playBingo(
      numbers: List<Int>,
      boards: List<List<List<Int>>>
  ): Triple<Int, List<List<Int>>, List<List<Boolean>>> {
    val checked = MutableList(boards.size) { MutableList(5) { MutableList(5) { false } } }
    numbers.forEach { n ->
      boards.indices.forEach { b ->
        if (checkBoard(n, boards[b], checked[b])) {
          // println("$n\n${boards[b]}\n${printChecked(checked[b])}")
          return Triple(n, boards[b], checked[b])
        }
      }
    }
    return Triple(0, emptyList(), emptyList())
  }

  fun getScore(number: Int, board: List<List<Int>>, checked: List<List<Boolean>>): Int {
    var sum = 0
    for (r in 0 until board.size) {
      for (c in 0 until board[r].size) {
        if (!checked[r][c]) {
          sum += board[r][c]
        }
      }
    }
    return sum * number
  }

  fun part1(input: List<String>): Int {
    val (numbers, boards) = parseInput(input)
    val (number, board, checked) = playBingo(numbers, boards)
    // println("$number\n$board\n$checked")
    return getScore(number, board, checked)
  }

  fun part2(input: List<String>): Int {
    val (numbers, boards) = parseInput(input)
    var _boards = boards
    var score = 0
    while (_boards.size > 0) {
      val (number, board, checked) = playBingo(numbers, _boards)
      score = getScore(number, board, checked)
      _boards = _boards.filter { it != board }
    }
    return score
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("04_test")
  check(part1(testInput) == 4512)
  check(part2(testInput) == 1924)

  val input = readInput("04")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
