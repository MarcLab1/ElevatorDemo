package com.elevator

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

fun main() {
    val e: ElevatorBetter = ElevatorBetter()

    e.buttonPush(10)
    e.buttonPush(4)
    e.buttonPush(7)
    e.buttonPush(1)

    e.start()
}

class ElevatorBetter {

    private val floor: MutableStateFlow<Int> = MutableStateFlow(1)
    private val floorsToVisit: ArrayList<Int> = ArrayList()

    fun buttonPush(newFloor: Int) {
        floorsToVisit.add(newFloor)
    }

    fun handleNewFloor() {
        if (floorsToVisit.contains(floor.value)) {
            println("Door opening on floor: ${floor.value}")
            floorsToVisit.remove(floor.value)
        } else
            println("1. Now on floor ${floor.value}")
    }

    fun start() {

        while (!floorsToVisit.isEmpty()) {

            runBlocking { delay(500L) }

            if(floorsToVisit.contains(floor.value)) {
                floorsToVisit.remove(floor.value)
            }
            if (floor.value > floorsToVisit.first()) {
                floor.value--
                handleNewFloor()
            } else if (floor.value < floorsToVisit.first()) {
                floor.value++
                handleNewFloor()
            } else {
                println("error?")
            }
        }
    }
}