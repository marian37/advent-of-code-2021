import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  data class Range(val s: Int, val e: Int) {
    fun intersect(other: Range): Boolean {
      return (other.s >= s && other.s <= e) ||
          (other.e >= s && other.e <= e) ||
          (s >= other.s && s <= other.e) ||
          (e >= other.s && e <= other.e)
    }
  }

  fun intersection(range: Range, otherRange: Range): Range {
    val r1 = range.s
    val r2 = range.e

    val o1 = otherRange.s
    val o2 = otherRange.e

    return if (r1 >= o1) {
      if (r2 <= o2) Range(r1, r2) else Range(r1, o2)
    } else {
      if (r2 > o2) Range(o1, o2) else Range(o1, r2)
    }
  }

  data class Cuboid(val isOn: Boolean, val xRange: Range, val yRange: Range, val zRange: Range) {
    fun volume(): Long {
      return (1L + xRange.e - xRange.s) *
          (1L + yRange.e - yRange.s).toLong() *
          (1L + zRange.e - zRange.s).toLong()
    }

    fun intersectOther(other: Cuboid): Boolean {
      return xRange.intersect(other.xRange) &&
          yRange.intersect(other.yRange) &&
          zRange.intersect(other.zRange)
    }

    fun getIntersection(other: Cuboid): Cuboid {
      return Cuboid(
          isOn,
          intersection(xRange, other.xRange),
          intersection(yRange, other.yRange),
          intersection(zRange, other.zRange)
      )
    }

    override fun toString(): String = "${if (isOn) "on" else "off"},$xRange,$yRange,$zRange\n"
  }

  fun parseRange(input: String): Range {
    val s = input.substringBefore("..").toInt()
    val end = input.substringAfter("..").toInt()
    return Range(s, end)
  }

  val inputLineRegex = """([a-z]+) x=([-|0-9|..]+),y=([-|0-9|..]+),z=([-|0-9|..]+)""".toRegex()
  fun parseInput(input: List<String>): List<Cuboid> {
    return input.map { line ->
      inputLineRegex.matchEntire(line)!!.destructured.let { (on, x, y, z) ->
        Cuboid(on == "on", parseRange(x), parseRange(y), parseRange(z))
      }
    }
  }

  fun part1(input: List<String>): Int {
    val cuboids = parseInput(input)
    val cube = MutableList(102) { MutableList(102) { MutableList(102) { false } } }
    val region = -50..50
    cuboids.forEach { c ->
      val newXRange = (c.xRange.s..c.xRange.e).intersect(region)
      val newYRange = (c.yRange.s..c.yRange.e).intersect(region)
      val newZRange = (c.zRange.s..c.zRange.e).intersect(region)
      for (x in newXRange) {
        for (y in newYRange) {
          for (z in newZRange) {
            cube[x + 50][y + 50][z + 50] = c.isOn
          }
        }
      }
    }

    return cube.sumOf { it.sumOf { it.count { it == true } } }
  }

  fun part2(input: List<String>): Long {
    val inputCuboids = parseInput(input)

    val onCuboids = mutableListOf<Cuboid>()
    val overlappingCuboids = mutableListOf<Cuboid>()
    inputCuboids.forEach { i ->
      val overlaps = mutableListOf<Cuboid>()
      val parts = mutableListOf<Cuboid>()
      onCuboids.forEach { o -> if (i.intersectOther(o)) overlaps.add(i.getIntersection(o)) }
      overlappingCuboids.forEach { o -> if (i.intersectOther(o)) parts.add(i.getIntersection(o)) }

      onCuboids.addAll(parts)
      overlappingCuboids.addAll(overlaps)
      if (i.isOn) onCuboids.add(i)
    }

    var m = 0L
    m += onCuboids.sumOf { it.volume() }
    m -= overlappingCuboids.sumOf { it.volume() }
    return m
  }

  // test if implementation meets criteria from the description, like:
  val testInput1 = readInput("22_test1")
  val testInput2 = readInput("22_test2")
  val testInput3 = readInput("22_test3")
  check(part1(testInput1) == 39)
  check(part1(testInput2) == 590784)
  check(part1(testInput3) == 474140)

  check(part2(testInput1) == 39L)
  check(part2(testInput3) == 2758514936282235L)

  val input = readInput("22")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
