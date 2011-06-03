package nu.tengstrand.tetrisanalyzer.gui

import java.awt.Rectangle

import scala.swing.Component
import scala.swing.LayoutContainer
import scala.swing.Panel

class NullPanel extends Panel with LayoutContainer {
  override lazy val peer = new javax.swing.JPanel(null) with SuperMixin
  type Constraints = Rectangle
  protected def areValid(c: Constraints): (Boolean, String) = (true, "")
  protected def constraintsFor(comp: Component) = comp.bounds
  def add(c: Component, b: Constraints) {
    if(b != null) {
      c.bounds.x = b.x
      c.bounds.y = b.y
      c.bounds.width = b.width
      c.bounds.height = b.height
      c.peer.setBounds(b)
    }
    peer.add(c.peer)
  }
}