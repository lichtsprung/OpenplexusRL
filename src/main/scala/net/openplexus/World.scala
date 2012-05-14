package net.openplexus

import org.newdawn.slick.Graphics
import collection.mutable.Buffer


/**
 * World related stuff.
 */

class World() {
  val player = Player()
  val map = WorldMap(player)

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

  var north: Cell = null
  var northeast: Cell = null
  var northwest: Cell = null
  var west: Cell = null
  var east: Cell = null
  var south: Cell = null
  var southwest: Cell = null
  var southeast: Cell = null



  private val entities = Buffer[Entity]()
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

  def registerNeighborSouth(neighbor: Cell) {
    if (neighbor != null) {
      south = neighbor
      neighbor.north = this
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

  def registerNeighborNorth(neighbor: Cell) {
    if (neighbor != null) {
      north = neighbor
      neighbor.south = this
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


  /**
   * Registers an entity with this cell.
   * @param entity the new entity
   * @return a list of all entities on this cell
   */
  def registerEntity(entity: Entity) = entities += entity

  /**
   * Removes an entity from this cell
   * @param entity the entity to be removed
   * @return a list of all entities on this cell
   */
  def unregisterEntity(entity: Entity) = entities -= entity

  def draw(g: Graphics) = {

  }

  override def toString = String.valueOf(id)

}

/**
 * The world map.
 * @param player the player
 */
case class WorldMap(val player: Player) {
  private val idGenerator = new IDGenerator()
  private val seedCell = Cell(idGenerator.nextID())
  private var mark = false

  init()
  resetMarkers()

  player.setPosition(seedCell)


  /**
   * Initializes the map.
   * @param width the starting width of the map
   * @param height the starting height of the map
   */
  def init(width: Int = 5, height: Int = 5) = {
    var top = seedCell

    // Oberste Zeile der Map erzeugen.
    for (i <- 0 to width) {
      val topEast = Cell(idGenerator.nextID())
      top.registerNeighborEast(topEast)
      top = topEast
    }

    var bottom = Cell(idGenerator.nextID())
    var nextTop = bottom
    top = seedCell

    for (k <- 1 to height) {

      for (i <- 1 to width) {
        val topEast = top.east
        val bottomEast = Cell(idGenerator.nextID())

        top.registerNeighborSoutheast(bottomEast)
        top.registerNeighborSouth(bottom)

        bottom.registerNeighborEast(bottomEast)
        bottom.registerNeighborNortheast(topEast)

        top = topEast
        bottom = bottomEast

      }
      top = nextTop
      bottom = Cell(idGenerator.nextID())
      nextTop = bottom
    }
  }

  /**
   * Traverses the underlying graph and prints some information about the cells
   */
  def traversMap(): Unit = {
    visit(seedCell)
    resetMarkers()
  }

  def resetMarkers(): Unit = reset(seedCell)

  private def reset(cell: Cell):Unit = {
    if (cell != null && cell.marked) {
      cell.marked = false
      reset(cell.north)
      reset(cell.northwest)
      reset(cell.west)
      reset(cell.southwest)
      reset(cell.south)
      reset(cell.southeast)
      reset(cell.east)
      reset(cell.northeast)
    }
  }

  private def visit(cell: Cell): Unit = {
    if (cell != null && !cell.marked) {
      //      printf("Cell %s is connected to \n", cell)
      //      printf("\tNorth: Cell %s\n", cell.north)
      //      printf("\tNorthwest: Cell %s\n", cell.northwest)
      //      printf("\tWest: Cell %s\n", cell.west)
      //      printf("\tSouthwest: Cell %s\n", cell.southwest)
      //      printf("\tSouth: Cell %s\n", cell.south)
      //      printf("\tSoutheast: Cell %s\n", cell.southeast)
      //      printf("\tEast: Cell %s\n", cell.east)
      //      printf("\tNortheast: Cell %s\n", cell.northeast)
      cell.marked = true
      visit(cell.north)
      visit(cell.northwest)
      visit(cell.west)
      visit(cell.southwest)
      visit(cell.south)
      visit(cell.southeast)
      visit(cell.east)
      visit(cell.northeast)
    }
  }


  def draw(g: Graphics) = {
    drawCell(seedCell, 0, 0)
    mark = !mark
  }

  private def drawCell(cell: Cell, indexX: Int, indexY: Int): Unit = {
    if (cell != null && mark == cell.marked) {
      Game.sprites.getEmptyCell().draw(indexX * Game.sprites.getSpriteWidth(), indexY * Game.sprites.getSpriteHeight())
      cell.marked = true
      drawCell(cell.north, indexX, indexY - 1)
      drawCell(cell.northwest, indexX - 1, indexY - 1)
      drawCell(cell.west, indexX - 1, indexY)
      drawCell(cell.southwest, indexX - 1, indexY + 1)
      drawCell(cell.south, indexX, indexY + 1)
      drawCell(cell.southeast, indexX + 1, indexY + 1)
      drawCell(cell.east, indexX + 1, indexY)
      drawCell(cell.northeast, indexX + 1, indexY + 1)
    } else {
      Game.sprites.getBlockedCell().draw(indexX * Game.sprites.getSpriteWidth(), indexY * Game.sprites.getSpriteHeight())
    }
  }

  def movePlayerNorth() = {
    if (player.getPosition.north != null) {
      player.setPosition(player.getPosition.north)
    }
  }

  def movePlayerSouth() = {
    if (player.getPosition.south != null) {
      player.setPosition(player.getPosition.south)
    }
  }

  // TODO fertigstellen
}

private class IDGenerator {
  private var id = 0

  def nextID() = {
    id += 1
    id
  }

  def lastID() = id

}



