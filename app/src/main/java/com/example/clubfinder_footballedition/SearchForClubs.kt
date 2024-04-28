package com.example.clubfinder_footballedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme

class SearchForClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GUI_Club_Search()
                }
            }
        }
    }
}

@Composable
fun GUI_Club_Search(){

    var searchTerm by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column(
                
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(value = searchTerm, onValueChange = { searchTerm = it })

                Button(onClick = {

                }) {
                    Text("Fetch Teams")
                }
            }




        }
        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = searchTerm
        )


    }
}

