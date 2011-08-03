package nu.tengstrand.tetrisanalyzer.game

case class MinMax(var value: Int, min: Int, max: Int) {
  def set(value: Int) {
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

  def increaseHeight() { height.increase() }
  def decreaseHeight() { height.decrease() }

  def increaseSizeKeepRatio() {
    if (width.increase())
      height.set(width.value * 2)
  }

  def decreaseSizeKeepRatio() {
    if (width.decrease())
      height.set(width.value * 2)
  }
}