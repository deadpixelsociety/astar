package com.thedeadpixelsociety.astar

import kotlin.system.measureNanoTime

const val WIDTH = 20
const val HEIGHT = 20
const val EMPTY = '.'
const val SOLID = '*'
const val START = 'O'
const val END = 'X'

fun main(args: Array<String>) {
    //@formatter:off
    val map =
            "********************" +
            "*..................*" +
            "*.O************....*" +
            "*.**...............*" +
            "*.*................*" +
            "*.*........*********" +
            "*.*................*" +
            "*.**************...*" +
            "***................*" +
            "*...****************" +
            "*.....*....*.......*" +
            "*......*....*......*" +
            "*.......*....*.....*" +
            "*........*....*....*" +
            "*.........*....*...*" +
            "*..........*....*..*" +
            "*...........*......*" +
            "*............*...X.*" +
            "*..................*" +
            "********************"
    //@formatter:on

    val path = arrayListOf<AStar.Node>()

    fun printMap() {
        for (y in 0 until HEIGHT) {
            for (x in 0 until WIDTH) {
                if (path.contains(AStar.Node(x, y))) print('#')
                else print(map[y * WIDTH + x])
            }

            println()
        }
    }

    printMap()

    println()
    println("Press any key to begin...")
    readLine()

    val ns = measureNanoTime {
        path.addAll(AStar(map, ::euclidean).search())
    }

    printMap()

    if (path.isEmpty()) println("NO PATH AVAILABLE!")

    println()
    println("Search took ${ns / 1000000L} milliseconds")
    println()

    readLine()
}
