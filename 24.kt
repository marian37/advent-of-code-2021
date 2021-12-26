import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun simulate(instructions: List<String>, input: String): Boolean {
    val xyzw = "xyzw"
    val register = MutableList(4) { 0 }
    var inputCounter = 0
    instructions.forEach { instruction ->
      val parts = instruction.split(" ")
      // if (parts[0] == "inp") println("$input ${input[inputCounter]} $register")
      when (parts[0]) {
        "inp" -> register[xyzw.indexOf(parts[1])] = input[inputCounter++].digitToInt()
        "add" ->
            register[xyzw.indexOf(parts[1])] +=
                if (xyzw.indexOf(parts[2]) == -1) parts[2].toInt()
                else register[xyzw.indexOf(parts[2])]
        "mul" ->
            register[xyzw.indexOf(parts[1])] *=
                if (xyzw.indexOf(parts[2]) == -1) parts[2].toInt()
                else register[xyzw.indexOf(parts[2])]
        "div" ->
            register[xyzw.indexOf(parts[1])] /=
                if (xyzw.indexOf(parts[2]) == -1) parts[2].toInt()
                else register[xyzw.indexOf(parts[2])]
        "mod" ->
            register[xyzw.indexOf(parts[1])] %=
                if (xyzw.indexOf(parts[2]) == -1) parts[2].toInt()
                else register[xyzw.indexOf(parts[2])]
        "eql" ->
            register[xyzw.indexOf(parts[1])] =
                if ((if (xyzw.indexOf(parts[2]) == -1) parts[2].toInt()
                    else register[xyzw.indexOf(parts[2])]) == register[xyzw.indexOf(parts[1])]
                )
                    1
                else 0
      }
    }
    val valid = register[2] == 0
    // println("$valid $input $register")
    return valid
  }

  fun requiredInValid(input: List<String>): Map<Int, Pair<Int, Int>> {
    val size = input.size / 14
    // for (i in 0 until size) {
    //   println("I: $i")
    //   println(
    //       input.indices.filter { it % size == i }.map { input[it] }.joinToString(separator =
    // "\n")
    //   )
    // }
    val required = mutableMapOf<Int, Pair<Int, Int>>()
    val stack = mutableListOf<Int>()

    val indexOfY = 15
    val yList =
        input.indices.filter { idx -> idx % size == indexOfY }.map { idx ->
          input[idx].split(" ")[2].toInt()
        }
    val indexOfX = 5
    val xList =
        input.indices.filter { idx -> idx % size == indexOfX }.map { idx ->
          input[idx].split(" ")[2].toInt()
        }

    for (i in yList.indices) {
      val lastYIdx = stack.lastOrNull()
      if (lastYIdx == null) {
        stack.add(i)
        continue
      }
      val x = xList[i]
      if (yList[lastYIdx] + x < 9) {
        stack.removeLast()
        required.put(i, Pair(lastYIdx, x + yList[lastYIdx]))
      } else {
        stack.add(i)
      }
    }

    // println(required)
    return required
  }

  fun applyRules(rules: Map<Int, Pair<Int, Int>>, modelNumberBuilder: MutableList<Int>): String {
    for ((k, v) in rules) {
      val a = modelNumberBuilder[v.first] - v.second
      if (a > 0 && a <= 9) {
        modelNumberBuilder[v.first] = a
      } else {
        modelNumberBuilder[k] = modelNumberBuilder[k] + v.second
      }
    }
    return modelNumberBuilder.joinToString(separator = "")
  }

  fun part1(input: List<String>): Long {
    val modelNumberBuilder = MutableList(14) { 9 }
    val rules = requiredInValid(input)
    val modelNumber = applyRules(rules, modelNumberBuilder)
    val valid = simulate(input, modelNumber)
    if (!valid) throw Exception("Invalid model number $modelNumber")
    return modelNumber.toLong()
  }

  fun part2(input: List<String>): Long {
    val modelNumberBuilder = MutableList(14) { 1 }
    val rules = requiredInValid(input)
    val modelNumber = applyRules(rules, modelNumberBuilder)
    val valid = simulate(input, modelNumber)
    if (!valid) throw Exception("Invalid model number $modelNumber")
    return modelNumber.toLong()
  }

  val input = readInput("24")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
