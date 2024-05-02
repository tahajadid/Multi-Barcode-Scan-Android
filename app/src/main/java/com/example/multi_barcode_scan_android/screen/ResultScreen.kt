package com.example.multi_barcode_scan_android.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_barcode_scan_android.util.constants.actualValues

@Composable
fun ResultScreen() {
    Surface {
        ChoiceList()
    }
}

@Composable
fun ChoiceList() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        // content padding
        contentPadding =
            PaddingValues(
                start = 6.dp,
                top = 6.dp,
                end = 12.dp,
                bottom = 6.dp,
            ),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        content = {
            items(actualValues) { it ->
                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Canvas(
                        modifier =
                            Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .size(4.dp),
                    ) {
                        drawCircle(Color.Black)
                    }
                    Text(
                        text = if(it.length.equals(12)) it.chunked(2).joinToString("-") else it,
                        fontSize = 16.sp)
                }
            }
        },
    )
}
