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

class AircraftProfilerSpec (_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("HelloAkkaSpec"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "An AircraftPofiler" should "be able reposition aircraft" in {
    val profiler = TestActorRef(AircraftProfiler.props(AircraftId(1), LocalDateTime.now()))
    profiler ! ChangePosition(position = Position3D (100,200,100))
    expectMsgType[PositionChanged].poistion should be(Position3D (100,200,100))
  }
}
