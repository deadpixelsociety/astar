package com.thedeadpixelsociety.astar

import java.util.*

typealias Heuristic = (AStar.Node, AStar.Node) -> Double

fun euclidean(node0: AStar.Node, node1: AStar.Node): Double {
    val x = node1.x - node0.x
    val y = node1.y - node0.y
    return Math.sqrt((x * x + y * y).toDouble())
}

fun manhattan(node0: AStar.Node, node1: AStar.Node): Double {
    return (Math.abs(node1.x - node0.x) + Math.abs(node1.y - node0.y)).toDouble()
}

class AStar(val map: String, val heuristic: Heuristic) {
    private val start = find(START)
    private val end = find(END)

    private val fringe = PriorityQueue<Node> { o1, o2 -> Math.signum(f(o1) - f(o2)).toInt() }
    private val closed = hashSetOf<Node>()

    private val gCosts = Array(map.length) { 0.0 }
    private val fCosts = Array(map.length) { 0.0 }

    fun search(): List<Node> {
        if (start == end) return listOf(start)

        g(start, 0.0)
        f(start, heuristic(start, end))
        fringe.add(start)

        while (!fringe.isEmpty()) {
            val current = fringe.poll() ?: break
            closed.add(current)
            if (current == end) return reconstructFrom(current)
            for (y in -1..1) {
                for (x in -1..1) {
                    if (x == 0 && y == 0) continue
                    val neighbor = Node(current.x + x, current.y + y).apply { parent = current }
                    if (closed.contains(neighbor) || !bound(neighbor) || blocked(neighbor)) continue
                    val g0 = g(current) + heuristic(current, neighbor)
                    if (!fringe.contains(neighbor)) fringe.add(neighbor)
                    else if (g0 >= g(neighbor)) continue
                    g(neighbor, g0)
                    f(neighbor, g0 + heuristic(neighbor, end))
                }
            }
        }

        return emptyList()
    }

    private fun index(x: Int, y: Int) = y * WIDTH + x

    private fun f(node: Node) = fCosts[index(node.x, node.y)]

    private fun f(node: Node, value: Double) {
        fCosts[index(node.x, node.y)] = value
    }

    private fun g(node: Node) = gCosts[index(node.x, node.y)]

    private fun g(node: Node, value: Double) {
        gCosts[index(node.x, node.y)] = value
    }

    private fun find(c: Char): Node {
        for (y in 0 until HEIGHT) {
            for (x in 0 until WIDTH) {
                if (map[index(x, y)] == c) return Node(x, y)
            }
        }

        return Node(-1, -1)
    }

    private fun bound(node: Node) = node.x >= 0 && node.y >= 0 && node.x < WIDTH && node.y < HEIGHT

    private fun blocked(node: Node) = map[index(node.x, node.y)] == SOLID

    private fun reconstructFrom(current: Node): List<Node> {
        val list = arrayListOf(current)
        var p = current.parent
        while (p != null) {
            list.add(p)
            p = p.parent
        }

        return list
    }

    data class Node(val x: Int, val y: Int) {
        var parent: Node? = null
    }
}