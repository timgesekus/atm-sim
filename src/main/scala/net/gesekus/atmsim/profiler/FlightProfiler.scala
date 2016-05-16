package net.gesekus.atmsim.profiler

import net.gesekus.atmsim.model.aircraft.Aircraft
import net.gesekus.atmsim.model.aircraft.AircraftType
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.DegreePerSecond
import net.gesekus.atmsim.model.Degree

class FlightProfiler {
}
object FlightProfiler {
  def simulate(aircraft: Aircraft, aircraftModel: AircraftType, time: Int): Aircraft = {
    def calcNewRoc: MeterPerSecond = {
      if (aircraft.position.x < aircraft.requestedAltitude) {
        MeterPerSecond(aircraft.roc.mps - (aircraftModel.accelerationChangePerSecond * time))
      } else {
        aircraft.roc
      }
    }

    def calcNewRot: DegreePerSecond = {
      if (aircraft.heading.angel < aircraft.requestedHeading.angel) {
        DegreePerSecond(aircraft.rot.dps - (aircraftModel.rotChangePerSecond * time))
      } else {
        aircraft.rot
      }
    }

    val newRoc = calcNewRoc
    val newRot = calcNewRot
    val newZ = aircraft.position.z + (newRoc.mps * time)
    val newHeading = Degree(aircraft.heading.angel + (newRot.dps * time))
    val xMul: Double = Math.sin(Math.toRadians(aircraft.heading.angel))
    val yMul: Double = Math.cos(Math.toRadians(aircraft.heading.angel))

    val newX = aircraft.position.x + xMul * toMiles(aircraft.ias.mps * time)
    val newY = aircraft.position.y + yMul * toMiles(aircraft.ias.mps * time)
    val newPosition = aircraft.position.copy(x = newX, y = newY)
    aircraft.copy(position = newPosition, heading = newHeading, roc = newRoc, rot = newRot)
  }

  private def toMiles(meter: Double): Double = {
    return meter * 0.000621371
  }

}
