package com.example.astrobin.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.astrobin.R

val nunito = FontFamily(
  Font(R.font.nunito_regular),
)

val lato = FontFamily(
  Font(R.font.lato_regular),
)

// Set of Material typography styles to start with
val Typography = Typography(
  defaultFontFamily = nunito,
  h1 = TextStyle(
    fontWeight = FontWeight.ExtraBold,
    fontSize = 24.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.5).sp
  ),
  h2 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    letterSpacing = (-0.5).sp
  ),
  h3 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    letterSpacing = 0.sp
  ),
//  h4 = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 34.sp,
//    letterSpacing = 0.25.sp
//  ),
//  h5 = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 24.sp,
//    letterSpacing = 0.sp
//  ),
  subtitle1 = TextStyle(
    fontFamily = lato,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 17.sp,
    letterSpacing = 0.15.sp
  ),
  subtitle2 = TextStyle(
    fontFamily = lato,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.15.sp
  ),
  body1 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
  ),
  caption = TextStyle(
    fontWeight = FontWeight.ExtraBold,
    fontSize = 12.sp,
    letterSpacing = 0.5.sp
  ),
)