package com.example.pinchzoomgesturesinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.pinchzoomgesturesinjetpackcompose.ui.theme.PinchZoomGesturesInJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PinchZoomGesturesInJetpackComposeTheme {

                // Get state for the scale of our image
                var scale by remember {
                    mutableStateOf(1f)
                }

                // Describe where our image currently is ()
                var offset by remember {
                    mutableStateOf(Offset.Zero)
                }

                var rotation by remember {
                    mutableStateOf(1f)
                }

                // BoxWithConstraints gives us access to its bounds (width, height)
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        // The ratio of its width to its height of my image
                        .aspectRatio(1280f / 959f)
                ) {

                    // Track the transformation state (zoom, pan, rotation) of the image
                    val state =
                        rememberTransformableState { zoomChange, panChange, rotationChange ->

                            // Update the scale, rotation, and offset variables based on the changes received
                            scale = (scale * zoomChange).coerceIn(1f, 5f)

                            rotation += rotationChange

                            val extraWidth = (scale - 1) * constraints.maxWidth
                            val extraHeight = (scale - 1) * constraints.maxHeight

                            val maxX = extraWidth / 2
                            val maxY = extraHeight / 2

                            offset = Offset(
                                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                            )
                        }
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            // allows you to apply transformations like scaling, rotation, and translation to the image
                            // based on the state variables.
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                rotationZ
                                translationX = offset.x
                                translationY = offset.y
                            }
                            .transformable(state)
                    )
                }

            }
        }
    }
}

