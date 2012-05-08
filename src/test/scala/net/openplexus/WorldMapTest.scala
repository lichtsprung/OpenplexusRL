package net.openplexus

import org.scalatest.FunSuite

/**
 * Tests for the WorldMap.
 */

class WorldMapTest extends FunSuite{
  test("Creating an empty WorldMap and spawn test room"){
    val worldMap = WorldMap(Player())
    worldMap.init(5,5)
    worldMap.traversMap()
  }
}
