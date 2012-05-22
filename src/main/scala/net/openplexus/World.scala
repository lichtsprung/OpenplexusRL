package net.openplexus

import org.newdawn.slick.Graphics
import collection.mutable.HashMap


/**
 * World related stuff.
 * blablablablabla
 */

class World(val width: Int, val height: Int) {
  val player = Player()
  val map = WorldMap(player, width, height)


  def moveEast(entity: Entity) = {
    val newPosition = map.getNeighbour(entity, Direction.East)
    map.setPosition(entity, newPosition)
  }


  def draw(g: Graphics) {
    map.draw(g)
  }

}




