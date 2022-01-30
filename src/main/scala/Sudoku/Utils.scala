package Sudoku

import scala.util.Random
import scala.collection.parallel.CollectionConverters._

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

  def backtracker(g: Grid, r: Int, c: Int): Boolean = {

    var row = r
    var col = c

    if (g.isCompleted && g.isValid) {
      println(g.toString)
      return true
    }

    if (col > 8)  {
      row += 1
      col = 0
    }

    if (g.cell(row, col) != 0) {
      if (col == 8) backtracker(g, row + 1, 0)
      else backtracker(g, row, col + 1)
    }

    for (n <- 1 until 10) {
      if (g.isSafe(row, col, n)) {
        g.update(row, col, n)
        if (backtracker(g, row, col + 1)) return true
      }
      g.update(row, col, 0)
      println(s"backtracking (${row}, ${col}), ${n}")
    }
    false
  }

  def randomize(g: Grid): Unit = {

    g.restart()

    var k = 0
    val random = new Random(System.nanoTime())
    val arr = scala.collection.mutable.ArrayBuffer[(Int, Int)]()
    for (i <- 0 until 9) {
      for (j <- 0 until 9) {
        arr += ((i, j))
        k += 1
      }
    }
    val shuffled = random.shuffle(arr).iterator
    while (shuffled.hasNext) {
      g.update(shuffled.next(), 1 + random.nextInt(9))
    }
  }
}

/*v.par.foreach(n => {
      if (g.isSafe(row, col, n)) {
        g.update(row, col, n)
        if (backtracker(g, row, col + 1)) return true
      }
      g.update(row, col, 0)
      println("backtracking")
    })

    for (n <- 1 until 10) {
      if (g.isSafe(row, col, n)) {
        g.update(row, col, n)
        if (backtracker(g, row, col + 1)) return true
      }
      g.update(row, col, 0)
      println("backtracking")
    }*/