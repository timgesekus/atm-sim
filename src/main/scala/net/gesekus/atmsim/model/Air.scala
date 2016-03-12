package net.gesekus.atmsim.model

case class Degree(angel: Double)
case class MeterPerSecond(mps: Double)

case class Position3D(x: Double, y: Double, z: Double)
case class AircraftId(id: Int)

case class Aircraft(id: AircraftId, heading: Degree, position: Position3D, groundSpeed: MeterPerSecond)