package com.elevator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevator.ui.theme.ElevatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElevatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    FancyElevatorScreen()
                }
            }
        }
    }
}

@Composable
fun ElevatorButton(floor: Int, buttonPressedLight: Boolean, onButtonClick: (Int) -> Unit) {

    Button(
        onClick = { onButtonClick(floor) },
        modifier = Modifier.padding(3.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (buttonPressedLight) Color.Magenta else Color.DarkGray)

    ) {
        Text(
            floor.toString(),
        )
    }
}

@Composable
fun ButtonsRow(
    start: Int,
    end: Int,
    getIsButtonPressed: (Int) -> Boolean,
    buttonPressEvent: (Int) -> Unit
) {
    Row {
        (start until end + 1).map {
            ElevatorButton(
                floor = it,
                buttonPressedLight = getIsButtonPressed(it),
                onButtonClick = { buttonPressEvent(it) })
        }
    }
}

@Composable
fun UpDownButtons(label : String, shouldShow: Boolean){
    Text(
        label,
        modifier = Modifier.alpha(if (shouldShow) 1.0f else 0.0f)
    )
}

@Composable
fun FancyElevatorScreen() {

    val fancyElevatorViewModel: FancyElevatorViewModel = viewModel()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UpDownButtons(label = " ^^^ ", shouldShow = fancyElevatorViewModel.direction.value == Direction.Up)
            Text(fancyElevatorViewModel.currentFloor.value.toString(), style = MaterialTheme.typography.headlineLarge)
            UpDownButtons(label = " vvv ", shouldShow = fancyElevatorViewModel.direction.value == Direction.Down)
        }
        ButtonsRow(
            start = 7,
            end = 9,
            getIsButtonPressed =  fancyElevatorViewModel::getIsButtonPressed,
            buttonPressEvent =  fancyElevatorViewModel::buttonPressEvent)
        ButtonsRow(
            start = 4,
            end = 6,
            getIsButtonPressed = fancyElevatorViewModel::getIsButtonPressed,
            buttonPressEvent =  fancyElevatorViewModel::buttonPressEvent)
        ButtonsRow(
            start = 1,
            end = 3,
            getIsButtonPressed = fancyElevatorViewModel::getIsButtonPressed,
            buttonPressEvent =  fancyElevatorViewModel::buttonPressEvent)
    }
}

@Preview
@Composable
fun ElevatorButtonPreviewPressed() {
    ElevatorTheme {
        ElevatorButton(floor = 1, buttonPressedLight = true, onButtonClick = {})
    }
}

@Preview
@Composable
fun ElevatorButtonPreviewUnpressed() {
    ElevatorTheme {
        ElevatorButton(floor = 1, buttonPressedLight = false, onButtonClick = {})
    }
}


