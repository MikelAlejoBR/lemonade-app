package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LemonadeApp()
                }
            }
        }
    }
}

@Composable
@Preview
fun LemonadeApp() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var currentState by remember { mutableStateOf(State.TREE) }
        var timeToTap by remember { mutableIntStateOf(0) }

        val imageResource = when (currentState) {
            State.TREE -> R.drawable.lemon_tree
            State.SQUEEZING, State.KEEP_SQUEEZING -> R.drawable.lemon_squeeze
            State.DRINKING -> R.drawable.lemon_drink
            else -> R.drawable.lemon_restart
        }

        val textResource = when(currentState) {
            State.TREE -> stringResource(R.string.lemonade_tap)
            State.SQUEEZING -> stringResource(R.string.lemonade_keep_tapping)
            State.KEEP_SQUEEZING -> stringResource(R.string.lemonade_keep_squeezing)
            State.DRINKING -> stringResource(R.string.lemonade_drink)
            else -> stringResource(R.string.lemonade_empty)
        }

        Image(
            painter = painterResource(imageResource),
            modifier = Modifier
                .wrapContentSize()
                .padding(15.dp)
                .clickable {
                    if (currentState == State.SQUEEZING || currentState == State.KEEP_SQUEEZING) {
                        if (timeToTap < 2) {
                            currentState = State.KEEP_SQUEEZING
                            timeToTap++
                        } else {
                            timeToTap = 0
                            currentState = changeState(currentState)
                        }
                    } else {
                        currentState = changeState(currentState);
                    }
                },
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = textResource)
    }
}

fun changeState(currentState: State): State {
    return when (currentState) {
        State.TREE -> State.SQUEEZING
        State.SQUEEZING, State.KEEP_SQUEEZING -> State.DRINKING
        State.DRINKING -> State.EMPTY
        else -> State.TREE
    }
}
