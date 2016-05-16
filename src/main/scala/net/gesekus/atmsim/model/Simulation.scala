package net.gesekus.atmsim.model

import java.time.LocalDateTime
import net.gesekus.atmsim.model.aircraft.Aircraft
import net.gesekus.atmsim.model.aircraft.AircraftType

case class AircraftTypeId(id: Int)
case class SimTime(simTime: LocalDateTime)

case class Simulation(
    aircraft: Map[AircraftId, Aircraft], 
    aicraftTypes: Map[AircraftTypeId, AircraftType], 
    simulationTime: SimTime
    )