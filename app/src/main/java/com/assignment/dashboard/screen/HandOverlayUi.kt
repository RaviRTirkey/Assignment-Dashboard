package com.assignment.dashboard.screen

import JointPoint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.dashboard.R

@Composable
fun HandOverlayUi(
    joints: List<JointPoint>,
    onJointClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center){
        BoxWithConstraints(
            modifier = modifier
                .aspectRatio(0.75f)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.hand_image), // Replace with your image
                contentDescription = "3D Hand Model",
                contentScale = ContentScale.Fit, // Crop ensures the image fills the Box perfectly
                modifier = Modifier.fillMaxSize()
            )

            val canvasWidth = maxWidth
            val canvasHeight = maxHeight

            joints.forEach { joint ->
                JointOverlay(
                    joint = joint,
                    containerWidth = canvasWidth,
                    containerHeight = canvasHeight,
                    onClick = { onJointClicked(joint.id) }
                )
            }

            Text(
                text = "Press On Circle\nto select the joint",
                fontSize = 12.sp,
                color = Color(0xFF757575), // TextGray
                lineHeight = 16.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun JointOverlay(
    joint: JointPoint,
    containerWidth: Dp,
    containerHeight: Dp,
    onClick: () -> Unit
) {
    val sizeFraction = if (joint.isMainJoint) 0.12f else 0.04f
    val dynamicSize = containerWidth * sizeFraction 

    val backgroundColor = if (joint.isMainJoint) {
        Color(0x4DD32F2F) 
    } else {
        Color(0xFF3B82F6) 
    }

    val borderColor = if (joint.isMainJoint) {
        Color(0x88D32F2F) 
    } else {
        Color.Transparent
    }

    val offsetX = (containerWidth * joint.xFraction) - (dynamicSize / 2)
    val offsetY = (containerHeight * joint.yFraction) - (dynamicSize / 2)

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(dynamicSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (joint.isMainJoint) 2.dp else 0.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable { onClick() } 
    )
}