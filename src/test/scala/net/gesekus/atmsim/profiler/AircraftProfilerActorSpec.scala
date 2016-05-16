package net.gesekus.atmsim.profiler

import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef }
import scala.concurrent.duration._
import akka.actor.ActorSystem
import net.gesekus.atmsim.model.AircraftId
import net.gesekus.atmsim.profiler._
import java.time.LocalDateTime
import net.gesekus.atmsim.model.Position3D
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent.Await
import scala.concurrent.duration._
import net.gesekus.atmsim.clock.SimTimeChanged
import net.gesekus.atmsim.model.MeterPerSecond
import akka.actor.ActorRef
import net.gesekus.atmsim.model.Position3D
import org.scalactic.Equality
import net.gesekus.atmsim.model.Position3D
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.MeterPerSecond
import net.gesekus.atmsim.model.Degree
import net.gesekus.atmsim.model.Degree

@RunWith(classOf[JUnitRunner])
class AircraftProfilerActorSpec(_system: ActorSystem)
    extends TestKit(_system)
    with ImplicitSender
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll {

  implicit val personEq =
    new Equality[Position3D] {
      def areEqual(a: Position3D, b: Any): Boolean =
        b match {
          case p: Position3D => a.x === p.x +- 0.01 && a.y === p.y +- 0.01 && a.z === p.z +- 0.01
          case _ => false
        }
    }
  def this() = this(ActorSystem("HelloAkkaSpec"))

  override def afterAll: Unit = {
    val terminate = system.terminate
    Await.ready(terminate, 10 seconds);
  }

  "An AircraftPofiler" should "be able reposition aircraft" in {
    val profiler = TestActorRef(AircraftProfilerActor.props(AircraftId(1), LocalDateTime.now()))
    changePosition(profiler, Position3D(100, 200, 100))
  }

  "An AircraftPofiler" should "be able change the ground speed" in {
    val profiler = TestActorRef(AircraftProfilerActor.props(AircraftId(1), LocalDateTime.now()))
    val gs = MeterPerSecond(800 * 0.277778)
    profiler ! ChangeGroundSpeed(gs)
    expectMsgType[GroundSpeedChanged].groundSpeed should equal(gs)
  }
  "An AircraftPofiler" should "advance an  aircraft with headin=0 on timetick" in {
    val simTime = LocalDateTime.now
    val profiler = TestActorRef(AircraftProfilerActor.props(AircraftId(1), simTime))
    
    changeGroundSpeed(profiler, MeterPerSecond(1609.34))
    changePosition(profiler, Position3D(100, 200, 100))
    
    profiler ! AdvanceToSimTime(simTime.plusSeconds(1))
    expectMsgType[PositionChanged].position should equal(Position3D(100, 201, 100))
    
    profiler ! AdvanceToSimTime(simTime.plusSeconds(2))
    expectMsgType[PositionChanged].position should equal(Position3D(100, 202, 100))
  
  }

  "An AircraftPofiler" should "advance an  aircraft with heading=90 on timetick" in {
    val simTime = LocalDateTime.now
    val profiler = TestActorRef(AircraftProfilerActor.props(AircraftId(1), simTime))
    
    
    changeGroundSpeed(profiler, MeterPerSecond(1609.34))
    changePosition(profiler, Position3D(100, 200, 100))
    changeHeading(profiler, Degree(90))
    profiler ! AdvanceToSimTime(simTime.plusSeconds(1))
    expectMsgType[PositionChanged].position should equal(Position3D(101, 200, 100))
    
    profiler ! AdvanceToSimTime(simTime.plusSeconds(2))
    expectMsgType[PositionChanged].position should equal(Position3D(102, 200, 100))
  
  }

  def changePosition(profiler: ActorRef, position: Position3D) {
    profiler ! ChangePosition(position)
    expectMsgType[PositionChanged].position should equal(position)
  }

  def changeGroundSpeed(profiler: ActorRef, gs: MeterPerSecond) {
    profiler ! ChangeGroundSpeed(gs)
    expectMsgType[GroundSpeedChanged].groundSpeed should equal(gs)
  }
  
  def changeHeading(profiler: ActorRef, heading: Degree) {
    profiler ! ChangeHeading(heading)
    expectMsgType[HeadingChanged].heading should be (heading)
  }
}
