package com.sweatnote.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.sweatnote.app.data.WorkoutSessionWithDetails
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)){
    var tabs = listOf("Day", "Month")
    val pagerState = rememberPagerState(initialPage = 1) { tabs.size }
    val coroutineScope = rememberCoroutineScope()


    Scaffold (
        topBar = { TopAppBar(title = { Text("Workout History") }) }
    ){innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed{index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {coroutineScope.launch { pagerState.animateScrollToPage(index) }},
                        text = {Text(title)}
                    )
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) {page ->
                when(page){
                    0 -> DayView(viewModel)
                    1 -> MonthView(
                        viewModel = viewModel,
                        onDateClick = {date ->
                            viewModel.onDateSelected(date)
                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MonthView(viewModel: HistoryViewModel, onDateClick: (LocalDate) -> Unit) {
    val workoutDates by viewModel.workoutDates.collectAsState()
    val visibleMonth by viewModel.visibleMonth.collectAsState()

    val state = rememberCalendarState(
        startMonth = visibleMonth,
        endMonth = visibleMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale()
    )

    Column {
        MonthNavigationHeader(
            visibleMonth = visibleMonth,
            onPreviousMonth = { viewModel.goToPreviousMonth() },
            onNextMonth = { viewModel.goToNextMonth() }
        )

        val daysOfWeek = daysOfWeek()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    text = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Divider()

        VerticalCalendar(
            state = state,
            dayContent = { day ->
                DayCell(
                    day = day,
                    hasWorkout = day.date in workoutDates,
                    onDateClick = onDateClick
                )
            }
        )
    }
}

@Composable
fun MonthNavigationHeader(
    visibleMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
        }

        Text(
            text = visibleMonth.format(formatter),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
private fun DayCell(day: CalendarDay, hasWorkout: Boolean, onDateClick: (LocalDate) -> Unit){
    Box(
        modifier = Modifier.aspectRatio(1f)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {onDateClick(day.date)}
            ),
        contentAlignment = Alignment.Center
    ){
        if(hasWorkout){
            Box(
                modifier = Modifier.size(30.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
            )
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            color =
                if (hasWorkout) {
                    MaterialTheme.colorScheme.onPrimary
                }else if(day.position == DayPosition.MonthDate){
                    MaterialTheme.colorScheme.onSurface
                } else {
                    Color.Gray
                }
        )
    }
}

@Composable
fun DayView(viewModel: HistoryViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val sessionForDay by viewModel.sessionForSelectedDate.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item{
            Text(
                text = selectedDate.format(dateFormatter),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        if(sessionForDay.isEmpty()){
            item{
                Text(
                    text = "No workout logged for this day.",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }else{
            items(sessionForDay, key = {it.session.id}) {session ->
                WorkoutHistoryCard(session = session)
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(session: WorkoutSessionWithDetails) {
    // Logic to determine the title and icon based on time of day
    val calendar = Calendar.getInstance().apply { timeInMillis = session.session.date }
    val (title: String, icon: ImageVector) = when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Morning Workout" to Icons.Default.WbSunny
        in 12..16 -> "Afternoon Workout" to Icons.Outlined.Brightness4
        else -> "Evening Workout" to Icons.Outlined.Nightlight
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // Use more subtle colors for a premium feel
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
            // --- 1. Redesigned Title Row with Icon ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 2. Improved Exercise and Set Display ---
            // Using a Column to contain all exercises for consistent spacing
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                session.exercises.forEach { exerciseWithSets ->
                    Column {
                        // Exercise Name stands out
                        Text(
                            text = exerciseWithSets.sessionExercise.exerciseName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Sets are clearly nested under the exercise
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            exerciseWithSets.sets.forEach { set ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // The bullet point for a classic list look
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "${set.weight} kg x ${set.reps} reps",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly muted for better hierarchy
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}