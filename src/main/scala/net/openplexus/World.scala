package net.openplexus

import org.newdawn.slick.Graphics


/**
 * World related stuff.
 */

class World(val width: Int, val height: Int) {
  val player = Player()
  val map = WorldMap(player, width, height)

  def draw(g: Graphics) {
    map.draw(g)
  }

}

/**
 * A hexagonal cell of the map.
 * @param id the id of this cell
 */
case class Cell(val id: Int) {
  var cellType: String = ""
  var northeast: Cell = null
  var northwest: Cell = null
  var west: Cell = null
  var east: Cell = null
  var southwest: Cell = null
  var southeast: Cell = null
  var visited: Boolean = false
  var visible: Boolean = false
  var marked: Boolean = false

  /**
   * Registers a neighbor east of this cell
   * @param neighbor the neighbor cell
   */
  def registerNeighborEast(neighbor: Cell) = {
    if (neighbor != null) {
      east = neighbor
      neighbor.west = this
    }
  }

  def registerNeighborWest(neighbor: Cell) = {
    if (neighbor != null) {
      west = neighbor
      neighbor.east = this
    }

  }


  def registerNeighborSoutheast(neighbor: Cell) {
    if (neighbor != null) {
      southeast = neighbor
      neighbor.northwest = this
    }

  }

  def registerNeighborSouthwest(neighbor: Cell) {
    if (neighbor != null) {
      southwest = neighbor
      neighbor.northeast = this
    }

  }


  def registerNeighborNortheast(neighbor: Cell) {
    if (neighbor != null) {
      northeast = neighbor
      neighbor.southwest = this
    }

  }

  def registerNeighborNorthwest(neighbor: Cell) {
    if (neighbor != null) {
      northwest = neighbor
      neighbor.southeast = this
    }

  }


  def draw(g: Graphics) = {

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
  private var mark = true

  init(width, height)


  player.setPosition(seedCell)


  private def createEvenRow(cell: Cell, width: Int) = {
    var currentBottom = Cell(idGenerator.nextID())
    val first = currentBottom
    for (i <- 1 to width) {
      currentBottom.registerNeighborEast(Cell(idGenerator.nextID()))
      currentBottom = currentBottom.east
    }

    currentBottom = first
    var currentTop = cell
    currentTop.registerNeighborSouthwest(currentBottom)
    currentBottom = currentBottom.east

    while (currentTop.east != null && currentBottom != null) {
      currentTop.registerNeighborSoutheast(currentBottom)
      currentTop.east.registerNeighborSouthwest(currentBottom)
      currentBottom = currentBottom.east
      currentTop = currentTop.east
    }
    first
  }

  private def createUnevenRow(cell: Cell, width: Int) = {

    var currentBottom = Cell(idGenerator.nextID())
    val first = currentBottom
    for (i <- 1 to width) {
      currentBottom.registerNeighborEast(Cell(idGenerator.nextID()))
      currentBottom = currentBottom.east
    }

    currentBottom = first
    var currentTop = cell
    while (currentTop.east != null && currentBottom != null) {
      currentTop.registerNeighborSoutheast(currentBottom)
      currentTop.east.registerNeighborSouthwest(currentBottom)
      currentBottom = currentBottom.east
      currentTop = currentTop.east
    }
    currentTop.registerNeighborSoutheast(currentBottom)
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
      current.registerNeighborEast(Cell(idGenerator.nextID()))
      current = current.east
    }

    current = seedCell


    for (i <- 1 to height) {
      if (i % 2 == 0) {
        current = createEvenRow(current, width)
      } else {
        current = createUnevenRow(current, width)
      }
    }
  }


  private def drawCell(cell: Cell): Unit = {
    drawCell(cell, 0, 0)
    mark = !mark
  }

  private def drawCell(cell: Cell, x: Float, y: Float): Unit = {
    if (cell != null && cell.marked != mark) {
      Game.sprites.getEmptyCell().draw(x, y)
      cell.marked = !cell.marked

      drawWest(cell.west, x, y)
      drawEast(cell.east, x, y)
      drawNortheast(cell.northeast, x, y)
      drawNorthwest(cell.northwest, x, y)
      drawSoutheast(cell.southeast, x, y)
      drawSouthwest(cell.southwest, x, y)
    }
  }


  private def drawEast(east: Cell, x: Float, y: Float): Unit = {
    val newX = x + Game.sprites.getSpriteWidth()
    if (east != null && east.marked != mark) {
      drawCell(east, newX, y)
    }
  }

  private def drawWest(west: Cell, x: Float, y: Float): Unit = {
    val newX = x - Game.sprites.getSpriteWidth()
    if (west != null && west.marked != mark) {
      drawCell(west, newX, y)
    }
  }

  private def drawNortheast(northeast: Cell, x: Float, y: Float): Unit = {
    val newX = x + (Game.sprites.getSpriteWidth() / 2)
    val newY = y - Game.sprites.getSpriteHeight() + 8
    if (northeast != null && northeast.marked != mark) {
      drawCell(northeast, newX, newY)
    }
  }

  private def drawSoutheast(southeast: Cell, x: Float, y: Float): Unit = {
    val newX = x + (Game.sprites.getSpriteWidth() / 2)
    val newY = y + Game.sprites.getSpriteHeight() - 8
    if (southeast != null && southeast.marked != mark) {
      drawCell(southeast, newX, newY)
    }
  }

  private def drawNorthwest(northwest: Cell, x: Float, y: Float): Unit = {
    val newX = x - (Game.sprites.getSpriteWidth() / 2)
    val newY = y - Game.sprites.getSpriteHeight() + 8
    if (northwest != null && northwest.marked != mark) {
      drawCell(northwest, newX, newY)
    }
  }

  private def drawSouthwest(southwest: Cell, x: Float, y: Float): Unit = {
    val newX = x - (Game.sprites.getSpriteWidth() / 2)
    val newY = y + Game.sprites.getSpriteHeight() - 8
    if (southwest != null && southwest.marked != mark) {
      drawCell(southwest, newX, newY)
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



