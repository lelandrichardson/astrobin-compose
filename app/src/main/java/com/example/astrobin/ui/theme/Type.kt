package com.example.astrobin.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
  h1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    letterSpacing = (-1.5).sp
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
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
  ),
  body1 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
  ),
)