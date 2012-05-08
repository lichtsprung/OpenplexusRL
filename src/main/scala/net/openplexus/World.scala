package net.openplexus

import org.newdawn.slick.Graphics

/**
 * World related stuff.
 */

class World() {
  private val player = new Player
  private val cellWidth = 16

  def playerMoveUp() = player.y -= 1

  def playerMoveDown() = player.y += 1

  def playerMoveRight() = player.x += 1

  def playerMoveLeft() = player.x -= 1


  def draw(g: Graphics) = {
    player.icon.draw(player.x * cellWidth, player.y * cellWidth)
  }

  def update() = {

  }

}

private case class Cell(var north: Cell, var south: Cell, var west: Cell, var east: Cell) {
  val cellType = "none"

}
