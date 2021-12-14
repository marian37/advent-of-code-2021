import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Pair<List<Char>, Map<String, Char>> {
    val polymer = input[0].toCharArray().toList()

    val rules: MutableMap<String, Char> = mutableMapOf()
    for (i in 2 until input.size) {
      val a = input[i].substringBefore(" -> ")
      val b = input[i].substringAfter(" -> ")[0]
      rules.put(a, b)
    }

    return Pair(polymer, rules)
  }

  fun applyStep(polymer: List<Char>, rules: Map<String, Char>): List<Char> {
    val newPolymer = mutableListOf(polymer[0])
    polymer.windowed(2) { (a, b) ->
      newPolymer.add(rules["$a$b"] ?: ' ')
      newPolymer.add(b)
    }
    return newPolymer
  }

  fun part1(input: List<String>): Int {
    val (template, rules) = parseInput(input)
    var polymer = template
    repeat(10) { polymer = applyStep(polymer, rules) }

    val analysis = polymer.groupingBy { it }.eachCount()

    return analysis.maxOf { it.value } - analysis.minOf { it.value }
  }

  fun mergeMaps(m1: Map<Char, Long>, m2: Map<Char, Long>): Map<Char, Long> {
    val res = mutableMapOf<Char, Long>()
    m1.forEach { (k, v) -> res.put(k, v) }
    m2.forEach { (k, v) -> res.put(k, (res[k] ?: 0) + v) }
    return res
  }

  val cache: MutableMap<String, Map<Char, Long>> = mutableMapOf()
  fun applyStepRec(rules: Map<String, Char>, a: Char, b: Char, level: Int): Map<Char, Long> {
    val cc = cache["$a$b$level"]
    if (cc != null) return cc

    if (level == 0) {
      if (a == b) return mapOf(a to 2L)
      return mapOf(a to 1L, b to 1L)
    }

    val c = rules["$a$b"] ?: ' '
    val m1 = applyStepRec(rules, a, c, level - 1)
    val m2 = applyStepRec(rules, c, b, level - 1)
    val res = mergeMaps(m1, m2)
    cache.put("$a$b$level", res)
    return res
  }

  fun part2(input: List<String>, steps: Int): Long {
    val (template, rules) = parseInput(input)
    var res: Map<Char, Long> = mapOf(template[0] to 1L, template[template.lastIndex] to 1L)
    template.windowed(2) { (a, b) -> res = mergeMaps(res, applyStepRec(rules, a, b, steps)) }
    return res.maxOf { it.value / 2 } - res.minOf { it.value / 2 }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("14_test")
  check(part1(testInput) == 1588)
  check(part2(testInput, 10) == 1588L)
  cache.clear()
  check(part2(testInput, 40) == 2188189693529)
  cache.clear()

  val input = readInput("14")
  println(part1(input))
  println(part2(input, 40))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
