package Sudoku

class Grid {

  private var numbers = Array.fill(9, 9) { 0 }

  def update(r: Int, c: Int, n: Int) = numbers(r)(c) = n

  def update(rc: (Int, Int), n: Int) = numbers(rc._1)(rc._2) = n

  def restart() = { numbers = Array.fill(9, 9) { 0 } }

  def cell(r: Int, c: Int) = numbers(r)(c)

  def cell(rc: (Int, Int)) = numbers(rc._1)(rc._2)

  def row(n: Int) = numbers(n)

  def column(n: Int) = {
    var i = 0
    val col = Array.fill(9) {
      0
    }

    while (i < 9) {
      col(i) = numbers(i)(n)
      i += 1
    }

    col
  }

  def box(n: Int) = {
    var r = 0
    var c = 0
    var i = 0
    var j = 0
    var k = 0
    val box = Array.fill(9) {
      0
    }

    n match {
      case 0 =>
      case 1 => { r = 0; c = 3 }
      case 2 => { r = 0; c = 6 }
      case 3 => { r = 3; c = 0 }
      case 4 => { r = 3; c = 3 }
      case 5 => { r = 3; c = 6 }
      case 6 => { r = 6; c = 0 }
      case 7 => { r = 6; c = 3 }
      case 8 => { r = 6; c = 6 }
    }

    while (i < 3) {
      while (j < 3) {
        box(k) = numbers(r)(c)
        j += 1
        c += 1
        k += 1
      }
      j = 0
      c -= 3
      i += 1
      r += 1
    }

    box
  }

  def box_(r: Int, c: Int): Int = { // returns box number
    if (r < 3) {
      if (c < 3)           return 0
      if (c >= 3 && c < 6) return 1
                           return 2
    }
    else if (r < 6) {
      if (c < 3)           return 3
      if (c >= 3 && c < 6) return 4
                           return 5
    }
    else {
      if (c < 3)           return 6
      if (c >= 3 && c < 6) return 7
                           return 8
    }
  }

  def isSafe(r: Int, c: Int, n: Int) = {
    !row(r).contains(n) && !column(c).contains(n) && !box(box_(r, c)).contains(n)
  }

  def isValid: Boolean = {

    def validRow(n: Int) = {
      val filtered = row(n).filter(_ != 0)
      filtered.length == filtered.distinct.length
    }

    def validColumn(n: Int) = {
      val filtered = column(n).filter(_ != 0)
      filtered.length == filtered.distinct.length
    }

    def validBox(n: Int) = {
      val filtered = box(n).filter(_ != 0)
      filtered.length == filtered.distinct.length
    }

    for (i <- 0 until 9) {
      if (!(validRow(i) && validColumn(i) && validBox(i))) return false
    }

    true
  }

  def weakestCell(): ((Int, Int), Int) = { //coords of weakest cell, placeable digit
    var i = 0
    var j = 0
    var best = (0, 0)
    var bestLen = 0
    var placeable = 0

    while (i < 9) {
      while (j < 9) {
        if (numbers(i)(j) == 0) {
          val arr = scala.collection.mutable.ArrayBuffer[Int]()
          arr.addAll(row(i))
          arr.addAll(column(j))
          arr.addAll(box(box_(i, j)))

          val filtered = arr.filter( _ != 0 ).distinct
          val len = filtered.length
          if (len == 8) {
            return ((i, j), Array(1, 2, 3, 4, 5, 6, 7, 8, 9).diff(filtered).head)
          }
          if (len > bestLen) {
            bestLen = len
            best = (i, j)
          }
        }
        j += 1
      }
      i += 1
      j = 0
    }
    (best, 0)
  }

  def isCompleted: Boolean = {
    for (i <- 0 until 9) {
      if (numbers(i).sum != 45) return false
    }
    isValid
  }


  override def toString = {

    var i = 0 //row
    var j = 0 //column

    var s = ""

    while (i < 9) {
      while (j < 9) {
        s += numbers(i)(j)
        j += 1
        if (j == 3 || j == 6) s += "|"
      }
      i += 1
      j = 0
      if (i == 3 || i == 6) s += "\n---------\n"
      else s += "\n"
    }
    s
  }
}