package net.openplexus

import org.newdawn.slick.Graphics


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
case class WorldMap(val player: Player) {
  private val idGenerator = new IDGenerator()
  private val seedCell = Cell(idGenerator.nextID())
  private var mark = true

  init()


  player.setPosition(seedCell)


  def createEvenRow(cell: Cell, width: Int): Unit = {

  }

  def createUnevenRow(cell: Cell, width: Int): Unit = {

  }

  /**
   * Initializes the map.
   * @param width the starting width of the map
   * @param height the starting height of the map
   */
  def init(width: Int = 5, height: Int = 5) = {
    // create top row
        var current = seedCell
        for (i <- 1 to width) {
          current.registerNeighborEast(Cell(idGenerator.nextID()))
          current = current.east
        }

        current = seedCell
        createUnevenRow(current, width)

//        for (i<- 1 to height){
//          if (i % 2 == 0){
//            createEvenRow(current, width)
//          }else{
//            createUnevenRow(current, width)
//          }
//        }




//    seedCell.registerNeighborNortheast(Cell(idGenerator.nextID()))
//    seedCell.northeast.registerNeighborNortheast(Cell(idGenerator.nextID()))
//
//    seedCell.registerNeighborNorthwest(Cell(idGenerator.nextID()))
//    seedCell.northwest.registerNeighborNorthwest(Cell(idGenerator.nextID()))
//
//    seedCell.registerNeighborSouthwest(Cell(idGenerator.nextID()))
//    seedCell.southwest.registerNeighborSouthwest(Cell(idGenerator.nextID()))
//
//    seedCell.registerNeighborSoutheast(Cell(idGenerator.nextID()))
//    seedCell.southeast.registerNeighborSoutheast(Cell(idGenerator.nextID()))
    //    seedCell.registerNeighborEast(Cell(idGenerator.nextID()))
    //    seedCell.registerNeighborSoutheast(Cell(idGenerator.nextID()))
    //    seedCell.registerNeighborSouthwest(Cell(idGenerator.nextID()))
    //    seedCell.registerNeighborWest(Cell(idGenerator.nextID()))
    //    seedCell.registerNeighborNorthwest(Cell(idGenerator.nextID()))
  }


  def drawCell(cell: Cell): Unit = {
    drawCell(cell, 0, 0)
    mark = !mark
  }

  def drawCell(cell: Cell, x: Int, y: Int): Unit = {
    if (cell != null && cell.marked != mark) {
      val width = x * Game.sprites.getSpriteWidth().toFloat
      val height = y * (Game.sprites.getSpriteHeight()).toFloat
      Game.sprites.getEmptyCell().draw(width, height)
      cell.marked = !cell.marked
      drawDiagonalCell(cell.northeast, x + 1, y - 1)
      drawCell(cell.east, x + 1, y)
      drawDiagonalCell(cell.southeast, x + 1, y + 1)
      drawDiagonalCell(cell.southwest, x - 1, y + 1)
      drawCell(cell.west, x - 1, y)
      drawDiagonalCell(cell.northwest, x - 1, y - 1)
    }
  }

  def drawDiagonalCell(cell: Cell, x: Int, y: Int): Unit = {
    if (cell != null && cell.marked != mark) {
      val width = x * (Game.sprites.getSpriteWidth() - (Game.sprites.getSpriteWidth() / 2)).toFloat
      val height = y * (Game.sprites.getSpriteHeight() - 8).toFloat
      Game.sprites.getEmptyCell().draw(width, height)
      cell.marked = !cell.marked
      drawDiagonalCell(cell.northeast, x + 1, y - 1)
      drawCell(cell.east, x + 1, y)
      drawDiagonalCell(cell.southeast, x + 1, y + 1)
      drawDiagonalCell(cell.southwest, x - 1, y + 1)
      drawCell(cell.west, x - 1, y)
      drawDiagonalCell(cell.northwest, x - 1, y - 1)
    }
  }

  def draw(g: Graphics) = {
    drawCell(seedCell)
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



