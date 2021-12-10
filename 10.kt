import java.io.File
import java.util.Stack

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun calc(line: String): Pair<Int, Stack<Char>?> {
    val stack = Stack<Char>()
    line.forEach { c ->
      when (c) {
        '(', '[', '{', '<' -> stack.push(c)
        ')' -> if (stack.pop() != '(') return Pair(3, null)
        ']' -> if (stack.pop() != '[') return Pair(57, null)
        '}' -> if (stack.pop() != '{') return Pair(1197, null)
        '>' -> if (stack.pop() != '<') return Pair(25137, null)
      }
    }
    return Pair(0, stack)
  }

  fun part1(input: List<String>): Int {
    return input.sumOf { line: String -> calc(line).first }
  }

  fun part2(input: List<String>): Long {
    val scores = MutableList(0) { 0L }
    input.forEach { line ->
      val (s, stack) = calc(line)
      if (s == 0) {
        var score = 0L
        while (stack?.isEmpty() == false) {
          score *= 5
          score +=
              when (stack.pop()) {
                '(' -> 1
                '[' -> 2
                '{' -> 3
                '<' -> 4
                else -> 0
              }
        }
        scores.add(score)
      }
    }
    scores.sort()
    return scores[scores.size / 2]
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("10_test")
  check(part1(testInput) == 26397)
  check(part2(testInput) == 288957L)

  val input = readInput("10")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
