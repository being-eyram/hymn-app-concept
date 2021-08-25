package com.example.hymnappconcept


import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hymnappconcept.repository.HymnRepository
import com.example.hymnappconcept.viewmodels.HymnViewModel
import com.example.hymnappconcept.viewmodels.HymnViewModelFactory
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


@ExperimentalComposeUiApi
@Composable
fun HymnsListScreen(repository: HymnRepository, navController: NavController) {
    val viewModel: HymnViewModel = viewModel(factory = HymnViewModelFactory(repository))
    val hymns by viewModel.result.observeAsState()
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val appBarHeight = 120.dp
    val appBarHeightPx = with(LocalDensity.current) { appBarHeight.roundToPx().toFloat() }
    val appBarOffsetHeightPx = remember { mutableStateOf(0f) }
    var searchTerm by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = appBarOffsetHeightPx.value + delta
                appBarOffsetHeightPx.value = newOffset.coerceIn(-(appBarHeightPx / 2), 0f)
                return Offset.Zero
            }
        }
    }

    Scaffold(
        //bottomBar = {BottomBar()}
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            if (hymns != null) {
                LazyColumn(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .focusable(),
                    state = listState,
                    contentPadding = PaddingValues(top = appBarHeight, start = 24.dp, end = 24.dp)
                ) {
                    item { Label(label = "Hymns") }
                    items(hymns!!) { hymn ->
                        HymnCard(
                            hymnNum = hymn.id,
                            hymnLyrics = hymn.lyrics,
                            onClick = { navController.navigate("ContentScreen/${hymn.id}") }
                        )
                    }
                }
                if (listState.isScrollInProgress) {
                    focusRequester.requestFocus()
                }
            }
        }
        AppBar(
            appBarHeight = appBarHeight,
            appBarOffsetHeightPx,
            search = searchTerm,
            onSearchTermChange = {
                searchTerm = it
                coroutineScope.launch {
                    val searchIsRegex = it.matches("""\d+""".toRegex())
                    if (searchIsRegex) {
                        viewModel.search(it.toInt())
                    } else viewModel.search(it)
                }
            },
            onClearClick = {
                searchTerm = emptyString
                coroutineScope.launch {
                    viewModel.search(searchTerm)
                }
            }
        )
    }
}


@Composable
fun HymnCard(hymnNum: Int, hymnLyrics: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth()
            .clickable { onClick() }

    ) {
        Text(
            modifier = Modifier.paddingFromBaseline(top = 32.dp, bottom = 10.dp),
            text = "Methodist Hymn $hymnNum",
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = hymnLyrics,
            maxLines = 2,
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.subtitle2,
            color = Color(0xFF000000).copy(alpha = 0.6F)
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun AppBar(
    appBarHeight: Dp,
    appBarOffsetHeightPx: MutableState<Float>,
    search: String,
    onSearchTermChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(appBarHeight)
            .offset {
                IntOffset(
                    x = 0, y = appBarOffsetHeightPx.value.roundToInt()
                )
            },
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AppBarTitle(
                    modifier = Modifier
                        .alignByBaseline()
                        .weight(weight = 1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_light_mode),
                    contentDescription = "toogle light mode"
                )
            }
            SearchBox(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 10.dp)
                    .height(40.dp),
                "Search In Hymns",
                search = search,
                onSearchTermChange = onSearchTermChange,
                onClearClick = onClearClick
            )
        }
    }
}

@Composable
fun BottomBar() {

    BottomNavigation(backgroundColor = Color(0xFF4C4853)) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text("Favorite") },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White,
            selected = true,
            onClick = { }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.List, contentDescription = null) },
            label = { Text("List") },
            selected = false,
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White,
            onClick = { }
        )
    }
}

@Composable
fun Label(label: String) {
    Text(
        modifier = Modifier.paddingFromBaseline(40.dp),
        text = label.uppercase(Locale.US),
        style = MaterialTheme.typography.h6,
        color = Color.Gray
    )
}

@Composable
fun AppBarTitle(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = "Methodist Hymn App",
        style = MaterialTheme.typography.subtitle2.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}





