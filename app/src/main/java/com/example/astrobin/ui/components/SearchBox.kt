package com.example.astrobin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astrobin.ui.theme.DarkBlue

@Composable
fun SearchBox(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    maxLines = 1,
    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
    cursorBrush = SolidColor(Color.White),
    decorationBox = { textField ->
      Box(
        Modifier
          .background(DarkBlue, RoundedCornerShape(24.dp))
          .padding(18.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = "Search",
          tint = Color.White,
          modifier = Modifier
            .align(Alignment.CenterStart)
            .size(24.dp)
        )
        Box(
          Modifier
            .align(Alignment.CenterStart)
            .padding(start = 36.dp)
            .fillMaxWidth()
        ) {
          textField()
        }
      }
    }
  )
}