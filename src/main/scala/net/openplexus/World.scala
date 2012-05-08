package net.openplexus

import org.newdawn.slick.Graphics
import collection.mutable.Buffer


/**
 * World related stuff.
 */

class World() {
  val player = Player()
  val map = WorldMap(player)

}

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


  def registerEntity(entity: Entity) = entities += entity

  def unregisterEntity(entity: Entity) = entities -= entity

  def draw(g: Graphics, sprites: SpriteManager) = {

  }

}

case class WorldMap(val player: Player) {
  private val idGenerator = new IDGenerator()
  private val seedCell = Cell(idGenerator.nextID())

  player.setPosition(seedCell)


  def init(width: Int, height: Int) = {
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

        println("top", top)
        println("bottom", bottom)
        println("topEast", topEast)
        println("bottomEast", bottomEast)
        println


        top.registerNeighborSoutheast(bottomEast)
        top.registerNeighborSouth(bottom)

        bottom.registerNeighborEast(bottomEast)
        bottom.registerNeighborNortheast(topEast)

        top = topEast
        bottom = bottomEast

      }

      println("------------------------------------------")
      top = nextTop
      bottom = Cell(idGenerator.nextID())
      nextTop = bottom
    }
  }

  def traversMap(): Unit = traversMap(seedCell)

  private def traversMap(cell: Cell): Unit = {
    if (cell != null) {
      println("Cell {} is connected to ", cell.id)
    }
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



