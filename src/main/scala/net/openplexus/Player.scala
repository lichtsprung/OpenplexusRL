package net.openplexus

import org.newdawn.slick.Graphics


/**
 *
 * Player related stuff.
 *
 */

case class Player() extends Entity with Movable{
  private val sprite = Game.sprites.getPlayerSprite()

  def draw(g: Graphics, x: Float, y: Float) {
    sprite.draw(x, y)
  }
}

/**
 * Everything that moves should implement this trait.
 */
trait Movable{
  protected var currentCell: Cell = null

  /**
   * Places the entity to a new Cell of the WorldMap
   * @return the old position on the map
   */
  def setPosition(cell: Cell) = {
    val oldCell = currentCell
    currentCell = cell
    currentCell.visible = true
    currentCell.visited = true
    oldCell
  }


  def getPosition = currentCell

}

abstract class Entity{
  abstract def draw(g: Graphics, x: Float, y: Float)
}