package Sudoku.GUI

import Sudoku.Grid

import java.awt.{Color, Font}
import javax.swing.BorderFactory
import scala.swing.event.Key._
import scala.swing.event.KeyPressed
import scala.swing.{Action, Button}

class gridButtons extends Button {
  background = Color.gray
  foreground = Color.black
  border = BorderFactory.createLineBorder(Color.black, 1)
}

class sudokuButton(g: Grid, val r: Int, val c: Int) extends Button {

  val t = this
  val bold = new Font("Arial", 1, 25)
  val noBold = new Font("Arial", 0, 25)
  private var initValue = g.cell(r, c)

  private def isUpdateable = initValue == 0

  def text_(s: String): Unit = if (s == "0") text = "" else text = s

  def coords = (r, c)

  def update(n: Int) = {
    if (this.isUpdateable) {
      g.update(r, c, n)
      this.text_(s"${n}")
    }
  }

  def reInit(n: Int) = {
      initValue = g.cell(r, c)
      t.font = if (isUpdateable) noBold else bold
      t.foreground = if (isUpdateable) Color.blue else Color.black
      this.text_(s"${n}")
  }

  text_(s"${g.cell(r, c)}")
  font = if (isUpdateable) noBold else bold
  background = Color.white
  foreground = if (isUpdateable) Color.blue else Color.black
  border = if (Array(0, 2, 4, 6, 8).contains(g.box_(r, c))) BorderFactory.createLineBorder(Color.black, 1)
  else BorderFactory.createLineBorder(Color.black, 3)
  action = new Action(t.text) {
    override def apply(): Unit = {
      listenTo(keys)
      reactions += {
        case KeyPressed(_, Key0, _, _) => t.update(0)
        case KeyPressed(_, Key1, _, _) => t.update(1)
        case KeyPressed(_, Key2, _, _) => t.update(2)
        case KeyPressed(_, Key3, _, _) => t.update(3)
        case KeyPressed(_, Key4, _, _) => t.update(4)
        case KeyPressed(_, Key5, _, _) => t.update(5)
        case KeyPressed(_, Key6, _, _) => t.update(6)
        case KeyPressed(_, Key7, _, _) => t.update(7)
        case KeyPressed(_, Key8, _, _) => t.update(8)
        case KeyPressed(_, Key9, _, _) => t.update(9)
      }
    }
  }
}