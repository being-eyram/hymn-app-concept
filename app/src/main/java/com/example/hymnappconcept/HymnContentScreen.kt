package com.example.hymnappconcept

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hymnappconcept.viewmodels.HymnContentViewModel


private val maxTitleFontSize = 50.sp
private val maxLyricsFontSize = 40.sp

@Composable
fun HymnContentScreen(
    navController: NavController,
    clickedHymnId: Int,
    hymnContentViewModel: HymnContentViewModel
) {
    hymnContentViewModel.getHymn(clickedHymnId)
    val clickedHymn = hymnContentViewModel.result.observeAsState()
    var sliderPosition by remember { mutableStateOf(0f) }
    var bodyTitleFontSize by remember { mutableStateOf(20.sp) }
    var lyricsFontSize by remember { mutableStateOf(16.sp) }
    var isTextSizeActionClicked: Boolean by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(indication = null, interactionSource = interactionSource) {
            if (isTextSizeActionClicked) {
                isTextSizeActionClicked = false
            }
        }) {

        Column(modifier = Modifier.fillMaxSize()) {
            ContentAppBar(
                title = getAppBarTitle(clickedHymnId),
                onNavigationActionClick = { navController.navigateUp() },
                onTextSizeActionClick = { isTextSizeActionClicked = !isTextSizeActionClicked }
            )
            clickedHymn.value?.let {
                HymnContent(
                    hymnTitle = it.title,
                    hymnLyrics = it.lyrics,
                    titleFontSize = bodyTitleFontSize,
                    bodyFontSize = lyricsFontSize,
                    scrollState = scrollState
                )
            }
        }

        if (scrollState.isScrollInProgress && isTextSizeActionClicked) {
            isTextSizeActionClicked = false
        }

        if (isTextSizeActionClicked) {
            SliderCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                sliderPosition = sliderPosition,
                onSliderPositionChange = { sliderPosition = it },
                onValueChangeFinished = {
                    bodyTitleFontSize = (maxTitleFontSize * sliderPosition) / 100f
                    lyricsFontSize = (maxLyricsFontSize * sliderPosition) / 100f
                }
            )
        }
    }
}


@Composable
fun ContentAppBar(
    title: String,
    onNavigationActionClick: () -> Unit,
    onTextSizeActionClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
    ) {
        Box(Modifier.fillMaxSize()) {
            Icon(
                Icons.Default.ArrowBack,
                "Navigate Up",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onNavigationActionClick)
                    .padding(16.dp)
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title
            )

            Icon(
                painterResource(id = R.drawable.ic_text_size),
                "Navigate Up",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onTextSizeActionClick)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun HymnContent(
    hymnTitle: String,
    hymnLyrics: String,
    titleFontSize: TextUnit,
    bodyFontSize: TextUnit,
    scrollState: ScrollState
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 24.dp, end = 24.dp),

        ) {
        Text(
            modifier = Modifier
                .paddingFromBaseline(28.dp),
            text = hymnTitle,
            textAlign = TextAlign.Center,
            style = typography.h5.copy(fontSize = titleFontSize)
        )

        Text(
            modifier = Modifier.paddingFromBaseline(56.dp),
            text = hymnLyrics,
            style = typography.body1.copy(fontSize = bodyFontSize)
        )

        Spacer(modifier = Modifier.padding(bottom = 56.dp))
    }
}

@Composable
fun SliderCard(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onSliderPositionChange: (sliderPosition: Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = modifier
            .height(88.dp)
            .width(312.dp)
            .shadow(elevation = 8.dp)
            .clip(MaterialTheme.shapes.small),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "FONT SIZE",
                modifier = Modifier
                    .paddingFromBaseline(top = 16.dp)
                    .align(Alignment.TopStart),
                style = typography.overline,
                color = Color.DarkGray
            )

            Row(Modifier.align(Alignment.Center)) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_minus),
                    contentDescription = "Reduce Font Size",
                )

                Slider(
                    modifier = Modifier.weight(1f),
                    value = sliderPosition,
                    valueRange = 50f..100f,
                    onValueChange = onSliderPositionChange,
                    onValueChangeFinished = onValueChangeFinished,
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "Reduce Font Size"
                )
            }
        }
    }
}

private fun getAppBarTitle(id: Int): String {
    return when {
        (id < 10) -> "MH00$id"
        (id < 99) -> "MH0$id"
        else -> "MH$id"
    }
}




