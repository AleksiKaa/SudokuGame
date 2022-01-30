import Sudoku._

object test extends App {

  val alphabet = "0123456789"

  val a = new Grid
  val b = new Grid
  val c = new Grid

  Utils.importGrid(a, """534678912
                         672195348
                         198342567
                         859761423
                         426853791
                         713924856
                         961537284
                         287419635
                         345286179""".filter( alphabet.contains(_) ))

  //530070000600195000098000060800060003400803001700020006060000280000419005000080079

  Utils.importGrid(b, """530070000
                         600195000
                         098000060
                         800060003
                         400803001
                         700020006
                         060000280
                         000419005
                         000080079""".filter( alphabet.contains(_) ))

  Utils.importGrid(c, """530070000
                         600195000
                         098000060
                         800060003
                         400803001
                         700020006
                         060000280
                         000419005
                         000080079""".filter( alphabet.contains(_) ))


  //Utils.backtrackerIterative(b)
  Utils.backtracker(b, 0, 0)
  println(b.toString)

}

