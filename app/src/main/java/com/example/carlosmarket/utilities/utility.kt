@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.carlosmarket.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.carlosmarket.ui.theme.Green
import com.example.carlosmarket.ui.theme.Yellow

@Composable
fun TopBar(
    topBarOnClick: () -> Unit,
    topBarButton: Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = "Carlos Market",
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Yellow,
            titleContentColor = Color.Black
        ),
        navigationIcon = {
            if (topBarButton) {
                IconButton(
                    onClick = { topBarOnClick() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        }
    )
}

@Composable
fun FloatButton(floatOnClick: () -> Unit) {
    FloatingActionButton(
        onClick = { floatOnClick() },
        containerColor = Yellow
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

@Composable
fun BottomBar(deleteOnClick: () -> Unit) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = { deleteOnClick() },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "delete"
                )
            }
        },
        containerColor = Green
    )

}