package com.example.dailynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailynotes.data.local.NoteDatabase
import com.example.dailynotes.data.repository.NoteRepository
import com.example.dailynotes.ui.screens.HomeScreen
import com.example.dailynotes.ui.screens.NoteDetailScreen
import com.example.dailynotes.ui.theme.DailyNotesTheme
import com.example.dailynotes.ui.viewmodel.HomeViewModel
import com.example.dailynotes.ui.viewmodel.HomeViewModelFactory
import com.example.dailynotes.ui.viewmodel.NoteDetailViewModel
import com.example.dailynotes.ui.viewmodel.NoteDetailViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = NoteDatabase.getDatabase(this)
        val repository = NoteRepository(database.noteDao)
        
        setContent {
            DailyNotesTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        val homeViewModel: HomeViewModel = viewModel(
                            factory = HomeViewModelFactory(repository)
                        )
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToAddNote = { navController.navigate("note_detail/-1") },
                            onNavigateToEditNote = { id -> navController.navigate("note_detail/$id") }
                        )
                    }
                    composable(
                        "note_detail/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                        val detailViewModel: NoteDetailViewModel = viewModel(
                            factory = NoteDetailViewModelFactory(repository)
                        )
                        
                        LaunchedEffect(noteId) {
                            if (noteId != -1) {
                                detailViewModel.loadNote(noteId)
                            }
                        }

                        NoteDetailScreen(
                            viewModel = detailViewModel,
                            noteId = noteId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
