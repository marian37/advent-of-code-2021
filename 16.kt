import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines().first()

  data class Packet(
      val version: Int,
      val type: Int,
      val lengthTypeID: Boolean?,
      val length: Int?,
      val literal: Long?,
      val subpackets: List<Packet>
  ) {
    fun versionSum(): Int {
      return version + subpackets.sumOf { it.versionSum() }
    }

    fun evaluate(): Long {
      return when (type) {
        0 -> subpackets.sumOf { it.evaluate() }
        1 -> {
          var product = 1L
          subpackets.forEach { product *= it.evaluate() }
          product
        }
        2 -> subpackets.minOf { it.evaluate() }
        3 -> subpackets.maxOf { it.evaluate() }
        5 -> if (subpackets[0].evaluate() > subpackets[1].evaluate()) 1 else 0
        6 -> if (subpackets[0].evaluate() < subpackets[1].evaluate()) 1 else 0
        7 -> if (subpackets[0].evaluate() == subpackets[1].evaluate()) 1 else 0
        else -> literal ?: 0L
      }
    }
  }

  fun binaryToInt(binaryInput: List<Boolean>): Int {
    return binaryInput.joinToString(separator = "") { if (it) "1" else "0" }.toInt(2)
  }

  fun parsePacket(binaryInput: List<Boolean>): Pair<Packet, List<Boolean>> {
    val type = binaryToInt(binaryInput.slice(3..5))
    if (type == 4) {
      var index = 6
      var literal = ""
      var end = false
      while (index < binaryInput.lastIndex && !end) {
        literal +=
            binaryInput.slice(index + 1..index + 4).joinToString(separator = "") {
              if (it) "1" else "0"
            }
        if (binaryInput[index] == false) end = true
        index += 5
      }
      return Pair(
          Packet(
              binaryToInt(binaryInput.slice(0..2)),
              type,
              null,
              null,
              literal.toLong(2),
              emptyList()
          ),
          binaryInput.slice(index..binaryInput.lastIndex)
      )
    }

    val lengthTypeID = binaryInput[6]
    if (lengthTypeID) {
      val length = binaryToInt(binaryInput.slice(7..17))
      var input = binaryInput.slice(18..binaryInput.lastIndex)
      val subpackets: MutableList<Packet> = mutableListOf()
      repeat(length) {
        val (p, r) = parsePacket(input)
        input = r
        subpackets.add(p)
      }
      return Pair(
          Packet(
              binaryToInt(binaryInput.slice(0..2)),
              type,
              lengthTypeID,
              length,
              null,
              subpackets
          ),
          input
      )
    }

    val length = binaryToInt(binaryInput.slice(7..21))
    var input = binaryInput.slice(22..(21 + length))
    val subpackets: MutableList<Packet> = mutableListOf()
    while (input.isNotEmpty()) {
      val (p, r) = parsePacket(input)
      input = r
      subpackets.add(p)
    }
    return Pair(
        Packet(binaryToInt(binaryInput.slice(0..2)), type, lengthTypeID, length, null, subpackets),
        binaryInput.slice(22 + length..binaryInput.lastIndex)
    )
  }

  fun hexaToBinary(hexaInput: String): List<Boolean> {
    val binaryOutput = MutableList(0) { false }
    hexaInput.forEach { letter ->
      "$letter".toInt(16).toString(2).padStart(4, '0').forEach { binaryOutput.add(it == '1') }
    }
    return binaryOutput
  }

  fun part1(input: String): Int {
    val packet = parsePacket(hexaToBinary(input))
    val output = packet.first.versionSum()
    // println("$output $packet")
    return output
  }

  fun part2(input: String): Long {
    val packet = parsePacket(hexaToBinary(input))
    val output = packet.first.evaluate()
    // println("$output")
    return output
  }

  // test if implementation meets criteria from the description, like:
  check(part1("D2FE28") == 6)
  check(part1("38006F45291200") == 9)
  check(part1("EE00D40C823060") == 14)

  check(part1("8A004A801A8002F478") == 16)
  check(part1("620080001611562C8802118E34") == 12)
  check(part1("C0015000016115A2E0802F182340") == 23)
  check(part1("A0016C880162017C3686B18A3D4780") == 31)

  check(part2("C200B40A82") == 3L)
  check(part2("04005AC33890") == 54L)
  check(part2("880086C3E88112") == 7L)
  check(part2("CE00C43D881120") == 9L)
  check(part2("D8005AC2A8F0") == 1L)
  check(part2("F600BC2D8F") == 0L)
  check(part2("9C005AC2F8F0") == 0L)
  check(part2("9C0141080250320F1802104A08") == 1L)

  val input = readInput("16")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
