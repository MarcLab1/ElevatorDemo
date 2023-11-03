package com.elevator

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class ElevatorOk {
    private var currentFloor = 1
    private val destinationFloors: MutableList<Int>


    init {
        destinationFloors = ArrayList()
    }

    fun addDestination(floor: Int) {
        if (floor != currentFloor) {
            destinationFloors.add(floor)
        }
    }

    fun start() {
        while (!destinationFloors.isEmpty()) {
            val nextFloor = destinationFloors.first()
            if (nextFloor > currentFloor) {
                println("Moving up to floor $nextFloor")
                runBlocking { delay(1000L) }
                currentFloor = nextFloor
            } else if (nextFloor < currentFloor) {
                println("Moving down to floor $nextFloor")
                runBlocking { delay(1000L) }
                currentFloor = nextFloor
            }
            destinationFloors.removeFirst()
            println("Elevator is now on floor $currentFloor")
        }
        println("Elevator is idle on floor $currentFloor")
    }
}


fun main(args: Array<String>) {
    val elevator = ElevatorOk()
    elevator.addDestination(5)
    elevator.addDestination(2)
    elevator.addDestination(10)
    elevator.addDestination(3)
    elevator.addDestination(1)
    elevator.start()
}
