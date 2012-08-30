package net.openplexus

import util.Random
import GroundType._
import collection.mutable.Buffer

/**
 * A world generator based on a cellular automaton.
 */
class WorldGenerator(val map: WorldMap) {
  private var counter = 0
  private val updateInterval = 30
  init()

  def init() {
    val unused = Buffer[Cell]()

    map.cells.foreach(cell => {
      if (Random.nextDouble() < 0.2) {
        cell.cellType = Forrest
      } else {
        unused += cell
      }
    })

    unused.foreach(cell => {
      if (cell != null) {
        if (Random.nextDouble() < 0.05) {
          cell.cellType = Sand
          unused -= cell
        }
      }
    })

    unused.foreach(cell => {
      if (cell != null) {
        if (Random.nextDouble() < 0.2) {
          cell.cellType = Water
          unused -= cell
        }
      }
    })
  }


  def update()  {
    if (counter == 0) {
      map.cells.foreach(cell => {
        val sandCount = getNeighborCount(cell, Sand)
        val forrestCount = getNeighborCount(cell, Forrest)
        val waterCount = getNeighborCount(cell, Water)
        val grassCount = getNeighborCount(cell, Grass)

        cell.cellType match {
          case Forrest =>
            if (grassCount > 4 && Random.nextDouble() < 0.2) {
              cell.cellType = Grass
            } else if (waterCount > 3 && Random.nextDouble() < 0.3) {
              cell.cellType = Water
            }
          case Grass =>
            if (forrestCount > 3 && Random.nextDouble() < 0.5) {
              cell.cellType = Forrest
            } else if (waterCount > 2 && Random.nextDouble() < 0.3) {
              cell.cellType = Water
            } else if (sandCount > 2 && waterCount < 2 && Random.nextDouble() < 0.1) {
              cell.cellType = Sand
            }
          case Sand =>
            if (waterCount > 1 && forrestCount > 1 && Random.nextDouble() < 0.5) {
              cell.cellType = Forrest
            } else if (waterCount > 3 && Random.nextDouble() < 0.2) {
              cell.cellType = Water
            } else if (waterCount > 1 && forrestCount < 2 && Random.nextDouble() < 0.5) {
              cell.cellType = Grass
            }
          case Water =>
            if (grassCount > 4 && Random.nextDouble() < 0.4) {
              cell.cellType = Grass
            } else if (sandCount > 2 && Random.nextDouble() < 0.4) {
              cell.cellType = Sand
            } else if (forrestCount > 3 && Random.nextDouble() < 0.4) {
              cell.cellType = Forrest
            }
        }
      })
    }
    counter = (counter + 1) % updateInterval
  }

  private def getNeighborCount(cell: Cell, groundType: GroundType) = {
    cell.neighbors.count(c => c._2.cellType == groundType)
  }
}
