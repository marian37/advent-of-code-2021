import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun replaceAt(input: String, pos: Int, replaceWith: Char): String {
    return "${input.slice(0..pos-1)}$replaceWith${input.slice(pos+1..input.lastIndex)}"
  }

  data class State(
      val hallway: String,
      val rooms: List<String>,
  ) {
    override fun toString(): String {
      return "$hallway|${rooms[0]}${rooms[1]}${rooms[2]}${rooms[3]}"
    }

    fun part1ToPart2(): State {
      return State(
          hallway,
          listOf(
              "${rooms[0][0]}DD${rooms[0][1]}",
              "${rooms[1][0]}CB${rooms[1][1]}",
              "${rooms[2][0]}BA${rooms[2][1]}",
              "${rooms[3][0]}AC${rooms[3][1]}"
          )
      )
    }

    fun getPositions(): Map<String, String> {
      val visited = mutableMapOf<Char, Int>('A' to 0, 'B' to 0, 'C' to 0, 'D' to 0)
      val positions = mutableMapOf<String, String>()
      for (i in 0 until hallway.length) {
        val l = hallway[i]
        if (l != '.') {
          val k = "$l${visited[l]}"
          visited[l] = visited[l]?.inc()!!
          positions.put(k, "H$i")
        }
      }
      for (i in rooms.indices) for (j in rooms[i].indices) {
        val l = rooms[i][j]
        if (l != '.') {
          val k = "$l${visited[l]}"
          visited[l] = visited[l]?.inc()!!
          positions.put(k, "${"ABCD"[i]}$j")
        }
      }
      return positions
    }

    fun move(amp: String, from: String, to: String): State {
      var newHallway = hallway
      val newRooms = rooms.toMutableList()
      when (from[0]) {
        'H' -> {
          val idx = from[1].digitToInt()
          newHallway = "${newHallway.slice(0..idx-1)}.${newHallway.slice(idx+1..6)}"
        }
        else -> {
          val roomIdx = "ABCD".indexOf(from[0])
          newRooms[roomIdx] = replaceAt(newRooms[roomIdx], from[1].digitToInt(), '.')
        }
      }
      when (to[0]) {
        'H' -> {
          val idx = to[1].digitToInt()
          newHallway = "${newHallway.slice(0..idx-1)}${amp[0]}${newHallway.slice(idx+1..6)}"
        }
        else -> {
          val roomIdx = "ABCD".indexOf(to[0])
          newRooms[roomIdx] = replaceAt(newRooms[roomIdx], to[1].digitToInt(), amp[0])
        }
      }
      return State(newHallway, newRooms)
    }

    fun checkPath(from: String, to: String, amp: String): Boolean {
      val hall = if (from[0] == 'H') from else to
      val room = if (from[0] == 'H') to else from
      val roomIdx = "ABCD".indexOf(room[0])

      // do not allow to go to different room
      if (to == room && room[0] != amp[0]) return false
      // do not allow to leave room if already in correct
      if (from == room &&
              from[0] == amp[0] &&
              rooms[roomIdx].slice(from[1].digitToInt()..rooms[roomIdx].lastIndex).all {
                it == amp[0]
              }
      )
          return false
      // do not allow to jump over in room when leaving
      if (from == room && rooms[roomIdx].slice(0..from[1].digitToInt() - 1).any { it != '.' })
          return false

      val hIdx = hall[1].digitToInt()
      // allow to go only to the bottom of room without jumping
      if (to == room &&
              rooms[roomIdx] !=
                  "${".".repeat(to[1].digitToInt()+1)}${"${amp[0]}".repeat(rooms[roomIdx].length-to[1].digitToInt()-1)}"
      )
          return false
      val hSubstring =
          when (roomIdx) {
            0 -> if (hIdx <= 1) hallway.slice(hIdx + 1..1) else hallway.slice(2..hIdx - 1)
            1 -> if (hIdx <= 2) hallway.slice(hIdx + 1..2) else hallway.slice(3..hIdx - 1)
            2 -> if (hIdx <= 3) hallway.slice(hIdx + 1..3) else hallway.slice(4..hIdx - 1)
            3 -> if (hIdx <= 4) hallway.slice(hIdx + 1..4) else hallway.slice(5..hIdx - 1)
            else -> ""
          }
      if (hSubstring.any { it != '.' } || (to == hall && hallway[hIdx] != '.')) return false
      // println("CheckPath: $from $to $amp $hall $room $roomIdx ${rooms[roomIdx]} $this")
      return true
    }
  }

  fun getFinalState(size: Int): State {
    return State(
        ".......",
        listOf("A".repeat(size), "B".repeat(size), "C".repeat(size), "D".repeat(size))
    )
  }

  fun getMoves(size: Int) =
      mutableMapOf<String, Map<String, Int>>()
          .also {
            it.putAll(
                (0 until size).map {
                  "A${it}" to
                      mapOf(
                          "H0" to 3 + it,
                          "H1" to 2 + it,
                          "H2" to 2 + it,
                          "H3" to 4 + it,
                          "H4" to 6 + it,
                          "H5" to 8 + it,
                          "H6" to 9 + it
                      )
                }
            )
            it.putAll(
                (0 until size).map {
                  "B${it}" to
                      mapOf(
                          "H0" to 5 + it,
                          "H1" to 4 + it,
                          "H2" to 2 + it,
                          "H3" to 2 + it,
                          "H4" to 4 + it,
                          "H5" to 6 + it,
                          "H6" to 7 + it
                      )
                }
            )
            it.putAll(
                (0 until size).map {
                  "C${it}" to
                      mapOf(
                          "H0" to 7 + it,
                          "H1" to 6 + it,
                          "H2" to 4 + it,
                          "H3" to 2 + it,
                          "H4" to 2 + it,
                          "H5" to 4 + it,
                          "H6" to 5 + it
                      )
                }
            )
            it.putAll(
                (0 until size).map {
                  "D${it}" to
                      mapOf(
                          "H0" to 9 + it,
                          "H1" to 8 + it,
                          "H2" to 6 + it,
                          "H3" to 4 + it,
                          "H4" to 2 + it,
                          "H5" to 2 + it,
                          "H6" to 3 + it
                      )
                }
            )
          }
          .toMap()

  fun getMovesR(moves: Map<String, Map<String, Int>>): Map<String, Map<String, Int>> =
      (0..6).map { "H$it" }.associateWith { h ->
        moves.keys.associateWith { k -> moves[k]?.get(h) ?: 0 }
      }

  fun parseInput(input: List<String>): State {
    val hallway = "......."
    val rooms = mutableListOf<String>()
    repeat(4) { i -> rooms.add("${input[2][3 + 2 * i]}${input[3][3 + 2 * i]}") }
    return State(hallway, rooms)
  }

  fun getGraph(initialState: State, size: Int): Map<String, Map<String, Int>> {
    val queue = mutableListOf<State>()
    val graph = mutableMapOf<String, MutableMap<String, Int>>()
    queue.add(initialState)

    val moves = getMoves(size)
    val movesR = getMovesR(moves)

    while (queue.isNotEmpty()) {
      val state = queue.removeFirst()
      if (graph[state.toString()] != null) continue

      graph[state.toString()] = mutableMapOf<String, Int>()
      val currentPositions = state.getPositions()
      var canGoHome = false

      for (a in currentPositions.keys) {
        if (canGoHome) break
        val coeff =
            when (a[0]) {
              'A' -> 1
              'B' -> 10
              'C' -> 100
              'D' -> 1000
              else -> 0
            }
        val pos = currentPositions[a] ?: ""

        val movesMap = if (pos[0] == 'H') movesR else moves

        for ((n, d) in movesMap[pos] ?: emptyMap()) {
          if (canGoHome) break
          val possible = state.checkPath(pos, n, a)
          if (possible) {
            canGoHome = pos[0] == 'H'
            val newState = state.move(a, pos, n)
            graph[state.toString()]?.put(newState.toString(), coeff * d)
            queue.add(newState)
          }
        }
      }
    }

    return graph
  }

  fun findPath(
      parents: Map<String, Pair<String?, Int?>>,
      initialState: String,
      finalState: String
  ): List<Pair<String?, Int?>> {
    var current: Pair<String?, Int?> = Pair(finalState, 0)
    val path = mutableListOf<Pair<String?, Int?>>()
    while (current.first != initialState) {
      path.add(0, current)
      current = parents[current.first!!]!!
    }
    path.add(0, current)
    return path
  }

  fun dijkstra(
      graph: Map<String, Map<String, Int>>,
      initialState: String,
      finalState: String
  ): Int {
    val d = mutableMapOf<String, Int>()
    graph.keys.forEach { s -> d.put(s, Int.MAX_VALUE) }
    d.put(initialState, 0)
    val parents = mutableMapOf<String, Pair<String?, Int?>>()

    val q = mutableSetOf<String>()
    q.addAll(graph.keys)
    while (q.isNotEmpty()) {
      if (q.size % 1000 == 0) println("Q: ${q.size}")
      val v = q.minByOrNull { u -> d[u] ?: Int.MAX_VALUE }!!
      q.remove(v)
      if (v == finalState) {
        println(findPath(parents, initialState, finalState).joinToString(separator = "\n"))
        return d[finalState] ?: Int.MAX_VALUE
      }
      for (w in graph[v]?.keys ?: emptyList()) {
        if (w !in q) continue
        // relax(v, w)
        val dv = d[v]!!
        val dw = d[w]!!
        val gvw = graph[v]?.get(w)!!
        if (dv + gvw < dw) {
          d[w] = dv + gvw
          parents[w] = Pair(v, gvw)
        }
      }
    }

    return d[finalState] ?: Int.MAX_VALUE
  }

  fun part1(input: List<String>): Int {
    val initialState = parseInput(input)
    val graph = getGraph(initialState, 2)
    val optimal = dijkstra(graph, initialState.toString(), getFinalState(2).toString())
    println("Optimal: $optimal")
    return optimal
  }

  fun part2(input: List<String>): Int {
    val initialState = parseInput(input).part1ToPart2()
    val graph = getGraph(initialState, 4)
    val optimal = dijkstra(graph, initialState.toString(), getFinalState(4).toString())
    println("Optimal: $optimal")
    return optimal
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("23_test")
  check(part1(testInput) == 12521)
  check(part2(testInput) == 44169)

  val input = readInput("23")
  println(part1(input))
  println(part2(input))
}

// command:
// kotlinc -include-runtime -d template.jar template.kt
// java -jar template.jar
