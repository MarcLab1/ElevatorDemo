package com.elevator

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.Deque
import java.util.LinkedList
import java.util.Queue

fun main() {

    val e = DumbElevator()
    e.pressButtonEvent(1)
    e.pressButtonEvent(2)
    e.pressButtonEvent(3)
    e.pressButtonEvent(4)
}


sealed class ElevatorDirection{
    object DirectionUp : ElevatorDirection()
    object DirectionDown : ElevatorDirection()
    object DirectionNone : ElevatorDirection()
}

class DumbElevator {

    private val floor: MutableStateFlow<Int> = MutableStateFlow(1)
    private val isDoorOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isMoving: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val direction: MutableStateFlow<ElevatorDirection> = MutableStateFlow(ElevatorDirection.DirectionNone)


    private val queue: Queue<Int> = LinkedList()

    fun pressButtonEvent(newFloor: Int) {

        if (isMoving().not())
            makeMove(newFloor)

        //else add the event to the queue
        else
            addMoveToQueue(newFloor)
    }

    private fun makeMove(newFloor: Int) {

        runBlocking {
            isDoorOpen.value = false
            isMoving.value = true

            if(floor.value == newFloor){
                direction.value = ElevatorDirection.DirectionNone
            }
            else {
                if (floor.value < newFloor)
                    direction.value = ElevatorDirection.DirectionUp
                else if (floor.value > newFloor)
                    direction.value = ElevatorDirection.DirectionDown

                floor.value = newFloor
                isMoving.value = false
                isDoorOpen.value = true
                println("at floor: " + floor.value)
                delay(1000L)
            }
            if (!queue.isEmpty()) {
                makeMove(queue.remove())
            }
        }
    }

    private fun addMoveToQueue(newFloor: Int) {
        queue.add(newFloor)
    }

    private fun isMoving(): Boolean {
        return isMoving.value
    }

}
