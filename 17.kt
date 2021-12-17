import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    val yIndex = input.first().indexOf('y')
    val lastDotIndex = input.first().lastIndexOf('.')
    val y = input.first().slice(yIndex + 2..lastDotIndex - 2).toInt()
    val startYvelo = -y - 1
    return (0..startYvelo).sum()
  }

  fun steps(y: Int, yRange: IntRange): IntRange? {
    var c = 0
    var cc = y
    var sum = 0
    while (sum !in yRange) {
      c++
      sum += cc--
      if (sum < Int.MIN_VALUE / 16) return null
    }
    var c2 = c
    while (sum in yRange) {
      c2++
      sum += cc--
      if (sum < Int.MIN_VALUE / 16) return null
    }
    return c..c2 - 1
  }

  fun xCount(steps: IntRange?, xRange: IntRange): Int {
    if (steps == null) return 0
    val s: MutableSet<Int> = mutableSetOf()
    steps.forEach { step ->
      (1..xRange.endInclusive).forEach { x ->
        var sum = 0
        var cc = x
        repeat(step) {
          sum += cc
          if (cc > 0) cc--
        }
        if (sum in xRange) {
          s.add(x)
        }
      }
    }
    return s.size
  }

  fun part2(input: List<String>): Int {
    return Regex("""target area: x=(\d+)..(\d+), y=-(\d+)..-(\d+)""").matchEntire(input.first())!!
        .destructured.let { (x1, x2, y1, y2) ->
      val xm = x1.toInt()
      val xM = x2.toInt()
      val ym = -y1.toInt()
      val yM = -y2.toInt()
      val yRange = ym..-ym - 1
      yRange.sumOf { y ->
        val s = steps(y, ym..yM)
        xCount(s, xm..xM)
      }
    }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("17_test")
  check(part1(testInput) == 45)
  check(part2(testInput) == 112)

  val input = readInput("17")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
