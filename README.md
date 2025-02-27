# Minesweeper

=========================
Implementation :=
=========================


I have three classes. My GameBoard class is my main class, as the implmentation of recursion occurs where the tiles
reveal themselves, and set themselves. That is also where the board is initialized in a 2D array.I further used this
class to check if a mine is there in a recursive style. Lastly, I used this class to implement the File I/O portion.
This included saving and then loading the game state.

In my Run Mine Sweeper class, I somewhat reworked the style of the run tic tac toe, ensuring everything on the board
appeared as it should when running. Obviously, I also incorporated my instructions and numMines pop up questions for
the user to interact with. I fed my File I/O buttons through here as well.


Lastly, I created a class for the Tiles to ensure I could change the state of the tiles with different functions.
I used revealed functions through MouseListeners and CLicks to change the state of each individual tile. I also
booleanized all the states to ensure they could be changed and check whether something is a mine, unrevealed, or
revealed.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  TicTacToe from here.
  YouTube videos on File I/O incorporation with JavaSwing
  JavaSwing documentation
