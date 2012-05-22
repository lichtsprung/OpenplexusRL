package net.openplexus

import org.newdawn.slick.Graphics
import collection.mutable.HashMap
import Direction._

/**
 * Maps and Renderers for the Map.
 */

object Direction extends Enumeration {
  type Direction = Value
  val Northeast, Northwest, Southeast, Southwest, East, West = Value
}

/**
 * A hexagonal cell of the map.
 * @param id the id of this cell
 */
case class Cell(val id: Int) {
  private val neighbors = HashMap[Direction, Cell]()
  var visited: Boolean = false
  // TODO needs to be set according to player position
  var visible: Boolean = true
  var marked: Boolean = false


  def registerNeighbor(neighbor: Cell, direction: Direction) = {
    direction match {
      case East =>
        neighbors += East -> neighbor
        neighbor.neighbors += West -> this
      case West =>
        neighbors += West -> neighbor
        neighbor.neighbors += East -> this
      case Northeast =>
        neighbors += Northeast -> neighbor
        neighbor.neighbors += Southwest -> this
      case Northwest =>
        neighbors += Northwest -> neighbor
        neighbor.neighbors += Southeast -> this
      case Southeast =>
        neighbors += Southeast -> neighbor
        neighbor.neighbors += Northwest -> this
      case Southwest =>
        neighbors += Southwest -> neighbor
        neighbor.neighbors += Northeast -> this
    }
  }


  def neighbor(direction: Direction) = {
    neighbors(direction)
  }


  def draw(x: Float, y: Float) = {
    if (visible) {
      Game.sprites.getEmptyCell().draw(x, y)
    } else if (visited) {
      Game.sprites.getBlockedCell().draw(x, y)
    }
  }

  override def toString = String.valueOf(id)

}

/**
 * The world map.
 * @param player the player
 */
case class WorldMap(val player: Player, var width: Int, var height: Int) {

  private val idGenerator = new IDGenerator()
  private val seedCell = Cell(idGenerator.nextID())
  private val positions = HashMap[Entity, Cell]()
  private var mark = true

  init(width, height)

  def setPosition(entity: Entity, position: Cell) = {
    val oldPosition = positions(entity)
    positions += entity -> position
    oldPosition
  }


  private def createEvenRow(cell: Cell, width: Int) = {
    var currentBottom = Cell(idGenerator.nextID())
    val first = currentBottom
    for (i <- 1 to width) {
      currentBottom.registerNeighbor(Cell(idGenerator.nextID()), East)
      currentBottom = currentBottom.neighbor(East)
    }

    currentBottom = first
    var currentTop = cell
    currentTop.registerNeighbor(currentBottom, Southwest)
    currentBottom = currentBottom.neighbor(East)

    while (currentTop.neighbor(East) != null && currentBottom != null) {
      currentTop.registerNeighbor(currentBottom, Southeast)
      currentTop.neighbor(East).registerNeighbor(currentBottom,Southwest)
      currentBottom = currentBottom.neighbor(East)
      currentTop = currentTop.neighbor(East)
    }
    first
  }

  private def createUnevenRow(cell: Cell, width: Int) = {

    var currentBottom = Cell(idGenerator.nextID())
    val first = currentBottom
    for (i <- 1 to width) {
      currentBottom.registerNeighbor(Cell(idGenerator.nextID()),East)
      currentBottom = currentBottom.neighbor(East)
    }

    currentBottom = first
    var currentTop = cell
    while (currentTop.neighbor(East) != null && currentBottom != null) {
      currentTop.registerNeighbor(currentBottom,Southeast)
      currentTop.neighbor(East).registerNeighbor(currentBottom,Southwest)
      currentBottom = currentBottom.neighbor(East)
      currentTop = currentTop.neighbor(East)
    }
    currentTop.registerNeighbor(currentBottom,Southeast)
    first
  }

  /**
   * Initializes the map.
   * @param width the starting width of the map
   * @param height the starting height of the map
   */
  private def init(width: Int = 5, height: Int = 5) = {
    // create top row
    var current = seedCell
    for (i <- 1 to width) {
      current.registerNeighbor(Cell(idGenerator.nextID()),East)
      current = current.neighbor(East)
    }

    current = seedCell


    for (i <- 1 to height) {
      if (i % 2 == 0) {
        current = createEvenRow(current, width)
      } else {
        current = createUnevenRow(current, width)
      }
    }

    positions += player -> seedCell
  }


  // Drawing should be in a separate class MapRenderer!
  private def drawCell(cell: Cell): Unit = {
    drawCell(cell, 0, 0)
    mark = !mark
  }

  private def drawCell(cell: Cell, x: Float, y: Float): Unit = {
    if (cell != null && cell.marked != mark) {
      cell.draw(x, y)
      cell.marked = !cell.marked

      draw(cell.west, x, y, West)
      draw(cell.east, x, y, East)
      draw(cell.northeast, x, y, Northeast)
      draw(cell.northwest, x, y, Northwest)
      draw(cell.southeast, x, y, Southeast)
      draw(cell.southwest, x, y, Southwest)
    }
  }


  def draw(cell: Cell, x: Float, y: Float, direction: Direction) = {
    var newX = 0.0f
    var newY = 0.0f
    direction match {
      case East =>
        newX = x + Game.sprites.getSpriteWidth()
        newY = y
      case West =>
        newX = x - Game.sprites.getSpriteWidth()
        newY = y
      case Northeast =>
        newX = x + (Game.sprites.getSpriteWidth() / 2)
        newY = y - Game.sprites.getSpriteHeight() + 8
      case Northwest =>
        newX = x - (Game.sprites.getSpriteWidth() / 2)
        newY = y - Game.sprites.getSpriteHeight() + 8
      case Southeast =>
        newX = x + (Game.sprites.getSpriteWidth() / 2)
        newY = y + Game.sprites.getSpriteHeight() - 8
      case Southwest =>
        newX = x - (Game.sprites.getSpriteWidth() / 2)
        newY = y + Game.sprites.getSpriteHeight() - 8
    }

    if (cell != null && cell.marked != mark) {
      drawCell(cell, newX, newY)
    }
  }

  def draw(g: Graphics) = {
    drawCell(seedCell)
  }
}

private class IDGenerator {
  private var id = 0

  def nextID() = {
    id += 1
    id
  }

  def lastID() = id
}


