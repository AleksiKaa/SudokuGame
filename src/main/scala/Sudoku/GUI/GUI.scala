package Sudoku.GUI

import Sudoku.{Grid, Utils}

import java.awt.Color
import scala.swing._

class GUI extends MainFrame {

  val game = new Grid
  Utils.randomize(game)

  title = "Sudoku!"

  val gridView = new GridPanel(9, 9) {
    background = Color.red
    preferredSize = new Dimension(450, 450)
    for (i <- 0 until 9) {
      for (j <- 0 until 9) {
        contents += new sudokuButton(game, i, j)
      }
    }
  }

  val textBox = new Label() {
    text = ""
    preferredSize = new Dimension(150, 50)
    visible = true
  }

  val toolbar = new GridPanel(1, 4) {
    preferredSize = new Dimension(150, 40)

    val checkButton = new gridButtons {
      text = "Check"
      action = new Action("Check") {
        override def apply(): Unit = {
          if (game.isCompleted) textBox.text = "Congratulations! You solved this puzzle!"
          else if (game.isValid) textBox.text = "Grid is valid"
          else textBox.text = "Grid is not valid"
        }
      }
    }

    val randomButton = new gridButtons {
      text = "Randomize"
      action = new Action("Randomize") {
        override def apply(): Unit = {
          Utils.randomize(game)
          for (i <- 0 until 81) {
            val button = gridView.contents(i).asInstanceOf[sudokuButton]
            button.reInit(game.cell(button.coords))
          }
        }
      }
    }

    val solveButton = new gridButtons {
      action = new Action("Solve") {
        override def apply(): Unit = {
          Utils.solve(game)
          gridView.contents.foreach(b => {
            val sb = b.asInstanceOf[sudokuButton]
            sb.update(game.cell(sb.coords))
          })
        }
      }
    }

    val importButton = new gridButtons {
      text = "Import"
      action = new Action("Import") {
        override def apply(): Unit = {
          val popup = Dialog
          val input = popup.showInput(null, "Please enter a sudoku in String form:", initial = "")
          input match {
            case Some(sudoku) => {
              if (sudoku.length >= 81) {
                Utils.importGrid(game, sudoku)
                gridView.contents.foreach(b => {
                  val sButton = b.asInstanceOf[sudokuButton]
                  sButton.update(game.cell(sButton.coords))
                })
              }
              else popup.showInput(null, "Entered string was not long enough", initial = "")
            }
            case None =>
          }
        }
      }
    }

    contents += checkButton
    contents += randomButton
    contents += solveButton
    contents += importButton
  }

  contents = new BorderPanel {
    add(gridView, BorderPanel.Position.North)
    add(toolbar, BorderPanel.Position.Center)
    add(textBox, BorderPanel.Position.South)
  }
}


object SudokuGame {
  def main(args: Array[String]) {
    val ui = new GUI
    ui.visible = true
  }
}

//assdasd