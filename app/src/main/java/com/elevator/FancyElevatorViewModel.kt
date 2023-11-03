package com.elevator

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

sealed class Direction {
    object Down : Direction()
    object Up : Direction()
    object None : Direction()
}

class FancyElevatorViewModel : ViewModel() {

    private val _currentFloor: MutableState<Int> = mutableStateOf(1)
    val currentFloor: State<Int> = _currentFloor
    private val isMoving: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isDoorOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _direction: MutableState<Direction> = mutableStateOf(Direction.None)
    val direction: State<Direction> = _direction
    private val buttonsPressed = mutableStateListOf<Int>()


    fun getIsButtonPressed(newFloor: Int): Boolean {
        return buttonsPressed.contains(newFloor)
    }

    fun buttonPressEvent(newFloor: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            buttonsPressed.add(newFloor)
            if (isMoving.value.not()) {
                goToFloor(newFloor)
            }
        }
    }

    fun openDoor() {

        isDoorOpen.value = true
        Log.i("TAG", "Opening door at floor ${_currentFloor.value}")
        timeDelayLong()
        isDoorOpen.value = false
        buttonsPressed.remove(_currentFloor.value)
        Log.i("TAG", "Closing door at floor ${_currentFloor.value}")
    }

    fun goToFloor(newFloor: Int) {

        isMoving.value = true
        //open the door we are here
        if (_currentFloor.value == newFloor) {
            openDoor()
            isMoving.value = false
            return
        }

        var nextFloor = newFloor
        while (buttonsPressed.isNotEmpty()) {
            if (_currentFloor.value < nextFloor) {
                _direction.value = Direction.Up
                moveUpFloor()
                handleDoWeOpenDoorAtCurrentFloor()
            } else if (_currentFloor.value > nextFloor) {
                _direction.value = Direction.Down
                moveDownFloor()
                handleDoWeOpenDoorAtCurrentFloor()
            } else {
                //currentFloor.value == newFloor, we need a new newFloor
                nextFloor = getNextFloor(newFloor)
            }
        }
        _direction.value = Direction.None
        isMoving.value = false
    }

    fun getNextFloor(newFloor: Int): Int {

        if (_direction.value == Direction.Up) {
            buttonsPressed.map { level ->
                if (level > newFloor)
                    return level
            }
            _direction.value = Direction.Down
            return buttonsPressed.first()
        } else {
            buttonsPressed.map { level ->
                if (level < newFloor)
                    return level
            }
            _direction.value = Direction.Up
            return buttonsPressed.first()
        }
    }

    fun handleDoWeOpenDoorAtCurrentFloor() {
        if (buttonsPressed.contains(_currentFloor.value)) {
            openDoor()
            buttonsPressed.remove(_currentFloor.value)
        }
    }

    fun timeDelayLong() {
        runBlocking {
            delay(1000L)
        }
    }

    fun timeDelayShort() {
        runBlocking {
            delay(500L)
        }
    }

    fun moveDownFloor() {
        timeDelayShort()
        _currentFloor.value--
        Log.i("TAG", "Elevator moves down to floor ${_currentFloor.value}")
    }

    fun moveUpFloor() {
        timeDelayShort()
        _currentFloor.value++
        Log.i("TAG", "Elevator moves up to floor ${_currentFloor.value}")
    }
}