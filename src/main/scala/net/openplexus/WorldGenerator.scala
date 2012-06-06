package net.openplexus

import util.Random

/**
 * A world generator based on a cellular automaton.
 */
class WorldGenerator(val map: WorldMap) {
  private var counter = 0

  def init() = {
    map.cells.foreach(cell => {
      if (Random.nextDouble() < 0.09) {
        cell.visible = true
        cell.neighbors.foreach(t => {
          if (Random.nextDouble() < 0.02){
            t._2.visible = true
          }
        })
      }
    })
  }

  init()

  def update() = {
    if (counter % 30 == 0) {
      map.cells.foreach(cell => {
       if (cell.visible){
         if (getVisibleNeighborCount(cell) > 4) {
           cell.visible = false
         } else if (getVisibleNeighborCount(cell) < 2) {
           cell.visible = false
         }
       }else{
         if (getVisibleNeighborCount(cell) == 2) {
           cell.visible = true
         }
       }
      })
    }
    counter += 1
  }

  private def getVisibleNeighborCount(cell: Cell) = {
    cell.neighbors.count(c => c._2.visible)
  }
}
