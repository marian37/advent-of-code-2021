import java.io.File

fun main() {
  val input = File("01.txt").readLines().map { it.toInt() }

  val increased = input.indices.count { idx -> idx != 0 && input[idx] > input[idx - 1] }
  println("Increased: $increased")

  val windowSize = 3
  val increased2 =
      input.indices.count { idx ->
        idx >= windowSize &&
            input.subList(idx - windowSize + 1, idx + 1).sum() >
                input.subList(idx - windowSize, idx).sum()
      }
  println("Increased2: $increased2")
}
