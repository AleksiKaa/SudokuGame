package Sudoku

import scala.util.Random

object Utils {

  def importGrid(g: Grid, sudoku: String): Unit = {

    val alphabet = "0123456789"
    val s = sudoku.filter(alphabet.contains(_))

    require(s.length == 81, s"Length was ${s.length}, not 81")

    var i = 0 //row
    var j = 0 //column
    var k = 0 //runner

    while (i < 9) {
      while (j < 9) {
        g.update(i, j, s(k).toInt - 48)
        j += 1
        k += 1
      }
      j = 0
      i += 1
    }
  }

  def solve(g: Grid): Unit = {
    def inner(): Unit = {
      val next = g.weakestCell()
      if (next._2 != 0) {
        g.update(next._1, next._2)
        inner()
      }
    }

    if (g.isValid) inner()
  }

  //working and is pretty fast
  def backtracker(g: Grid, r: Int, c: Int): Boolean = {

    var row = r
    var col = c

    if (g.isCompleted) {
      return true
    }

    if (col > 8) {
      row += 1
      col = 0
    }

    if (g.isFilled || row > 8) return false

    if (g.cell(row, col) != 0) {
      if (col == 8) backtracker(g, row + 1, 0)
      else backtracker(g, row, col + 1)
    }

    for (n <- 1 until 10) {
      if (g.isSafe(row, col, n)) {
        g.update(row, col, n)
        if (backtracker(g, row, col + 1)) return true
        g.update(row, col, 0)
        //println(s"backtracking (${row}, ${col}), ${n}")
      }
    }
    false
  }

  //works very well
  def backtrackerIterative(g: Grid): Boolean = {

    for (row <- 0 until 9) {
      for (col <- 0 until 9) {
        if (g.cell(row, col) == 0) {
          for (n <- 1 until 10) {
            if (g.isSafe(row, col, n)) {
              g.update(row, col, n)
              if (g.isCompleted) return true
              else {
                if (backtrackerIterative(g)) return true
              }
              g.update(row, col, 0)
              //println(s"backtracking (${row}, ${col}), ${n}")
            }
          }
          return false
        }
      }
    }
    false
  }

  def randomize(g: Grid): Unit = {

    //Set all cells to 0
    g.restart()

    //Create a list of all cells and shuffle them
    val random = new Random(System.nanoTime())
    val arr = scala.collection.mutable.ArrayBuffer[(Int, Int)]()
    for (i <- 0 until 9) {
      for (j <- 0 until 9) {
        arr += ((i, j))
      }
    }

    val shuffled = random.shuffle(arr).iterator

    while (shuffled.hasNext) {
      val next = shuffled.next()
      //Add a random number from 1 to 9 to a free cell
      g.update(next, 1 + random.nextInt(9))
      //If the updated grid does not have a solution, repeat the step
      while (!backtrackerIterative(g)) {
        g.update(next, 1 + random.nextInt(9))
        //Stop when all cells are assigned a number and the grid is valid
        if (g.isCompleted) return //prob need another keyword to break the loop
      }
    }
    /*
    val shuffledAgain = random.shuffle(arr).iterator

    while (shuffledAgain.hasNext) {
      val next = shuffledAgain.next()
      g.update(next, 0)
    }
    */
  }
}