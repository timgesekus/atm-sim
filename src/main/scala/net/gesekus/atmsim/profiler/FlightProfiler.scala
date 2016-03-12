package net.gesekus.atmsim.profiler

import akka.actor.Actor
import net.gesekus.atmsim.model.Degree
import net.gesekus.atmsim.model.AircraftId
import net.gesekus.atmsim.model.Aircraft
import net.gesekus.atmsim.model.Position3D
import net.gesekus.atmsim.model.SimTime
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.SimTime
import java.time.LocalDateTime
import java.time.Period
import java.time.Duration
import akka.actor.Props

sealed trait AircraftCommand
case class ChangeHeading(heading: Degree) extends AircraftCommand
case class ChangePosition(position: Position3D) extends AircraftCommand
case class ChangeGroundSpeed(groundSpeed: MeterPerSecond) extends AircraftCommand
case class AdvanceToSimTime(simTime: LocalDateTime) extends AircraftCommand

sealed trait AircraftEvent
case class HeadingChanged(heading: Degree) extends AircraftEvent
case class PositionChanged(poistion: Position3D) extends AircraftEvent
case class GroundSpeedChanged(groundSpeed: MeterPerSecond) extends AircraftEvent
case class SimTimeAdvanced(simTime: LocalDateTime) extends AircraftEvent

object AircraftProfiler {
  
  def props(id: AircraftId, simTime : LocalDateTime) : Props = Props (new AircraftProfiler(id,simTime))
}

class AircraftProfiler(id: AircraftId, simTime : LocalDateTime) extends Actor {
  
  var currentSimTime = simTime
  var aircraft = Aircraft(id = id, heading = Degree(0), position = Position3D(0, 0, 0), groundSpeed = MeterPerSecond(0))
  def receive = {
    case ChangeHeading(heading) => updateState(HeadingChanged(heading))
    case ChangePosition(newPostion) => updateState(PositionChanged(newPostion))
    case ChangeGroundSpeed(newGroundSpeed) => updateState(GroundSpeedChanged(newGroundSpeed))
    case AdvanceToSimTime(simTime) => advanceToSimTime(simTime)
  }

  def updateState(event: AircraftEvent) {
    event match {
      case HeadingChanged(newHeading) => aircraft = aircraft.copy(heading = newHeading)
      case PositionChanged(newPosition) => aircraft = aircraft.copy(position = newPosition)
      case GroundSpeedChanged(newGroundSpeed) => aircraft = aircraft.copy(groundSpeed = newGroundSpeed)
      case SimTimeAdvanced(newSimTime) => currentSimTime = newSimTime
    }
    sender ! event
  }

  def advanceToSimTime(newSimTime: LocalDateTime) {
    val duration = Duration.between(currentSimTime, newSimTime);
    val ds = duration.getSeconds;
    val xMul : Double = Math.sin(aircraft.heading.angel)
    val yMul : Double = Math.cos(aircraft.heading.angel)
   
    val newX = aircraft.position.x + xMul * aircraft.groundSpeed.mps * ds
    val newY = aircraft.position.y + yMul * aircraft.groundSpeed.mps * ds
    val newPosition = aircraft.position.copy( x = newX, y = newY)
    updateState(PositionChanged(newPosition))
    updateState(SimTimeAdvanced(newSimTime))
  }
}