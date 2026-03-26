package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

/**
 * アプリ共通のテキストコンポーネント。
 *
 * - [style]: サイズや行間など。省略するとコンテキストのスタイルを継承。
 * - [secondary]: true にすると outline カラー（補助テキスト用）。
 * - [bold]: true にすると FontWeight.Bold。
 */
@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    secondary: Boolean = false,
    bold: Boolean = false,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = if (bold) style.copy(fontWeight = FontWeight.Bold) else style,
        color = if (secondary) MaterialTheme.colorScheme.outline else Color.Unspecified,
        textAlign = textAlign,
    )
}
