package net.openplexus

import org.newdawn.slick.Graphics
import collection.mutable.Buffer


/**
 *
 * Player related stuff.
 *
 */

case class Player() extends Entity {
  private val sprite = Game.sprites.getPlayerSprite()


  def draw(g: Graphics, x: Float, y: Float) {
    sprite.draw(x, y)
  }
}

/**
 * Everything that moves should implement this trait.
 */

abstract class Entity {
  def draw(g: Graphics, x: Float, y: Float)
}