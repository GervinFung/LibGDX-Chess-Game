A simple 2 player chess AI that implemented minimax & alpha-beta pruning & move-ordering & pawn structure analysis to fully optimise the AI with a proper FEN file format

## Folder Structure

The workspace contains two folders by default, where:

- `core`: the folder to maintain sources, stores the code for chess engine logic and UI design
- `assset`: the folder that store images for the UI

## To play the Game

NOTE: **if AI is thinking, the UI will freeze**

No. | Steps
--- | ----
1.| User can choose to play as white/black
2.| User can choose white/black as AI
3.| Or, user can watch AI play against itself
4.| Once the game started, left click the piece you wish to move, and click the square you wish to place it
5.| User can reset timer if they wish to start a new game

## Game Features

NOTE: **if AI move is undo, reconfigure the Setup AI again**

No. | Features
--- | ----
1.| Restart new game
2.| Save game/ Load saved game
3.| Exit game
4.| Highlight legal moves
5.| Show move history
6.| Choose AI level from 1 to 10
7.| Show captured piece
8.| Show AI thinking progress bar
9.| Undo previous move
10.| Multithread feature depends on the number of cores of a pc, the greater the better/faster AI can think
11.| Flip board
12.| Choose 6 different board colors

![Chess sample](chess.png)