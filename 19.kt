import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
  val transformations: List<List<List<Int>>> =
      listOf(
          // x-y-z
          listOf(
              listOf(1, 0, 0),
              listOf(0, 1, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 1, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, -1, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, 1, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, -1, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 1, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, -1, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, -1, 0),
              listOf(0, 0, -1),
          ),
          // x-z-y
          listOf(
              listOf(1, 0, 0),
              listOf(0, 0, 1),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 0, 1),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, 0, 1),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, 0, -1),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 0, 1),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 0, -1),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(1, 0, 0),
              listOf(0, 0, -1),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(-1, 0, 0),
              listOf(0, 0, -1),
              listOf(0, -1, 0),
          ),
          // y-x-z
          listOf(
              listOf(0, 1, 0),
              listOf(1, 0, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(-1, 0, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(1, 0, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(1, 0, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(-1, 0, 0),
              listOf(0, 0, 1),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(-1, 0, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(1, 0, 0),
              listOf(0, 0, -1),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(-1, 0, 0),
              listOf(0, 0, -1),
          ),
          // z-y-x
          listOf(
              listOf(0, 0, 1),
              listOf(0, 1, 0),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(0, 1, 0),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(0, -1, 0),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(0, 1, 0),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(0, -1, 0),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(0, 1, 0),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(0, -1, 0),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(0, -1, 0),
              listOf(-1, 0, 0),
          ),
          // y-z-x
          listOf(
              listOf(0, 1, 0),
              listOf(0, 0, 1),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(0, 0, 1),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(0, 0, 1),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(0, 0, -1),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(0, 0, 1),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, 1, 0),
              listOf(0, 0, -1),
              listOf(-1, 0, 0),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(0, 0, -1),
              listOf(1, 0, 0),
          ),
          listOf(
              listOf(0, -1, 0),
              listOf(0, 0, -1),
              listOf(-1, 0, 0),
          ),
          // z-x-y
          listOf(
              listOf(0, 0, 1),
              listOf(1, 0, 0),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(-1, 0, 0),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(1, 0, 0),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(1, 0, 0),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(0, 0, 1),
              listOf(-1, 0, 0),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(-1, 0, 0),
              listOf(0, 1, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(1, 0, 0),
              listOf(0, -1, 0),
          ),
          listOf(
              listOf(0, 0, -1),
              listOf(-1, 0, 0),
              listOf(0, -1, 0),
          ),
      )

  fun readInput(name: String) = File("$name.txt").readLines()

  data class Beacon(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String = "$x,$y,$z"

    fun transform(vec: List<List<Int>>): Beacon {
      return Beacon(
          x * vec[0][0] + y * vec[0][1] + z * vec[0][2],
          x * vec[1][0] + y * vec[1][1] + z * vec[1][2],
          x * vec[2][0] + y * vec[2][1] + z * vec[2][2]
      )
    }

    fun add(o: Beacon): Beacon {
      return Beacon(x + o.x, y + o.y, z + o.z)
    }
  }

  fun matrixMultiply(a: List<List<Int>>, b: List<List<Int>>): List<List<Int>> {
    val result = MutableList(a.size) { MutableList(a[0].size) { 0 } }
    for (r in result.indices) for (c in result[r].indices) {
      for (i in result.indices) {
        result[r][c] += a[r][i] * b[i][c]
      }
    }
    return result
  }

  fun parseBeacon(line: String): Beacon {
    val (x, y, z) = line.split(",")
    return Beacon(x.toInt(), y.toInt(), z.toInt())
  }

  data class Scanner(val id: Int, val beacons: List<Beacon>) {
    override fun toString(): String =
        "--- scanner $id ---\n${beacons.joinToString(separator = "\n") { it.toString() }}"

    fun transform(vec: List<List<Int>>): List<Beacon> {
      return beacons.map { it.transform(vec) }
    }
  }

  data class Pairing(val i: Int, val j: Int, val vec: List<List<Int>>, val transl: Beacon)

  fun parseInput(input: List<String>): List<Scanner> {
    val scanners = MutableList(0) { Scanner(0, emptyList()) }
    val beacons = MutableList(0) { Beacon(0, 0, 0) }
    var scannerId = -1
    input.forEach { line ->
      when {
        line.startsWith("---") -> {
          scannerId += 1
        }
        line.isEmpty() -> {
          scanners.add(Scanner(scannerId, beacons.toList()))
          beacons.clear()
        }
        else -> {
          beacons.add(parseBeacon(line))
        }
      }
    }
    scanners.add(Scanner(scannerId, beacons.toList()))
    return scanners
  }

  val pairings = mutableSetOf<Pairing>()

  fun part1(input: List<String>): Int {
    val scanners = parseInput(input)
    val pairs = mutableSetOf<Pairing>()

    for (i in scanners.indices) {
      for (j in scanners.indices) {
        if (i != j) {
          for (vec in transformations) {
            val transformed = scanners[j].transform(vec)
            for (b in scanners[i].beacons) for (t in transformed) {
              val diff = Beacon(b.x - t.x, b.y - t.y, b.z - t.z)
              val c = transformed.count { it.add(diff) in scanners[i].beacons }
              if (c >= 12) {
                pairs.add(Pairing(i, j, vec, diff))
              }
            }
          }
        }
      }
    }

    val processed = MutableList(scanners.size) { false }
    val toBeProcessed = mutableListOf<Pairing>()
    toBeProcessed.addAll(pairs.filter { it.i == 0 })
    val beaconsSet = mutableSetOf<Beacon>()
    beaconsSet.addAll(scanners[0].beacons)

    while (!processed.all { it } && toBeProcessed.isNotEmpty()) {
      val t = toBeProcessed.removeLast()
      if (processed[t.j]) {
        continue
      } else {
        pairings.add(t)
        beaconsSet.addAll(scanners[t.j].transform(t.vec).map { it.add(t.transl) })
        processed[t.j] = true
        toBeProcessed.addAll(
            pairs.filter { it.i == t.j }.map {
              Pairing(
                  0,
                  it.j,
                  matrixMultiply(t.vec, it.vec),
                  t.transl.add(it.transl.transform(t.vec))
              )
            }
        )
      }
    }

    return beaconsSet.size
  }

  fun part2(): Int {
    var dist = 0
    for (i in pairings) for (j in pairings) {
      val diff =
          abs(i.transl.x - j.transl.x) + abs(i.transl.y - j.transl.y) + abs(i.transl.z - j.transl.z)
      dist = max(dist, diff)
    }
    return dist
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("19_test")
  check(part1(testInput) == 79)
  check(part2() == 3621)

  pairings.clear()

  val input = readInput("19")
  println(part1(input))
  println(part2())
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
