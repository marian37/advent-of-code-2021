import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val directions = listOf(Pair(0, 1), Pair(1, 0))

  fun getValue(input: List<String>, i: Int, j: Int, direction: Pair<Int, Int>): Char {
    val r = (input.size + i + direction.first) % input.size
    val c = (input[0].length + j + direction.second) % input[0].length
    return input[r][c]
  }

  fun move(input: List<String>, symbol: Char): List<String> {
    val output = MutableList(input.size) { "" }
    val direction = if (symbol == '>') directions[0] else directions[1]
    val oppositeDirection = Pair(-1 * direction.first, -1 * direction.second)
    for (i in input.indices) {
      for (j in input[0].indices) {
        output[i] =
            output[i] +
                if (input[i][j] == '.' && getValue(input, i, j, oppositeDirection) == symbol) symbol
                else if (input[i][j] == symbol && getValue(input, i, j, direction) == '.') '.'
                else input[i][j]
      }
    }
    return output
  }

  fun simulateMove(input: List<String>): List<String> {
    var output = move(input, '>')
    output = move(output, 'v')
    return output
  }

  fun part1(input: List<String>): Int {
    var oldState = emptyList<String>()
    var state = input
    var counter = 0
    while (oldState != state) {
      oldState = state
      counter++
      state = simulateMove(state)
    }
    return counter
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("25_test")
  check(part1(testInput) == 58)

  val input = readInput("25")
  println(part1(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
