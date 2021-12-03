import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    var gamma = ""
    var epsilon = ""
    val half = input.size / 2
    for (i in 0 until input[0].length) {
      val c = input.count { line -> line[i] == '1' }
      gamma += if (c > half) '1' else '0'
      epsilon += if (c > half) '0' else '1'
    }
    val g = gamma.toInt(2)
    val e = epsilon.toInt(2)
    return g * e
  }

  fun part2(input: List<String>): Int {
    var listO2 = input
    var i = 0
    while (listO2.size > 1) {
      val (match, rest) = listO2.partition { line -> line[i] == '1' }
      listO2 = if (match.size >= rest.size) match else rest
      i++
    }
    var o2 = listO2[0].toInt(2)
    var listCO2 = input
    i = 0
    while (listCO2.size > 1) {
      val (match, rest) = listCO2.partition { line -> line[i] == '0' }
      listCO2 = if (match.size <= rest.size) match else rest
      i++
    }
    var co2 = listCO2[0].toInt(2)
    return o2 * co2
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("03_test")
  check(part1(testInput) == 198)
  check(part2(testInput) == 230)

  val input = readInput("03")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
