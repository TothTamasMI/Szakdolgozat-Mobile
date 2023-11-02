package com.example.szakdolgozat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.szakdolgozat.ui.theme.SzakdolgozatTheme

class AboutPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SzakdolgozatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayEverything()
                }
            }
        }
    }
}

@Composable
fun DisplayEverything() {
    val c1 = colorResource(id = R.color.blue)
    val c2 = colorResource(id = R.color.blue2)
    Column(modifier = Modifier
        .background(brush = Brush.horizontalGradient(listOf(c1, c2)))
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally){

        DisplayHeaderCard()
        DisplayDescriptionCard()
        DisplayGithubCard()
    }
}

@Composable
fun DisplayHeader(){
    val title = stringResource(id = R.string.about_page_title)
    val version = stringResource(id = R.string.about_page_version)

    val modifier = Modifier
        .background(color = Color.White)
        .padding(15.dp)
        .fillMaxWidth()

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally){
        DisplayText(text = title, modifier = modifier, size = 45.sp, textAlign = TextAlign.Center)
        DisplayText(text = version, modifier = modifier, textAlign = TextAlign.Center)
    }
}

@Composable
fun DisplayDescription(){
    val modifier = Modifier
        .background(color = Color.White)
        .padding(15.dp)

    DisplayText(modifier = modifier,text = stringResource(id = R.string.about_page_description))
}

@Composable
fun DisplayGithubLinks() {
    val mobile = stringResource(id = R.string.about_page_mobile)
    val arduino = stringResource(id = R.string.about_page_arduino)
    val context = LocalContext.current
    val modifier = Modifier
        .background(color = Color.White)
        .padding(15.dp)

    DisplayText(text = mobile, modifier = modifier
        .clickable {
            context.startActivity(getIntent(arduino = false))
        })
    DisplayText(text = arduino, modifier = modifier
        .clickable {
            context.startActivity(getIntent(arduino = true))
        })
}

@Composable
fun DisplayHeaderCard(){
    val modifier = Modifier
        .background(
            color = Color.Transparent
        )
        .padding(20.dp)
    DisplayCard(modifier = modifier, displayHeader = true)

}

@Composable
fun DisplayDescriptionCard(){
    val modifier = Modifier
        .background(
            color = Color.Transparent
        )
        .padding(20.dp)
    DisplayCard(modifier = modifier, displayDescription = true)
}

@Composable
fun DisplayGithubCard() {
    val modifier = Modifier
        .background(
            color = Color.Transparent
        )
        .padding(20.dp)
    DisplayCard(modifier = modifier, displayGithubLinks = true)
}

@Composable
fun DisplayCard(
    modifier: Modifier,
    displayGithubLinks: Boolean = false,
    displayDescription: Boolean = false,
    displayHeader: Boolean = false,
    shape: Shape = RoundedCornerShape(20.dp)
){
    Card(modifier = modifier, shape = shape){

        if(displayGithubLinks){
            DisplayGithubLinks()
        } else if (displayDescription){
            DisplayDescription()
        } else if (displayHeader){
            DisplayHeader()

        }
    }
}

@Composable
fun DisplayText(modifier: Modifier = Modifier, text: String = "",textAlign: TextAlign = TextAlign.Left, size: TextUnit = 20.sp, color : Color = colorResource(id = R.color.blue)){
    Text(text = text,
        modifier = modifier.background(Color.White),
        textAlign = textAlign,
        fontSize = size,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    SzakdolgozatTheme {
        DisplayEverything()
    }
}

fun getIntent(arduino : Boolean = false): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    if (arduino){
        intent.data = Uri.parse("https://github.com/TothTamasMI/Szakdolgozat-Arduino")
    } else {
        intent.data = Uri.parse("https://github.com/TothTamasMI/Szakdolgozat-Mobile")
    }
    return intent
}