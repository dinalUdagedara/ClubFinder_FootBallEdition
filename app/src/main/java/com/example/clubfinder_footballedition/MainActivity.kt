package com.example.clubfinder_footballedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
    MainButtons()
}

@Composable
fun MainButtons(){
    Surface(

    )
    {
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Add Leagues to DB")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Search for Clubs by League")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Search for Clubs")
                }
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ClubFinder_FootBallEditionTheme {
        MainButtons()
    }
}