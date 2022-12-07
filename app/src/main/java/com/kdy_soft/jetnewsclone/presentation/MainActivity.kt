package com.kdy_soft.jetnewsclone.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.kdy_soft.jetnewsclone.presentation.JetNewsApp
import com.kdy_soft.jetnewsclone.ui.theme.JetNewsCloneTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SystemWindow + TopAppBar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            JetNewsCloneTheme {
                JetNewsApp(
                    widthSizeClass = calculateWindowSizeClass(activity = this).widthSizeClass
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetNewsCloneTheme {
        Greeting("Android")
    }
}