package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.enumerations.DataStatus
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.ui.activities.actualTabs.ActualNavigation
import com.itgates.ultra.pulpo.cira.ui.composeUI.ButtonFactory
import com.itgates.ultra.pulpo.cira.ui.composeUI.TextFactory
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Double
import kotlin.math.absoluteValue

@AndroidEntryPoint
class DetailingActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var slides: List<Slide> = listOf()
    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        SwitchUI()
//                        VideoView()
                    }
                }
            }
        }

        setObservers()
        loadSlidesData()
    }

    private fun setObservers() {
        cacheViewModel.slideData.observe(this@DetailingActivity) {
            slides = it
            dataStateFlow.value = DataStatus.DONE
        }
    }

    private fun loadSlidesData() {
        cacheViewModel.loadSlidesByPresentationId(PassedValues.detailingActivity_presentationId)
    }

    @Composable
    fun SwitchUI() {
        val status = dataStateFlow.collectAsState()
        when(status.value) {
            DataStatus.LOADING -> {}
            DataStatus.DONE -> { FullScreenSlides() }
            DataStatus.REFRESH -> { FullScreenSlides() }
            DataStatus.ERROR -> {}
            DataStatus.NO_DATA -> {}
        }
    }
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun FullScreenSlides() {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                    ButtonFactory(text = "   Finish Presentation   ") {
                        finish()
                    }
                }
            },
            content = { OnBoardingPager() }
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TrialOnBoardingPager() {
        val pageRemember = rememberPagerState(initialPage = 0)

        Column {
            HorizontalPager(state = pageRemember, pageCount = 6) { page ->
                val file = Utilities.getAssetFile(applicationContext, "${page + 1}.jpeg")
                if (file!!.exists()) {}
                CoilImage(file)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun OnBoardingPager() {
        val isIndexedShown = remember { mutableStateOf(false) }
        val pageRemember = rememberPagerState(initialPage = 0)

        val coroutineScope = rememberCoroutineScope()
        Row {
            AnimatedVisibility(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = SpringSpec(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                visible = isIndexedShown.value
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp)
                ) {
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = padding_8),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        items(slides.size) {
                            Box(modifier = Modifier
                                .height(50.dp)
                                .aspectRatio(1F)) {
                                ButtonFactory(text = it.toString()) {
                                    coroutineScope.launch {
                                        if (pageRemember.currentPage != it) {
                                            println("lllllllllllllllllllllllllllllllllllllllllllllllllllllllllll")
                                            println(it)
                                            pageRemember.scrollToPage(page = it)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box {
                HorizontalPager(state = pageRemember, pageCount = slides.size) { page ->
                    val folderName = "mySlides" // Specify the folder name

                    val slidesFolder = File(applicationContext.cacheDir, folderName)
                    var file = File(slidesFolder, "${slides[page].id}_${slides[page].filePath}")

                    // for html files if exists
                    val htmlFiles = ArrayList<File>()
                    if (slidesFolder.exists()) {
                        println("------------------------------ 6666666666666666666666666666666666")
                        println("------------------------------ $file")
                        println("------------------------------ ${applicationContext.cacheDir}")
                    }

                    if (slides[page].filePath.endsWith(".zip")) {
                        val fileName = slides[page].filePath
                        val fileExtension = ".zip"
                        val fileTitle = fileName.subSequence(0, fileName.length-fileExtension.length)
                        val destinationFile = File(
                            slidesFolder,
                            "${slides[page].id}_extracted_$fileTitle/$fileTitle"
                        )
                        file = destinationFile
                        for (subFile in destinationFile.listFiles()!!) {
                            println("~~~~~~~~~~~~~~~~~~~~~~~ :: --------------------------- $subFile")
                            if (subFile.extension == "html") {
                                htmlFiles.add(subFile)
                            }
                        }
                        for (subFile in htmlFiles) {
                            println("~~~~~~~~~~~~~~~~~~~~~~~ :: --------------------------- $subFile")
                        }
                    }

                    when(slides[page].slideType) {
                        "Image" -> CoilImage(file = file)
                        "Video" -> VideoView(file = file)
                        "PDF" -> PdfViewer(file = file)
                        "HTML" -> TrialHtmlViewer(file = file)
//                        "HTML" -> {
//                            LazyColumn {
//                                items(htmlFiles) {
//                                    HtmlViewer(file = it)
//                                }
//                            }
//                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(padding_8)
                        .size(padding_40)
                        .clip(ITGatesCircularCornerShape)
                        .background(ITGatesGreyColor)
                        .clickable { isIndexedShown.value = !isIndexedShown.value },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(padding_36)
                            .padding(padding_4),
                        painter = painterResource(R.drawable.show_list_icon),
                        contentDescription = "Icon",
                        tint = ITGatesPrimaryColor
                    )
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SpeedOnBoardingPager() {
        val desiredPage = remember { mutableStateOf(0) }
        val canScroll = remember { mutableStateOf(false) }
        val isIndexedShown = remember { mutableStateOf(false) }

        Row {
            AnimatedVisibility(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = SpringSpec(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                visible = isIndexedShown.value
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp)
                ) {
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = padding_8),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        items(slides.size) {
                            Box(modifier = Modifier
                                .height(50.dp)
                                .aspectRatio(1F)) {
                                ButtonFactory(text = it.toString()) {
                                    if (desiredPage.value != it) {
                                        desiredPage.value = it
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollableState { delta ->
                        println("111111111111111111111111333")
                        println(delta)
                        delta
                    }
                )
            ) {
                val folderName = "mySlides" // Specify the folder name

                val slidesFolder = File(applicationContext.cacheDir, folderName)
                var file = File(slidesFolder, "${slides[desiredPage.value].id}_${slides[desiredPage.value].filePath}")

                // for html files if exists
                val htmlFiles = ArrayList<File>()
                if (slidesFolder.exists()) {
                    println("------------------------------ 6666666666666666666666666666666666")
                    println("------------------------------ $file")
                    println("------------------------------ ${applicationContext.cacheDir}")
                }

                if (slides[desiredPage.value].filePath.endsWith(".zip")) {
                    val fileName = slides[desiredPage.value].filePath
                    val fileExtension = ".zip"
                    val fileTitle = fileName.subSequence(0, fileName.length-fileExtension.length)
                    val destinationFile = File(
                        slidesFolder,
                        "${slides[desiredPage.value].id}_extracted_$fileTitle/$fileTitle"
                    )
                    file = destinationFile
                    for (subFile in destinationFile.listFiles()!!) {
                        println("~~~~~~~~~~~~~~~~~~~~~~~ :: --------------------------- $subFile")
                        if (subFile.extension == "html") {
                            htmlFiles.add(subFile)
                        }
                    }
                    for (subFile in htmlFiles) {
                        println("~~~~~~~~~~~~~~~~~~~~~~~ :: --------------------------- $subFile")
                    }
                }

                when(slides[desiredPage.value].slideType) {
                    "Image" -> CoilImage(file = file)
                    "Video" -> VideoView(file = file)
                    "PDF" -> PdfViewer(file = file)
//                    "HTML" -> TrialHtmlViewer(file = file)
                    "HTML" -> {
                        LazyColumn {
                            items(htmlFiles) {
                                HtmlViewer(file = it)
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(padding_8)
                        .size(padding_40)
                        .clip(ITGatesCircularCornerShape)
                        .background(ITGatesGreyColor)
                        .clickable { isIndexedShown.value = !isIndexedShown.value },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(padding_36)
                            .padding(padding_4),
                        painter = painterResource(R.drawable.show_list_icon),
                        contentDescription = "Icon",
                        tint = ITGatesPrimaryColor
                    )
                }
            }
        }

//        LaunchedEffect(canScroll.value) {
//            // Scroll to a specific page when the composable is first composed
//            if (canScroll.value && pageRemember.currentPage != desiredPage.value) {
//                println("lllllllllllllllllllllllllllllllllllllllllllllllllllllllllll")
//                println("${desiredPage.value}")
//                val job = launch { pageRemember.animateScrollToPage(page = desiredPage.value) }
//                job.join()
//                canScroll.value = false
//            }
//        }
    }

    @Composable
    fun VideoView(file: File) {
        val context = LocalContext.current
        val url = file.toUri()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Purple500),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val exoPlayer = ExoPlayer.Builder(context).build()
//            val mediaItem = MediaItem.fromUri(Uri.parse(url))
                val mediaItem = MediaItem.fromUri(url)
                exoPlayer.setMediaItem(mediaItem)

                val playerView = StyledPlayerView(context)
                playerView.player = exoPlayer

                DisposableEffect(AndroidView(factory = { playerView })) {
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true

                    onDispose {
                        exoPlayer.release()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun CoilImage(file: File) {
        val context = LocalContext.current
        val url = file.toUri()

        val imgBitmap = BitmapFactory.decodeFile(file.absolutePath)

        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberImagePainter(
                data = file,
                builder = {

                }
            )

            Image(painter = painter, contentDescription = "Image", modifier = Modifier.fillMaxSize())
        }
    }

    @Composable
    fun PdfViewer(file: File) {
        val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        println("..................................................... ${pdfRenderer.pageCount}")

        val pageList = ArrayList<Bitmap>()
        for (pageNum in 0 until pdfRenderer.pageCount) {
            val bitmap: Bitmap = pdfRenderer.openPage(pageNum).run {
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                close()
                bmp
            }
            pageList.add(bitmap)
        }

        LazyColumn {
            items(pageList) {pageContent ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ITGatesPrimaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = pageContent.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(Color.White)
                    )
                }

            }
        }

        pdfRenderer.close()
        parcelFileDescriptor.close()
    }

    @Composable
    fun HtmlViewer(file: File) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(ITGatesPrimaryColor),
        ) {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        webViewClient = WebViewClient()
                    }
                },
                update = {
                    it.settings.javaScriptEnabled = true
                    it.settings.allowFileAccess = true

                    // to let the content fit the screen
                    it.settings.loadWithOverviewMode = true
                    it.settings.useWideViewPort = true

                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                    it.loadData(content, "text/html", "UTF-8")
                    it.loadUrl(file.absolutePath)

                }
            )
        }

    }

    @Composable
    fun TrialHtmlViewer(file: File) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(ITGatesPrimaryColor),
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                TextFactory(text = "Coming Soon (HTML)", size = 20.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

    }
}