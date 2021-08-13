package com.example.hymnappconcept

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hymnappconcept.database.HymnEntity
import com.example.hymnappconcept.viewmodels.HymnContentViewModel

@Composable
fun HymnContentScreen(
    navController: NavController,
    clickedHymnId: Int,
    hymnContentViewModel: HymnContentViewModel
) {
    hymnContentViewModel.getHymn(clickedHymnId)
    val clickedHymn = hymnContentViewModel.result.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        ContentAppBar(
            title = when {
                clickedHymnId < 10 -> "MH00$clickedHymnId"
                clickedHymnId < 99 -> "MH0$clickedHymnId"
                else -> "MH$clickedHymnId"
            },
            onNavigationClick = { navController.navigateUp() },
            onTextSizeClick = {}
        )
        if (clickedHymn.value != null) {
            HymnContent(
                hymnTitle = clickedHymn.value!!.title,
                hymnLyrics = clickedHymn.value!!.lyrics
            )
        }
    }
}


@Composable
fun ContentAppBar(
    title: String,
    onNavigationClick: () -> Unit,
    onTextSizeClick: () -> Unit
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
                    .clickable(onClick = onNavigationClick)
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
                    .clickable(onClick = onTextSizeClick)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun HymnContent(hymnTitle: String, hymnLyrics: String) {
    val scrollState = rememberScrollState()
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
            style = MaterialTheme.typography.h5
        )

        Text(
            modifier = Modifier.paddingFromBaseline(56.dp),
            text = hymnLyrics,
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.padding(bottom = 56.dp))
    }
}



