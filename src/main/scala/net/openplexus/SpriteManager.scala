package net.openplexus

import org.newdawn.slick.{Image, SpriteSheet}


/**
 * Handling sprites.
 */

class SpriteManager {
  private val sprites = new SpriteSheet("graphics.png", 16, 16)
  private val height = sprites.getHeight / sprites.getVerticalCount
  private val width = sprites.getWidth / sprites.getHorizontalCount

  def getPlayerSprite(): Image = sprites.getSprite(0, 0)

  def getSpriteHeight(): Int = height

  def getSpriteWidth(): Int = width

  def getEmptyCell(): Image = sprites.getSprite(0, 1)

  def getBlockedCell(): Image = sprites.getSprite(0, 8)


}
