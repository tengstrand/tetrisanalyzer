package com.github.tetrisanalyzer.gui

import scala.swing._

object TetrisAnalyzer extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "TetrisAnalyzer"
    preferredSize = new Dimension(250,500)

    val label = new Label {
      text = "TetrisAnalyzer - version 1.1"
    }
    val playfield = new Playfield

    contents = new BoxPanel(Orientation.Vertical) {
      contents += playfield
      contents += label
    }
  }
}