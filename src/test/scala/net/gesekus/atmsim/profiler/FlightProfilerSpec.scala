package net.gesekus.atmsim.profiler

import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import net.gesekus.atmsim.model.aircraft.Aircraft
import net.gesekus.atmsim.model.Position3D
import net.gesekus.atmsim.model.Degree
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.DegreePerSecond
import net.gesekus.atmsim.model.aircraft.AircraftType
import org.scalactic.Equality

@RunWith(classOf[JUnitRunner])
class FlightProfilerSpec extends FlatSpec with Matchers {
  implicit val personEq =
    new Equality[Position3D] {
      def areEqual(a: Position3D, b: Any): Boolean =
        b match {
          case p: Position3D => a.x === p.x +- 0.01 && a.y === p.y +- 0.01 && a.z === p.z +- 0.01
          case _ => false
        }
    }

  "A FlightPofiler" should "be advance an aircraft" in {
    val aircraft = Aircraft(
      position = Position3D(0, 0, 0),
      heading = Degree(0),
      ias = MeterPerSecond(1609.34),
      roc = MeterPerSecond(0),
      rot = DegreePerSecond(0),
      requestedIas = MeterPerSecond(1609.34),
      requestedHeading = Degree(0),
      requestedAltitude = 0)

      
    val a320 = AircraftType(
      name = "A320",
      iasChangePerSecond = 10.0,
      rocChangePerSecond = 10,
      rotChangePerSecond = 10,
      accelerationChangePerSecond = 10)
    val advancedAircraft = FlightProfiler.simulate(aircraft, a320, 1)
    advancedAircraft.position should equal(Position3D(0, 1, 0))
  }
}