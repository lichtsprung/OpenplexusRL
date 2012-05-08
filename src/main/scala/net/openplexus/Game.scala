package net.openplexus

import org.newdawn.slick._


object Game {
  def main(args: Array[String]) = {
    val container = new AppGameContainer(new Game, 800, 600, false)

    container.setTargetFrameRate(60)
    container.start
  }
}

class Game extends BasicGame("street") {
  var world: World = null

  override def init(gc: GameContainer) {
    world = new World
    println("Initialized Game")
  }

  override def update(gc: GameContainer, delta: Int) {
    val input = gc.getInput
    if (input.isKeyPressed(Input.KEY_UP)){
     world.playerMoveUp()
    }else if (input.isKeyPressed(Input.KEY_DOWN)){
      world.playerMoveDown()
    }else if (input.isKeyPressed(Input.KEY_LEFT)){
      world.playerMoveLeft()
    }else if (input.isKeyPressed(Input.KEY_RIGHT)){
      world.playerMoveRight()
    }
    world.update()
  }

  override def render(gc: GameContainer, g: Graphics) {
    g.setColor(Color.black)
    g.fillRect(0, 0, gc.getWidth, gc.getHeight)
    g.setColor(Color.green)
    world.draw(g)
  }
}
