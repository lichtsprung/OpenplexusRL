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
case class Cell(val id: Int, var x: Float = 0.0f, var y: Float = 0.0f) {
  val neighbors = HashMap[Direction, Cell]()
  private val entities = Buffer[Entity]()
  var visited: Boolean = false
  // TODO needs to be set according to player position
  var visible: Boolean = false


  def registerNeighbor(neighbor: Cell, x: Float, y: Float, direction: Direction) = {
    var newX = 0.0f
    var newY = 0.0f
    direction match {
      case East =>
        newX = x + Game.sprites.getSpriteWidth()
        newY = y
        neighbors += East -> neighbor
        neighbor.neighbors += West -> this
      case West =>
        newX = x - Game.sprites.getSpriteWidth()
        newY = y
        neighbors += West -> neighbor
        neighbor.neighbors += East -> this
      case Northeast =>
        newX = x + (Game.sprites.getSpriteWidth() / 2)
        newY = y - Game.sprites.getSpriteHeight() + 8
        neighbors += Northeast -> neighbor
        neighbor.neighbors += Southwest -> this
      case Northwest =>
        newX = x - (Game.sprites.getSpriteWidth() / 2)
        newY = y - Game.sprites.getSpriteHeight() + 8
        neighbors += Northwest -> neighbor
        neighbor.neighbors += Southeast -> this
      case Southeast =>
        newX = x + (Game.sprites.getSpriteWidth() / 2)
        newY = y + Game.sprites.getSpriteHeight() - 8
        neighbors += Southeast -> neighbor
        neighbor.neighbors += Northwest -> this
      case Southwest =>
        newX = x - (Game.sprites.getSpriteWidth() / 2)
        newY = y + Game.sprites.getSpriteHeight() - 8
        neighbors += Southwest -> neighbor
        neighbor.neighbors += Northeast -> this
    }
    neighbor.x = newX
    neighbor.y = newY
  }


  def neighbor(direction: Direction): Cell = {
    if (neighbors.contains(direction)) {
      return neighbors(direction)
    }
    return null
  }

  def draw(g: Graphics) = {
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
  private val positions = HashMap[Entity, Cell]()
  val cells = Buffer[Cell]()
  private val seedCell = createCell
  private var mark = true

  init(25, 25)

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
    val currentCell = positions(entity)
    if (currentCell.neighbor(direction) == null) {
      positions(entity).registerNeighbor(createCell, currentCell.x, currentCell.y, direction)
    }
    positions(entity).neighbor(direction)
  }

  private def createSouthwestNeighborRow(cell: Cell) = {
    var bottom = createCell
    var top = cell
    val first = bottom

    while (top.neighbor(East) != null) {
      top.registerNeighbor(bottom, top.x, top.y, Southwest)
      val bottomEast = createCell
      bottom.registerNeighbor(bottomEast, bottom.x, bottom.y, East)
      bottom = bottom.neighbor(East)
      top.registerNeighbor(bottomEast, top.x, top.y, Southeast)
      top = top.neighbor(East)
    }
    top.registerNeighbor(bottom, top.x, top.y, Southwest)

    first
  }

  private def createSoutheastNeighborRow(cell: Cell) = {

    var currentBottom = createCell
    var currentTop = cell
    val first = currentBottom

    while (currentTop.neighbor(East) != null) {
      currentTop.registerNeighbor(currentBottom, currentTop.x, currentTop.y, Southeast)
      currentTop.neighbor(East).registerNeighbor(currentBottom, currentTop.neighbor(East).x, currentTop.neighbor(East).y, Southwest)
      val newCell = createCell
      currentBottom.registerNeighbor(newCell, currentBottom.x, currentBottom.y, East)
      currentBottom = currentBottom.neighbor(East)
      currentTop = currentTop.neighbor(East)
    }

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
    for (i <- 2 to width) {
      current.registerNeighbor(createCell, current.x, current.y, East)
      current = current.neighbor(East)
    }

    current = seedCell


    for (i <- 2 to height) {
      if (i % 2 == 0) {
        current = createSoutheastNeighborRow(current)
      } else {
        current = createSouthwestNeighborRow(current)
      }
    }

    setPosition(player, seedCell)
  }


  private def createCell: Cell = {
    val cell = Cell(idGenerator.nextID())
    cells += cell
    cell
  }


  def draw(g: Graphics) = {
    cells.foreach(cell => cell.draw(g))
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