# Chess Engine

![](https://github.com/ACSmyth/chess-engine/blob/media/initial_board.PNG)

## Features
- Alpha-beta pruning
- Quiescence search
- Check extensions
- Zobrist hashing
- Custom opening book (generated from master games and lichess.org database)
- History heuristic table
- Transposition table

## Commands
- <b>drag piece:</b> make move
- <b>space bar:</b> make computer move
- <b>e:</b> toggle edit mode
- <b>s:</b> flip board
- <b>t:</b> toggle check extensions
- <b>left arrow key:</b> undo move
- <b>right arrow key:</b> redo move
- <b>hold right click:</b> draw arrows
- <b>left click anywhere:</b> remove arrows

## How to Run

- If you have Java 8 installed:

  `java -jar out/artifacts/ChessEngine_jar/ChessEngine.jar`

- If you have Homebrew but not Java 8:

  ```
  brew install openjdk@8
  /usr/local/opt/openjdk@8/bin/java -jar out/artifacts/ChessEngine_jar/ChessEngine.jar
  ```
