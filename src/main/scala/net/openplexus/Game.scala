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

class Game extends BasicGame("OpenplexusRL") {
  var world: World = null
  var sprites: SpriteManager = null
  var start = false

  override def init(gc: GameContainer) {
    sprites = new SpriteManager()
    world = new World(gc.getWidth / sprites.getSpriteWidth(), gc.getHeight / (sprites.getSpriteHeight() / 2))
    println("Initialized Game")
  }

  override def update(gc: GameContainer, delta: Int) {
    val input = gc.getInput
    if (input.isKeyPressed(Input.KEY_NUMPAD4)) {
      world.moveEntity(world.player, Direction.West)
    } else if (input.isKeyPressed(Input.KEY_NUMPAD6)) {
      world.moveEntity(world.player, Direction.East)
    } else if (input.isKeyPressed(Input.KEY_NUMPAD7)) {
      world.moveEntity(world.player, Direction.Northwest)
    } else if (input.isKeyPressed(Input.KEY_NUMPAD9)) {
      world.moveEntity(world.player, Direction.Northeast)
    } else if (input.isKeyPressed(Input.KEY_NUMPAD1)) {
      world.moveEntity(world.player, Direction.Southwest)
    } else if (input.isKeyPressed(Input.KEY_NUMPAD3)) {
      world.moveEntity(world.player, Direction.Southeast)
    }else if (input.isKeyPressed(Input.KEY_ENTER)){
      start = true
    }
    if (start){
      world.update()
    }
  }

  override def render(gc: GameContainer, g: Graphics) {
    g.setColor(Color.black)
    g.fillRect(0, 0, gc.getWidth, gc.getHeight)
    //g.translate(gc.getWidth / 2, gc.getHeight / 2)
    world.draw(g)
  }
}


