import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    var horizontal = 0
    var depth = 0
    for (line in input) {
      val (command, value) = line.split(" ")
      when (command) {
        "forward" -> horizontal += value.toInt()
        "down" -> depth += value.toInt()
        "up" -> depth -= value.toInt()
      }
    }
    return horizontal * depth
  }

  fun part2(input: List<String>): Int {
    var horizontal = 0
    var depth = 0
    var aim = 0
    for (line in input) {
      val (command, value) = line.split(" ")
      when (command) {
        "forward" -> {
          val intValue = value.toInt()
          horizontal += intValue
          depth += aim * intValue
        }
        "down" -> aim += value.toInt()
        "up" -> aim -= value.toInt()
      }
    }
    return horizontal * depth
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("02_test")
  check(part1(testInput) == 150)
  check(part2(testInput) == 900)

  val input = readInput("02")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
