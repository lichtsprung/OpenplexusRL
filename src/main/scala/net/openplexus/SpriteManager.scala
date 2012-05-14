package net.openplexus

import org.newdawn.slick.{Image, SpriteSheet}


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

  def getEmptyCell(): Image = sprites.getSprite(1, 0)

  def getBlockedCell(): Image = sprites.getSprite(2, 0)
}
