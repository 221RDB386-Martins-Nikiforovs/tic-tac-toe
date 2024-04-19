//
//
// Autors:   Mārtiņš Nikiforovs 221RDB386
//
// GitHub: https://github.com/221RDB386-Martins-Nikiforovs/tic-tac-toe
//
// !!! Atsauces pašās projekta beigās !!!
//
//
//
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toe.AboutPage
import kotlinx.coroutines.delay


@Composable
fun TicTacToe(sharedPreferences: SharedPreferences, context: Context) {

    val savedName = remember { sharedPreferences.getString("userName", "") }
    Toast.makeText(context, "Sveiki,  " + savedName , Toast.LENGTH_LONG).show()

    var popup by remember { mutableStateOf(false) }
    val board = remember {
        mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
    }

    val CurrPlayer = remember {
        mutableStateOf("X")
    }

    val winner = remember {
        mutableStateOf<String?>(null)
    }

    val initialBoard = Array(3) {
        arrayOfNulls<String>(3)
    }

    val startPlayer = "X"

    var isComputerMode by remember { mutableStateOf(false) }




    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Tic-Tac-Toe :)",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Player vs Player",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 1.2
            Switch(
                checked = isComputerMode,
                onCheckedChange = { isChecked -> isComputerMode = isChecked },
                colors = SwitchDefaults.colors(checkedBorderColor = Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Player vs Computer",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }



        Box(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .aspectRatio(1f),
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                                onClick = {
                                    if (board.value[row][col] == null && winner.value == null) {
                                        board.value[row][col] = CurrPlayer.value
                                        CurrPlayer.value = if (CurrPlayer.value == "X") "O" else "X"
                                        winner.value = checkForWinner(board.value)
                                        if (winner.value == null && isComputerMode && CurrPlayer.value == "O") {
                                            // Computer's turn
                                            val move = getComputerMove(board.value)
                                            board.value[move.first][move.second] = CurrPlayer.value
                                            winner.value = checkForWinner(board.value)
                                            CurrPlayer.value = if (CurrPlayer.value == "X") "O" else "X"
                                        }

                                    }
                                }
                            ) {
                                Text(
                                    text = board.value[row][col] ?: "",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.Black
                                )

                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Current Player: ${CurrPlayer.value}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }




                if (winner.value != null) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                    Text(
                        text = "Winner: ${winner.value}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Green,
                        modifier = Modifier.padding(top = 4.dp)
                    )}
                    LaunchedEffect(true) {
                        delay(3000)
                        board.value = initialBoard
                        CurrPlayer.value = startPlayer
                        winner.value = null
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            board.value = initialBoard
                            CurrPlayer.value = startPlayer
                            winner.value = null

                        }
                    ) {
                        Text(
                            text = "Reset",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                        )
                    }
                    Button(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        onClick = {
                            popup = true
                        }
                    ) {
                        Text(
                            text = "Par autoru",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                        )
                    } ;if (popup) {
                    AuthorPupUp(
                        onDismissRequest = { popup = false },
                        dialogTitle = "Mārtiņš Nikiforovs",
                        dialogText = "8.grupa 221RDB386",
                        icon = Icons.Default.Info
                    )
                }

                }






            }
        }
    }


}



@Composable
fun ShowAbout(){
    AboutPage()
}


fun getComputerMove(board: Array<Array<String?>>): Pair<Int, Int> {
    val bestMove = findBestMove(board)
    return Pair(bestMove.row, bestMove.col)
}


fun findBestMove(board: Array<Array<String?>>): Move {
    var bestVal = Int.MIN_VALUE
    var bestMove = Move(-1, -1)
    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j] == null) {
                board[i][j] = "O"
                val moveVal = minimax(board, 0, false)
                board[i][j] = null
                if (moveVal > bestVal) {
                    bestMove.row = i
                    bestMove.col = j
                    bestVal = moveVal
                }
            }
        }
    }
    return bestMove
}


data class Move(var row: Int, var col: Int)

// 1.1

fun minimax(board: Array<Array<String?>>, depth: Int, isMaximizing: Boolean): Int {
    val score = score(board)
    if (score == 10 || score == -10) {
        return score
    }
    if (!isMovesLeft(board)) {
        return 0
    }
    return if (isMaximizing) {
        var best = Int.MIN_VALUE
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == null) {
                    board[i][j] = "O"
                    best = maxOf(best, minimax(board, depth + 1, !isMaximizing))
                    board[i][j] = null
                }
            }
        }
        best
    } else {
        var best = Int.MAX_VALUE
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == null) {
                    board[i][j] = "X"
                    best = minOf(best, minimax(board, depth + 1, !isMaximizing))
                    board[i][j] = null
                }
            }
        }
        best
    }
}

fun score(board: Array<Array<String?>>): Int {
    // Check rows
    for (row in 0..2) {
        if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            if (board[row][0] == "O") return 10
            else if (board[row][0] == "X") return -10
        }
    }
    // Check columns
    for (col in 0..2) {
        if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            if (board[0][col] == "O") return 10
            else if (board[0][col] == "X") return -10
        }
    }
    // Check diagonals
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        if (board[0][0] == "O") return 10
        else if (board[0][0] == "X") return -10
    }
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        if (board[0][2] == "O") return 10
        else if (board[0][2] == "X") return -10
    }
    return 0
}

fun isMovesLeft(board: Array<Array<String?>>): Boolean {
    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col] == null) {
                return true
            }
        }
    }
    return false
}


fun checkForWinner(board: Array<Array<String?>>): String? {

    for (row in 0..2) {
        if (board[row][0] != null && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            return board[row][0]
        }
    }
    for (col in 0..2) {
        if (board[0][col] != null && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            return board[0][col]
        }
    }
    if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        return board[0][0]
    }
    if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        return board[0][2]
    }

    if (board[0][0] != null && board[1][0] != null
        && board[2][0] != null && board[0][1] != null
        && board[0][2] != null && board[1][1] != null
        && board[1][2] != null && board[2][1] != null
        && board[2][2] != null ){

        return "Tie"
    }

    return null
}

@Composable
fun AuthorPupUp(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "randm txt")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Aizvērt")
            }
        },
        dismissButton = null
    )
}

// ATSAUCES:

//1.1
// MiniMax algoritms (taisīts  mākslīgāintelekta priekšmeta vajadzībai un atkārtoti izmantots šeit): https://github.com/catisioo/Komanda_34_MIP/blob/main/othermain.py
// ChatGpt vaicājumi:
// 1.1 "translate this minimax python code to Kotlin, for android app with API 28, its for tic-tac -tie game: "def minimax(number, depth, player):
//  if depth == 0 or number <= 10:
//    return 1  # Default divisor and score if depth is 0 or number is small
//
//  best_divisor = None
//  best_score = float('-inf') if player == 1 else float('inf')
//
//  for divisor in [2, 3, 4]:
//    if number % divisor == 0:
//        new_number = number // divisor
//        eval_score = minimax(new_number, depth - 1, 2 if player == 1 else 1)
//
//        if player == 1:
//            if eval_score > best_score:
//                best_score = eval_score
//                best_divisor = divisor
//        else:
//            if eval_score < best_score:
//                best_score = eval_score
//                best_divisor = divisor
//
//  return best_divisor if best_divisor is not None else 1", here is the existing Kotlin game code: "package com.example.tic_tac_toe
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//
//@Composable
//fun TicTacToe() {
//    val board = remember {
//        mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
//    }
//
//    val currentPlayer = remember {
//        mutableStateOf("X")
//    }
//
//    val winner = remember {
//        mutableStateOf<String?>(null)
//    }
//
//    val initialBoard = Array(3){
//        arrayOfNulls<String>(3)
//    }
//
//    val initialPlayer = "X"
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(MaterialTheme.colorScheme.error)
//        .padding(16.dp)
//    )
//    {
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
//        {
//            Text(text = "Tic-Tac-Toe   :)",  style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface)
//        }
//        Box(modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth()
//        )
//        {
//            Column {
//                for (row in 0 .. 2){
//                    Row {
//                        for (col in 0 .. 2){
//                            Button(modifier = Modifier
//                                .weight(1f)
//                                .padding(4.dp), onClick = {
//                                if (board.value [row] [col] == null && winner.value == null){
//                                    board.value[row][col] = currentPlayer.value
//                                    currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
//                                    winner.value = checkForWinner(board.value)
//                                }
//                            }) {
//                                Text(text = board.value[row] [col] ?: "", style =  MaterialTheme.typography.headlineSmall, color = Color.White)
//                            }
//                        }
//                    }
//                }
//                Text(
//                    text = "Current Player: ${currentPlayer.value}",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.padding(top =  16.dp)
//                )
//                if (winner.value != null){
//                    Text(
//                        text = "Winner: ${winner.value}",
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = MaterialTheme.colorScheme.onSurface,
//                        modifier = Modifier.padding(top =  4.dp)
//                    )
//                    LaunchedEffect(true){
//                        delay(2000)
//                        board.value = initialBoard
//                        currentPlayer.value=initialPlayer
//                        winner.value=null
//                    }
//                }
//
//                Button(onClick = {board.value = initialBoard
//                currentPlayer.value=initialPlayer
//                    winner.value=null}){
//                    Text(
//                        text = "Reset",
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = Color.White,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}fun checkForWinner(board:Array<Array<String?>>): String? {
//    for (row in 0 .. 2){
//    if (board[row] [0] !=null  && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
//        return board[row][0]
//        }
//    }
//    for (col in 0 .. 2){
//        if (board[0] [col] !=null  && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
//            return board[0][col]
//        }
//    }
//    if (board[0][0] !=null && board[0][0] == board[1][1] && board[1][1] == board[2][2]){
//        return board[0][0]
//    }
//    if (board[0][2] !=null && board[0][2] == board[1][1] && board[1][1] == board[2][0]){
//        return board[0][2]
//    }
//    return null
//}
//
//""

//1.1.1 "modify the following Kotlin code for android API 28, its a tic tac toe game, where the minimax algorithm needs to be used for human vs computer gamemode: "package com.example.tic_tac_toe
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//
//@Composable
//fun TicTacToe() {
//    val board = remember {
//        mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
//    }
//
//    val currentPlayer = remember {
//        mutableStateOf("X")
//    }
//
//    val winner = remember {
//        mutableStateOf<String?>(null)
//    }
//
//    val initialBoard = Array(3){
//        arrayOfNulls<String>(3)
//    }
//
//    val initialPlayer = "X"
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(MaterialTheme.colorScheme.error)
//        .padding(16.dp)
//    )
//    {
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
//        {
//            Text(text = "Tic-Tac-Toe   :)",  style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface)
//        }
//        Box(modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth()
//        )
//        {
//            Column {
//                for (row in 0 .. 2){
//                    Row {
//                        for (col in 0 .. 2){
//                            Button(modifier = Modifier
//                                .weight(1f)
//                                .padding(4.dp), onClick = {
//                                if (board.value [row] [col] == null && winner.value == null){
//                                    board.value[row][col] = currentPlayer.value
//                                    currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
//                                    winner.value = checkForWinner(board.value)
//                                }
//                            }) {
//                                Text(text = board.value[row] [col] ?: "", style =  MaterialTheme.typography.headlineSmall, color = Color.White)
//                            }
//                        }
//                    }
//                }
//                Text(
//                    text = "Current Player: ${currentPlayer.value}",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.padding(top =  16.dp)
//                )
//                if (winner.value != null){
//                    Text(
//                        text = "Winner: ${winner.value}",
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = MaterialTheme.colorScheme.onSurface,
//                        modifier = Modifier.padding(top =  4.dp)
//                    )
//                    LaunchedEffect(true){
//                        delay(2000)
//                        board.value = initialBoard
//                        currentPlayer.value=initialPlayer
//                        winner.value=null
//                    }
//                }
//
//                Button(onClick = {board.value = initialBoard
//                currentPlayer.value=initialPlayer
//                    winner.value=null}){
//                    Text(
//                        text = "Reset",
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = Color.White,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}fun checkForWinner(board:Array<Array<String?>>): String? {
//    for (row in 0 .. 2){
//    if (board[row] [0] !=null  && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
//        return board[row][0]
//        }
//    }
//    for (col in 0 .. 2){
//        if (board[0] [col] !=null  && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
//            return board[0][col]
//        }
//    }
//    if (board[0][0] !=null && board[0][0] == board[1][1] && board[1][1] == board[2][2]){
//        return board[0][0]
//    }
//    if (board[0][2] !=null && board[0][2] == board[1][1] && board[1][1] == board[2][0]){
//        return board[0][2]
//    }
//    return null
//}
//fun minimax(number: Int, depth: Int, player: Int): Int {
//    if (depth == 0 || number <= 10) {
//        return 1 // Default divisor and score if depth is 0 or number is small
//    }
//
//    var bestDivisor: Int? = null
//    var bestScore = if (player == 1) Int.MIN_VALUE else Int.MAX_VALUE
//
//    for (divisor in listOf(2, 3, 4)) {
//        if (number % divisor == 0) {
//            val newNumber = number / divisor
//            val evalScore = minimax(newNumber, depth - 1, if (player == 1) 2 else 1)
//
//            if (player == 1) {
//                if (evalScore > bestScore) {
//                    bestScore = evalScore
//                    bestDivisor = divisor
//                }
//            } else {
//                if (evalScore < bestScore) {
//                    bestScore = evalScore
//                    bestDivisor = divisor
//                }
//            }
//        }
//    }
//
//    return bestDivisor ?: 1
//}"
// 1.1 modify your kotlin code for android API 28, so that there would be two buttons one to choose player vs player, and one for player vs computer (uses minimax algorithm)



// 1.1  wher minimax

// 1.1  rewrite entire code plis

// 1.1 turn this into minimax plis daddy "fun getComputerMove(board: Array<Array<String?>>): Pair<Int, Int> {
//    // Call minimax to determine the best move for the computer
//    // For simplicity, let's say the computer makes the first available move
//    for (row in board.indices) {
//        for (col in board[row].indices) {
//            if (board[row][col] == null) {
//                return Pair(row, col)
//            }
//        }
//    }
//    // This should never happen if the game logic is correct
//    return Pair(0, 0)
//}"


// 1.1  make button switch "        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            Button(
//                onClick = { isComputerMode = false },
//                modifier = Modifier.padding(8.dp)
//            ) {
//                Text(text = "Player vs Player")
//            }
//            Button(
//                onClick = { isComputerMode = true },
//                modifier = Modifier.padding(8.dp)
//            ) {
//                Text(text = "Player vs Computer")
//            }
//        }"




//1.2 how do i update the ui on switch getting clicked
