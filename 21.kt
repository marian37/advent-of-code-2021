import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  data class Player(var position: Int, var score: Int) {
    override fun toString() = "$position;$score"
  }

  fun part1(input: List<String>): Int {
    val p1position = input[0].substringAfter(":").trim().toInt()
    val p2position = input[1].substringAfter(":").trim().toInt()

    var p1turn = true
    var diceRolled = 0
    var dice = 0
    val p1 = Player(p1position, 0)
    val p2 = Player(p2position, 0)
    while (p1.score < 1000 && p2.score < 1000) {
      var valueRolled = 0
      repeat(3) {
        valueRolled += (dice++ % 100) + 1
        diceRolled++
      }
      var p = if (p1turn) p1 else p2
      p.position += valueRolled
      p.position %= 10
      p.score += if (p.position == 0) 10 else p.position
      p1turn = !p1turn
    }

    return min(p1.score, p2.score) * diceRolled
  }

  val valuesRolled = listOf(3 to 1L, 4 to 3L, 5 to 6L, 6 to 7L, 7 to 6L, 8 to 3L, 9 to 1L)
  val cache = mutableMapOf<String, Long>()

  fun p1win(p1turn: Boolean, p1: Player, p2: Player): Long {
    val hash = "${if (p1turn) 1 else 0}${p1};${p2}"
    if (cache[hash] != null) return cache[hash] ?: 0L

    if (!p1turn && p1.score >= 21) return 1
    if (p1turn && p2.score >= 21) return 0

    var res = 0L
    for ((value, times) in valuesRolled) {
      if (p1turn) {
        var newPosition = p1.position + value
        newPosition %= 10
        res +=
            times *
                p1win(
                    !p1turn,
                    Player(newPosition, p1.score + if (newPosition == 0) 10 else newPosition),
                    Player(p2.position, p2.score)
                )
      } else {
        var newPosition = p2.position + value
        newPosition %= 10
        res +=
            times *
                p1win(
                    !p1turn,
                    Player(p1.position, p1.score),
                    Player(newPosition, p2.score + if (newPosition == 0) 10 else newPosition),
                )
      }
    }
    cache[hash] = res
    return res
  }

  fun part2(input: List<String>): Long {
    val p1position = input[0].substringAfter(":").trim().toInt()
    val p2position = input[1].substringAfter(":").trim().toInt()

    val p1wins = p1win(true, Player(p1position, 0), Player(p2position, 0))
    val p2wins = p1win(false, Player(p2position, 0), Player(p1position, 0))
    // println("$p1wins $p2wins")
    return max(p1wins, p2wins)
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("21_test")
  check(part1(testInput) == 739785)
  check(part2(testInput) == 444356092776315L)

  cache.clear()

  val input = readInput("21")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
