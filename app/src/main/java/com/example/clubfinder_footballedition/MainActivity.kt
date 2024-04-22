package com.example.clubfinder_footballedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

//@Composable
//fun MainButtons(){
//    Surface(
//        modifier = Modifier.fillMaxSize()
//    )
//    {
//        Row() {
//            Image(painter = painterResource(id = R.drawable.coverimage2),
//                contentDescription = "Cover Image" ,
//                modifier = Modifier
//                    .fillMaxWidth()  // Make the image fill the entire width of the parent
//                    .height(200.dp)
//            )
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(24.dp)
//                ,
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Button(onClick = { /*TODO*/ }) {
//                    Text(text = "Add Leagues to DB")
//                }
//                Button(onClick = { /*TODO*/ }) {
//                    Text(text = "Search for Clubs by League")
//                }
//                Button(onClick = { /*TODO*/ }) {
//                    Text(text = "Search for Clubs")
//                }
//            }
//
//        }
//
//    }
//
//}
@Composable
fun MainButtons(){
    Surface(
        modifier = Modifier.fillMaxSize()
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.coverimage5),
                contentDescription = "Cover Image",
                modifier = Modifier
                    .fillMaxWidth()  // Make the image fill the entire width of the parent
                    .height(600.dp)  // Set the height of the image to a fixed value
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp) // Set a fixed height for all buttons

                ) {
                    Text(text = "Add Leagues to DB")
                }
                Spacer(modifier = Modifier.height(4.dp)) // Add space between buttons
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp) // Set a fixed height for all buttons
                ) {
                    Text(text = "Search for Clubs by League")
                }
                Spacer(modifier = Modifier.height(4.dp)) // Add space between buttons
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp) // Set a fixed height for all buttons
                ) {
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