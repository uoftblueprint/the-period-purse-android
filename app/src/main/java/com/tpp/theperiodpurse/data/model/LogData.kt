package com.tpp.theperiodpurse.data.model

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.symptomlog.*
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.LogViewModel

typealias ComposablePromptFun = @Composable (logViewModel: LogViewModel, appViewModel: AppViewModel) -> Unit
typealias ComposableIconFun = @Composable (color: Color) -> Unit

enum class LogPrompt(
    var title: Int,
    var icon: ComposableIconFun,
    var prompt: ComposablePromptFun,
) {
    Flow(
        title = R.string.period_flow,
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.opacity_black_24dp),
                contentDescription = "Flow Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> FlowPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Ovulation(
        title = R.string.ovulation,
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.ovulation),
                contentDescription = "Ovulation Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> OvulationPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Mood(
        title = R.string.mood,
        icon = { color ->
            Icon(
                imageVector = Icons.Outlined.AddReaction,
                contentDescription = "Mood Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> MoodPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Sleep(
        title = R.string.sleep,
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.nightlight_black_24dp),
                contentDescription = "Sleep Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> SleepPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Cramps(
        title = R.string.cramps,
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.sentiment_very_dissatisfied_black_24dp),
                contentDescription = "Cramp Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> CrampsPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Exercise(
        title = R.string.exercise,
        icon = { color ->
            Icon(
                painter = painterResource(id = R.drawable.self_improvement_black_24dp),
                contentDescription = "Exercise Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> ExercisePrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    Notes(
        title = R.string.notes,
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.edit_black_24dp),
                contentDescription = "Notes Icon",
                tint = color,
            )
        },
        prompt = { logViewModel, appViewModel -> NotesPrompt(logViewModel = logViewModel, appViewModel = appViewModel) },
    ),
    ;
    companion object {
        private val map = values().associateBy { it.title }
        infix fun from(title: Int) = map[title]
    }
}

open class LogSquare(
    var description: String,
    var icon: ComposableIconFun,
    var promptTitle: Int,
    var dataName: String,
) {
    object FlowLight : LogSquare(
        description = "Light",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.water_drop_black_24dp),
                contentDescription = "FlowLight",
                tint = color
            )
        },
        promptTitle = R.string.period_flow,
        dataName = FlowSeverity.Light.name,
    )
    object FlowMedium : LogSquare(
        description = "Medium",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.opacity_black_24dp),
                contentDescription = "FlowMedium",
                tint = color
            )
        },
        promptTitle = R.string.period_flow,
        dataName = FlowSeverity.Medium.name,
    )
    object FlowHeavy : LogSquare(
        description = "Heavy",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.flow_heavy),
                contentDescription = "FlowHeavy",
                tint = color
            )
        },
        promptTitle = R.string.period_flow,
        dataName = FlowSeverity.Heavy.name,
    )
    object FlowSpotting : LogSquare(
        description = "Spotting",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.spotting),
                contentDescription = "FlowSpotting",
                tint = color
            )
        },
        promptTitle = R.string.period_flow,
        dataName = FlowSeverity.Spotting.name,
    )
    object FlowNone : LogSquare(
        description = "None",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.not_interested_black_24dp),
                contentDescription = "FlowNone",
                tint = color
            )
        },
        promptTitle = R.string.period_flow,
        dataName = FlowSeverity.None.name,
    )
    object Ovulating : LogSquare(
        description = "Ovulating",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.ovulation),
                contentDescription = "Ovulating",
                tint = color
            )
        },
        promptTitle = R.string.ovulating,
        dataName = com.tpp.theperiodpurse.data.model.Ovulation.Ovulating.displayName,
    )
    object NotOvulating : LogSquare(
        description = "Not Ovulating",
        icon = { color ->
            Icon(
                painter = painterResource(R.drawable.not_interested_black_24dp),
                contentDescription = "Not Ovulating",
                tint = color
            )
        },
        promptTitle = R.string.not_ovulating,
        dataName = com.tpp.theperiodpurse.data.model.Ovulation.Ovulating.displayName,
    )
    object MoodHappy : LogSquare(
        description = "Happy",
        icon = {color ->
            Icon(
                painter = painterResource(R.drawable.sentiment_satisfied_black_24dp),
                contentDescription = "MoodHappy",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.HAPPY.displayName,
    )

    object MoodNeutral : LogSquare(
        description = "Meh",
        icon = {color ->
            Icon(
                painter = painterResource(R.drawable.sentiment_neutral_black_24dp),
                contentDescription = "MoodNeutral",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.NEUTRAL.displayName,
    )

    object MoodSad : LogSquare(
        description = "Sad",
        icon = {color ->
            Icon(
                painter = painterResource(R.drawable.sentiment_dissatisfied_black_24dp),
                contentDescription = "MoodSad",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.SAD.displayName,
    )

    object MoodSilly : LogSquare(
        description = "Silly/Goofy",
        icon = {color ->
            Icon(
                painter = painterResource(R.drawable.sentiment_very_satisfied_black_24dp),
                contentDescription = "MoodSilly",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.LOL.displayName,
    )

    object MoodSick : LogSquare(
        description = "Sick",
        icon = {color ->
            Icon(
                painter = painterResource(R.drawable.sick_black_24dp),
                contentDescription = "MoodSick",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.SICK.displayName,
    )

    object MoodAngry : LogSquare(
        description = "Angry",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.MoodBad,
                contentDescription = "MoodAngry",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.ANGRY.displayName,
    )

    object MoodLoved : LogSquare(
        description = "Loved",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "MoodLoved",
                tint = color
            )
        },
        promptTitle = R.string.mood,
        dataName = Mood.LOVED.displayName,
    )

    object CrampsNeutral : LogSquare(
        description = "Neutral",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SentimentNeutral,
                contentDescription = "CrampsNeutral",
                tint = color
            )
        },
        promptTitle = R.string.cramps,
        dataName = CrampSeverity.Neutral.name,
    )

    object CrampsBad : LogSquare(
        description = "Bad",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SentimentDissatisfied,
                contentDescription = "CrampsBad",
                tint = color
            )
        },
        promptTitle = R.string.cramps,
        dataName = CrampSeverity.Bad.name,
    )

    object CrampsTerrible : LogSquare(
        description = "Terrible",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SentimentVeryDissatisfied,
                contentDescription = "CrampsTerrible",
                tint = color
            )
        },
        promptTitle = R.string.cramps,
        dataName = CrampSeverity.Terrible.name,
    )

    object CrampsNone : LogSquare(
        description = "None",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SentimentVerySatisfied,
                contentDescription = "CrampsNone",
                tint = color
            )
        },
        promptTitle = R.string.cramps,
        dataName = CrampSeverity.None.name,
    )

    object ExerciseCardio : LogSquare(
        description = "Cardio",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.DirectionsRun,
                contentDescription = "ExerciseCardio",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.CARDIO.displayName,
    )

    object ExerciseYoga : LogSquare(
        description = "Yoga",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SelfImprovement,
                contentDescription = "ExerciseYoga",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.YOGA.displayName,
    )

    object ExerciseStrength : LogSquare(
        description = "Strength",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.FitnessCenter,
                contentDescription = "ExerciseStrength",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.STRENGTH.displayName,
    )

    object ExerciseBallSports : LogSquare(
        description = "Ball Sports",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SportsSoccer,
                contentDescription = "ExerciseBallSports",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.BALL_SPORTS.displayName,
    )

    object ExerciseMartialArts : LogSquare(
        description = "Martial Arts",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SportsMartialArts,
                contentDescription = "ExerciseMartialArts",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.MARTIAL_ARTS.displayName,
    )

    object ExerciseWaterSports : LogSquare(
        description = "Water Sports",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.Pool,
                contentDescription = "Water Sports",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.WATER_SPORTS.displayName,
    )

    object ExerciseCycling : LogSquare(
        description = "Cycling",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.DirectionsBike,
                contentDescription = "ExerciseCycling",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.CYCLING.displayName,
    )

    object ExerciseRacquetSports : LogSquare(
        description = "Racquet Sports",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.SportsTennis,
                contentDescription = "ExerciseRacquetSports",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.RACKET_SPORTS.displayName,
    )

    object ExerciseWinterSports : LogSquare(
        description = "Winter Sports",
        icon = {color ->
            Icon(
                imageVector = Icons.Rounded.DownhillSkiing,
                contentDescription = "ExerciseWinterSports",
                tint = color
            )
        },
        promptTitle = R.string.exercise,
        dataName = Exercise.WINTER_SPORTS.displayName,
    )
}
