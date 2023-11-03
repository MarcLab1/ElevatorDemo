package com.elevator

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.Deque
import java.util.LinkedList

fun main(){
    val e : BetterElevator = BetterElevator()
    e.pressButtonEvent(ElevatorEvent.GoToFloor(4))
    e.pressButtonEvent(ElevatorEvent.GoToFloor(2))
    e.pressButtonEvent(ElevatorEvent.GoToFloor(10))
    e.pressButtonEvent(ElevatorEvent.GoToFloor(7))
}
sealed class ElevatorEvent {
    data class GoToFloor(val floor: Int) : ElevatorEvent()
    object DoorOpen : ElevatorEvent()
    object DoorClose : ElevatorEvent()
}

class BetterElevator {

    private val events: MutableSharedFlow<ElevatorEvent> = MutableSharedFlow()
    private val currentFloor: MutableStateFlow<Int> = MutableStateFlow(1)
    private val moves: MutableList<Int> = mutableListOf()
    private val direction: MutableStateFlow<ElevatorDirection> =
        MutableStateFlow(ElevatorDirection.DirectionUp)
    private val isMoving : MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun pressButtonEvent(event: ElevatorEvent) {
        when (event) {
            is ElevatorEvent.GoToFloor -> {
                putFloorInQueue(event.floor)
            }

            is ElevatorEvent.DoorOpen -> {

            }

            is ElevatorEvent.DoorClose -> {

            }

            else -> {}
        }
    }

    private fun putFloorInQueue(floor: Int) {
        if (isMoving.value == false)
            moveToFloor(floor)
        //the elevator is moving, add the button press to the queue
        else{
            moves.add(floor)
        }

        while(moves.isNotEmpty() && isMoving.value == false){
            moveToFloor(moves.removeFirst())
        }
    }


    private fun moveToFloor(floor: Int) {
        if (currentFloor.value == floor) {
            println("already on floor $floor")
        } else if (currentFloor.value < floor) {
            println("moving up to $floor")
            direction.value = ElevatorDirection.DirectionUp
            isMoving.value = true
            runBlocking {  delay(1000L) }
            currentFloor.value = floor
            direction.value = ElevatorDirection.DirectionNone
            isMoving.value = false
            println("at floor $floor")
        } else {
            println("moving down to $floor")
            direction.value = ElevatorDirection.DirectionDown
            isMoving.value = true
            runBlocking {  delay(1000L) }
            currentFloor.value = floor
            direction.value = ElevatorDirection.DirectionNone
            isMoving.value = false
            println("at floor $floor")
        }
    }
}