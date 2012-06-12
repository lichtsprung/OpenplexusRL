package net.openplexus

import org.newdawn.slick.Graphics


/**
 * World related stuff.
 * blablablablabla
 */

class World(val width: Int, val height: Int) {
  val player = Player()
  val map = WorldMap(player, width, height)
  val worldGenerator = new WorldGenerator(map)


  def moveEntity(entity: Entity, direction: Direction.Direction) = {
    val newPosition = map.getNeighbor(entity, direction)
    map.setPosition(entity, newPosition)
  }

  def update(){
    worldGenerator.update()
  }


  def draw(g: Graphics) {
    map.draw(g)
  }
}


object Direction extends Enumeration {
  type Direction = Value
  val Northeast, Northwest, Southeast, Southwest, East, West = Value
}

object GroundType extends Enumeration{
  type GroundType = Value
  val Nothing, Grass, Forrest, Water, Sand = Value
}



