package com.example.clubfinder_footballedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme

class SearchingClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    Text(text = "Hello $name!")
}

@Composable
fun Content (){

    var clubName by rememberSaveable{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = clubName,
            onValueChange = {
                clubName = it
            },
            label = { Text(text = "Enter League Name")}
            )


        Button(onClick = { /*TODO*/ }) {
            Text(text = "Retrieve Clubs")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = " Save clubs to Database")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ClubFinder_FootBallEditionTheme {
        Greeting("Android")
    }
}