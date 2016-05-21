package net.gesekus.atmsim.model.aircraft

import net.gesekus.atmsim.model.Position3D
import net.gesekus.atmsim.model.Degree
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.DegreePerSecond

case class Aircraft(position: Position3D,
                    heading: Degree,
                    ias: MeterPerSecond,
                    roc: MeterPerSecond,
                    rot: DegreePerSecond,
                    requestedIas: MeterPerSecond,
                    requestedHeading: Degree,
                    requestedAltitude: Double)
                    
case class AircraftType(name: String,
                        iasChangePerSecond: Double,
                        rocChangePerSecond: Double,
                        rotChangePerSecond: Double,
                        accelerationChangePerSecond: Double)
