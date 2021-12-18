import java.io.File
import kotlin.math.max

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  data class Number(
      var left: Number?,
      var leftValue: Int = -1,
      var right: Number?,
      var rightValue: Int = -1
  ) {
    override fun toString(): String {
      return "[${if (left != null) left.toString() else leftValue},${if (right != null) right.toString() else rightValue}]"
    }

    fun magnitude(): Int {
      val leftMagnitude = if (left != null) left!!.magnitude() else leftValue
      val rightMagnitude = if (right != null) right!!.magnitude() else rightValue
      return 3 * leftMagnitude + 2 * rightMagnitude
    }

    fun explode(level: Int): Pair<Int, Int>? {
      if (level == 1) {
        if (left != null && left!!.left == null && left!!.right == null) {
          val exploded = left
          left = null
          leftValue = 0

          var retR: Int
          if (right == null) {
            rightValue += exploded!!.rightValue
            retR = 0
          } else {
            retR = right!!.addLeft(exploded!!.rightValue)
          }
          return Pair(exploded.leftValue, retR)
        } else if (right != null && right!!.left == null && right!!.right == null) {
          val exploded = right
          right = null
          rightValue = 0

          var retL: Int
          if (left == null) {
            leftValue += exploded!!.leftValue
            retL = 0
          } else {
            retL = left!!.addRight(exploded!!.leftValue)
          }
          return Pair(retL, exploded.rightValue)
        } else {
          return null
        }
      } else {
        var exploded = left?.explode(level - 1)
        if (exploded == null) {
          exploded = right?.explode(level - 1)
        } else {
          // left exploded
          val (l, r) = exploded
          var retR: Int
          if (right == null) {
            rightValue += r
            retR = 0
          } else {
            retR = right!!.addLeft(r)
          }
          return Pair(l, retR)
        }
        if (exploded == null) return null
        else {
          // right exploded
          val (l, r) = exploded
          var retL: Int
          if (left == null) {
            leftValue += l
            retL = 0
          } else {
            retL = left!!.addRight(l)
          }
          return Pair(retL, r)
        }
      }
    }

    fun addLeft(n: Int): Int {
      if (left == null) {
        leftValue += n
        return 0
      }

      return left!!.addLeft(n)
    }

    fun addRight(n: Int): Int {
      if (right == null) {
        rightValue += n
        return 0
      }

      return right!!.addRight(n)
    }

    fun split(): Boolean {
      if (left == null) {
        if (leftValue >= 10) {
          val div = leftValue / 2
          val mod = leftValue % 2
          left = Number(null, div, null, div + mod)
          return true
        }
      } else {
        var splitLeft = left!!.split()
        if (splitLeft) return true
      }

      if (right == null) {
        if (rightValue >= 10) {
          val div = rightValue / 2
          val mod = rightValue % 2
          right = Number(null, div, null, div + mod)
          return true
        }
      } else {
        var splitRight = right!!.split()
        if (splitRight) return true
      }

      return false
    }
  }

  fun parseNumber(input: String): Number {
    var left: Number? = null
    var leftValue = -1
    var right: Number? = null
    var rightValue = -1

    if (input[1] == '[') {
      var i = 2
      val s = MutableList(1) { '[' }
      while (s.isNotEmpty()) {
        when (input[i]) {
          '[' -> s.add('[')
          ']' -> s.removeLast()
        }
        i++
      }
      left = parseNumber(input.slice(1..i - 1))
    } else {
      val i = input.indexOfFirst { it == ',' }
      leftValue = input.slice(1..i - 1).toInt()
    }

    if (input[input.length - 2] == ']') {
      var i = input.length - 3
      val s = MutableList(1) { ']' }
      while (s.isNotEmpty()) {
        when (input[i]) {
          ']' -> s.add(']')
          '[' -> s.removeLast()
        }
        i--
      }
      right = parseNumber(input.slice(i + 1..input.length - 2))
    } else {
      val i = input.indexOfLast { it == ',' }
      rightValue = input.slice(i + 1..input.length - 2).toInt()
    }
    return Number(left, leftValue, right, rightValue)
  }

  fun part1(input: List<String>): Int {
    var acc: Number? = null
    input.map { line ->
      val n = parseNumber(line)
      if (acc == null) acc = n else acc = Number(acc, -1, n, -1)
      // println("Before $acc")
      var end = false
      while (!end) {
        val exploded = acc!!.explode(4)
        // println("Exploded $exploded $acc")
        if (exploded == null) {
          val splitted = acc!!.split()
          // println("Splitted $splitted $acc")
          if (!splitted) end = true
        }
      }
      // println("After: $acc")
    }
    return acc!!.magnitude()
  }

  fun part2(input: List<String>): Int {
    var magnitude = 0
    for (i in input.indices) {
      for (j in input.indices) {
        if (i != j) {
          val n = Number(parseNumber(input[i]), -1, parseNumber(input[j]), -1)
          var end = false
          while (!end) {
            val exploded = n.explode(4)
            if (exploded == null) {
              val splitted = n.split()
              if (!splitted) end = true
            }
          }
          magnitude = max(n.magnitude(), magnitude)
        }
      }
    }
    return magnitude
  }

  // test if implementation meets criteria from the description, like:
  check(parseNumber("[[1,2],[[3,4],5]]").magnitude() == 143)
  check(parseNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude() == 1384)
  check(parseNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude() == 445)
  check(parseNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude() == 791)
  check(parseNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude() == 1137)
  check(parseNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude() == 3488)

  check(parseNumber("[[[[[9,8],1],2],3],4]").apply { explode(4) }.toString() == "[[[[0,9],2],3],4]")
  check(parseNumber("[7,[6,[5,[4,[3,2]]]]]").apply { explode(4) }.toString() == "[7,[6,[5,[7,0]]]]")
  check(parseNumber("[[6,[5,[4,[3,2]]]],1]").apply { explode(4) }.toString() == "[[6,[5,[7,0]]],3]")
  check(
      parseNumber("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").apply { explode(4) }.toString() ==
          "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
  )
  check(
      parseNumber("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").apply { explode(4) }.toString() ==
          "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
  )

  check(
      parseNumber("[[[[0,7],4],[15,[0,13]]],[1,1]]").apply { split() }.toString() ==
          "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
  )

  check(
      parseNumber("[[[[4,0],[5,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]")
          .apply { explode(4) }
          .toString() == "[[[[4,0],[5,4]],[[0,[7,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]"
  )

  val testInput = readInput("18_test")
  check(part1(testInput) == 4140)
  check(part2(testInput) == 3993)

  val input = readInput("18")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
