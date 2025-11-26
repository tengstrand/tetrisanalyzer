package nu.tengstrand.tetrisanalyzer.game

case class MinMax(var value: Int, min: Int, max: Int) {
  def set(value: Int): Unit = {
    if (value < min) this.value = min
    else if (value > max) this.value = max
    else this.value = value
  }
  def increase() = { if (value < max) { value += 1; true } else false }
  def decrease() = { if (value > min) { value -= 1; true } else false }
}
case class Size(width: Int, height: Int)

class BoardSize(var width: MinMax, var height: MinMax) {
  def size = Size(width.value, height.value)

  def increaseWidth(): Unit = { width.increase() }
  def decreaseWidth(): Unit = { width.decrease() }
  def increaseHeight(): Unit = { height.increase() }
  def decreaseHeight(): Unit = { height.decrease() }

  def increaseSizeKeepRatio(): Unit = {
    if (width.increase())
      height.set(width.value * 2)
  }

  def decreaseSizeKeepRatio(): Unit = {
    if (width.decrease())
      height.set(width.value * 2)
  }
}