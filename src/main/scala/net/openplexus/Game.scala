package net.openplexus

import org.newdawn.slick._


object Game {
  private val game = new Game

  def sprites = game.sprites

  def main(args: Array[String]) = {
    val container = new AppGameContainer(game, 800, 600, false)

    container.setTargetFrameRate(60)
    container.start
  }
}

class Game extends BasicGame("street") {
  var world: World = null
  var sprites: SpriteManager = null

  override def init(gc: GameContainer) {
    sprites = new SpriteManager()
    world = new World
    println("Initialized Game")
  }

  override def update(gc: GameContainer, delta: Int) {
  }

  override def render(gc: GameContainer, g: Graphics) {
    g.setColor(Color.black)
    g.fillRect(0, 0, gc.getWidth, gc.getHeight)
    g.translate(gc.getWidth / 2, gc.getHeight / 2)
    world.draw(g)
  }
}
