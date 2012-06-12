package net.openplexus

import org.newdawn.slick.{Image, SpriteSheet}
import GroundType._


/**
 * Handling sprites.
 */

class SpriteManager {
  private val sprites = new SpriteSheet("graphics.png", 32, 32)
  private val height = sprites.getHeight / sprites.getVerticalCount
  private val width = sprites.getWidth / sprites.getHorizontalCount

  def getPlayerSprite(): Image = sprites.getSprite(0, 0)

  def getSpriteHeight(): Int = height

  def getSpriteWidth(): Int = width

  def groundSprite(groundType: GroundType): Image = {
    groundType match {
      case Nothing =>
        sprites.getSprite(1, 0)
      case Grass =>
        sprites.getSprite(0, 1)
      case Water =>
        sprites.getSprite(2, 1)
      case Forrest =>
        sprites.getSprite(3, 1)
      case Sand =>
        sprites.getSprite(1, 1)
    }
  }
}
