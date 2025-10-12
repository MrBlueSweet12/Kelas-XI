package com.mrbluesweet12.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mrbluesweet12.newsapp.navigation.HomePageScreen
import com.mrbluesweet12.newsapp.navigation.NewsArticleScreen
import com.mrbluesweet12.newsapp.navigation.AddNewsScreen
import com.mrbluesweet12.newsapp.navigation.EditNewsScreen
import com.mrbluesweet12.newsapp.navigation.LocalNewsDetailScreen
import com.mrbluesweet12.newsapp.ui.HomeScreen
import com.mrbluesweet12.newsapp.ui.NewsArticlePage
import com.mrbluesweet12.newsapp.ui.AddNewsScreen as AddNewsScreenComposable
import com.mrbluesweet12.newsapp.ui.EditNewsScreen as EditNewsScreenComposable
import com.mrbluesweet12.newsapp.ui.LocalNewsDetailScreen as LocalNewsDetailScreenComposable
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModel
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val newsViewModel: NewsViewModel = viewModel(
                        factory = NewsViewModelFactory(this@MainActivity)
                    )
                    
                    NavHost(
                        navController = navController,
                        startDestination = HomePageScreen
                    ) {
                        composable<HomePageScreen> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "News Now",
                                    color = Color.Red,
                                    fontSize = 25.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                )
                                HomeScreen(
                                    newsViewModel = newsViewModel,
                                    navController = navController
                                )
                            }
                        }
                        
                        composable<NewsArticleScreen> {
                            val args = it.toRoute<NewsArticleScreen>()
                            NewsArticlePage(url = args.url)
                        }
                        
                        composable<AddNewsScreen> {
                            AddNewsScreenComposable(
                                newsViewModel = newsViewModel,
                                navController = navController
                            )
                        }
                        
                        composable<EditNewsScreen> {
                            val args = it.toRoute<EditNewsScreen>()
                            EditNewsScreenComposable(
                                newsId = args.newsId,
                                newsViewModel = newsViewModel,
                                navController = navController
                            )
                        }
                        
                        composable<LocalNewsDetailScreen> {
                            val args = it.toRoute<LocalNewsDetailScreen>()
                            LocalNewsDetailScreenComposable(
                                newsId = args.newsId,
                                newsViewModel = newsViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
