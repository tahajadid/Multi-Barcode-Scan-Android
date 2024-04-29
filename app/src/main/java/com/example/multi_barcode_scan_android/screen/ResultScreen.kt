package com.example.multi_barcode_scan_android.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_barcode_scan_android.MainActivity
import com.example.multi_barcode_scan_android.actualValues
import com.example.multi_barcode_scan_android.theme.MultiBarcodeScanAndroidTheme

@Composable
fun ResultScreen(navController: NavController) {

    Surface() {
        ChoiceList(navController)
    }
}

@Composable
fun ChoiceList(
    navController : NavController
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),

        // content padding
        contentPadding = PaddingValues(
            start = 6.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp
        ),
        content = {

            Log.d("TestValueMutt"," mutableListOf<String>() : "+actualValues)

            items(actualValues) { it ->

                Log.d("TestValueMutt"," index: "+it)

                Row(Modifier.padding(8.dp),verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .size(6.dp)){
                        drawCircle(Color.Black)
                    }
                    Text(text = it,fontSize = 16.sp)
                }

            }
        }
    )
}

@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WelcomeScreenPreview() {
    MultiBarcodeScanAndroidTheme {
        ChoiceList(NavController(MainActivity.activityInstance))
    }
}
