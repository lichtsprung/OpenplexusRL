package net.openplexus

import org.newdawn.slick.Graphics
import net.openplexus.Direction._
import collection.mutable.{Buffer, HashMap}

/**
 * Maps and Renderers for the Map.
 */


/**
 * A hexagonal cell of the map.
 * @param id the id of this cell
 */
case class Cell(val id: Int) {
  private val neighbors = HashMap[Direction, Cell]()
  private val entities = Buffer[Entity]()
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


  def neighbor(direction: Direction): Cell = {
    if (neighbors.contains(direction)) {
      return neighbors(direction)
    }
    return null
  }

  def draw(g: Graphics, x: Float, y: Float) = {
    if (visible) {
      Game.sprites.getEmptyCell().draw(x, y)
      entities.foreach(entity => entity.draw(g, x, y))
    }
  }

  def addEntity(entity: Entity) = {
    entities += entity
  }

  def removeEntity(entity: Entity) = {
    entities -= entity
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
  //setPosition(player, seedCell)

  def setPosition(entity: Entity, position: Cell): Cell = {
    if (positions.contains(player)) {
      val oldPosition = positions(entity)
      oldPosition.removeEntity(entity)
      position.addEntity(entity)
      positions += entity -> position
      return oldPosition
    } else {
      positions += entity -> position
      return null
    }

  }

  def getNeighbor(entity: Entity, direction: Direction) = {
    if (positions(entity).neighbor(direction) == null) {
      positions(entity).registerNeighbor(Cell(idGenerator.nextID()), direction)
    }
    positions(entity).neighbor(direction)
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
      currentTop.neighbor(East).registerNeighbor(currentBottom, Southwest)
      currentBottom = currentBottom.neighbor(East)
      currentTop = currentTop.neighbor(East)
    }
    first
  }

  private def createUnevenRow(cell: Cell, width: Int) = {

    var currentBottom = Cell(idGenerator.nextID())
    val first = currentBottom
    for (i <- 1 to width) {
      currentBottom.registerNeighbor(Cell(idGenerator.nextID()), East)
      currentBottom = currentBottom.neighbor(East)
    }

    currentBottom = first
    var currentTop = cell
    while (currentTop.neighbor(East) != null && currentBottom != null) {
      currentTop.registerNeighbor(currentBottom, Southeast)
      currentTop.neighbor(East).registerNeighbor(currentBottom, Southwest)
      currentBottom = currentBottom.neighbor(East)
      currentTop = currentTop.neighbor(East)
    }
    currentTop.registerNeighbor(currentBottom, Southeast)
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
      current.registerNeighbor(Cell(idGenerator.nextID()), East)
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

    setPosition(player, seedCell)
  }


  // Drawing should be in a separate class MapRenderer!
  private def drawCell(g: Graphics, cell: Cell): Unit = {
    drawCell(g, cell, 0, 0)
    mark = !mark
  }

  private def drawCell(g: Graphics, cell: Cell, x: Float, y: Float): Unit = {
    if (cell != null && cell.marked != mark) {
      cell.draw(g, x, y)
      cell.marked = !cell.marked

      draw(g, cell.neighbor(West), x, y, West)
      draw(g, cell.neighbor(East), x, y, East)
      draw(g, cell.neighbor(Northeast), x, y, Northeast)
      draw(g, cell.neighbor(Northwest), x, y, Northwest)
      draw(g, cell.neighbor(Southeast), x, y, Southeast)
      draw(g, cell.neighbor(Southwest), x, y, Southwest)
    }
  }


  def draw(g: Graphics, cell: Cell, x: Float, y: Float, direction: Direction) = {
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
      drawCell(g, cell, newX, newY)
    }
  }

  def draw(g: Graphics) = {
    drawCell(g, seedCell)
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