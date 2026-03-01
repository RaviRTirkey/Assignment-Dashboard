package com.assignment.dashboard.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.assignment.dashboard.R
import com.assignment.dashboard.ui.theme.SidebarBlue

@Composable
fun SidebarUi(
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false,
    icons: List<Int>,
    iconSize: Dp,
    onIconClick: () -> Unit
) {

    if (isHorizontal) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(SidebarBlue),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.icon),
                contentDescription = "app icon",
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))

            icons.forEach { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )
            }

        }
    } else {
        Column(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .background(Color(0xFF72CBE6)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.icon),
                contentDescription = "app icon",
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.height(8.dp))

            icons.forEach { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                painter = painterResource(R.drawable.ic_hide_sidebar),
                contentDescription = "home",
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )

        }
    }
}