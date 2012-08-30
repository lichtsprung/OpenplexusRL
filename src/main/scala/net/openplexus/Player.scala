package net.openplexus

import org.newdawn.slick.Graphics


/**
 *
 * Player related stuff.
 *
 */

case class Player() extends Entity {
  private val sprite = Game.sprites.getPlayerSprite
  private var hunger = 0.0

  def draw(g: Graphics, x: Float, y: Float) {
    sprite.draw(x, y)
  }

  def update(){
    hunger += 0.01
  }
}

/**
 * Everything that moves should implement this trait.
 */

abstract class Entity {
  def draw(g: Graphics, x: Float, y: Float)
  def update()
}