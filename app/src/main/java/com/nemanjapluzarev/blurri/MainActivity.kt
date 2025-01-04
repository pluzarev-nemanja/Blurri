package com.nemanjapluzarev.blurri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.nemanjapluzarev.blurri.core.BlurriImage
import com.nemanjapluzarev.blurri.ui.theme.BlurriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlurriTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize()) {

                        BlurriImage(
                            model = "https://images.freeimages.com/image/previews/762/sunset-cowboy-ride-5690199.jpg?fmt=webp&h=350",
                            contentDescription = "",
                            modifier = Modifier.size(550.dp),
                            blurRadius = 4f,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
        }
    }
}