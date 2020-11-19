# Slider-Puzzle
Solves the slider puzzle for n*n board as few moves as possible

### How to use:
- Attach the following file for helper functions: https://algs4.cs.princeton.edu/code/algs4.jar <br>
(For details of above .jar file, check https://algs4.cs.princeton.edu/code/)
- Board class creates an nXn board with the given position of numbers on various tiles. Create a board by calling the contructor Board(int[][] tiles), with the 
array tiles containing numbers at each position
- Solver takes in a board object and returns the minimum number of steps to solve it, along with an iterable of the steps (using A* algorithm)
