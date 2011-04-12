package org.performanceman

import actors.Actor
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

class CPUprofileActorTest extends FlatSpec with ShouldMatchers with BeforeAndAfterAll {

  var cpucount: Int = 0

  class TestActor extends Actor {
    var intresponse = 0
    var doubleresponse = 0.0
    var loadreport:  List[Double] = List.empty
    var reportcount = 0

    def act = {
      loop {
      react {
        case numport: CPUnumReport => { intresponse = numport.cpunum }
        case freport: CPUfreqReport => { doubleresponse = freport.cpufreq }
        case load_report: CPUloadReport => {
          loadreport = load_report.cpuloads
          println(loadreport)
          reportcount = reportcount+1
        }
        case "exit" => exit()
      }
      }
    }
  }

  var testee  = new TestActor

  override def beforeAll() {
    ProfileActor.start
    ProfileActor ! UpdateFrequency(1000)
    testee = new TestActor
    testee.start
    testee ! ("test init")
  }

  override def afterAll() {
    testee ! "exit"
    ProfileActor ! "exit"
    Thread.sleep(2000)
  }



  behavior of "A CPU profile Actor"

  it should "return the correct number of CPU's" in {
    cpucount = CPUprofile.getCPUcount
    println(cpucount)
    CPUprofile.getCPUcount should be >= (0)
    val resp1 = ProfileActor ! CPUnumRequest( testee )
    Thread.sleep(2000)
    testee.intresponse should be >= (0)
    println(testee.doubleresponse)
  }

  it should "return the correct CPU frequency" in {
    var cpufreq = CPUprofile.getCPUFrequency
    println(cpufreq)
    CPUprofile.getCPUcount should be >= (0)
    val resp1 = ProfileActor ! CPUfreqRequest( testee )
    Thread.sleep(2000)
    testee.doubleresponse should be >= (0.0)
    println(testee.doubleresponse)
  }

  it should "return the correct CPU load" in {
    var cpuload = CPUprofile.getCPUload
    println(cpuload)
    cpuload.size should be >= (0)
    val resp1 = ProfileActor !? CPUloadRequest( testee )
    Thread.sleep(4000)
    testee.loadreport.size should be >= (0)
    println(testee.loadreport)
    testee.loadreport foreach println
  }

  it should "allow client actors to register for updates" in {
    val testee1  = new TestActor
    val testee2  = new TestActor
    val testee3  = new TestActor
    testee1.start
    testee2.start
    testee3.start

    ProfileActor ! Subscribe(testee1)
    ProfileActor ! Subscribe(testee2)
    ProfileActor ! Subscribe(testee3)

    Thread.sleep(2000)
    println(ProfileActor.observers.size + " clients subscribed ")
    ProfileActor.observers.size should be === 3

    ProfileActor ! Unsubscribe(testee1)
    ProfileActor ! Unsubscribe(testee2)
    ProfileActor ! Unsubscribe(testee3)

    Thread.sleep(2000)
    println(ProfileActor.observers.size + " clients subscribed ")
    ProfileActor.observers.size should be === 0

    testee1 ! "exit"
    testee2 ! "exit"
    testee3 ! "exit"
  }

    it should "push performmance updates to subscribed clients" in {
    val testee1  = new TestActor
    val testee2  = new TestActor
    val testee3  = new TestActor
    testee1.start
    testee2.start
    testee3.start

    ProfileActor ! Subscribe(testee1)
    ProfileActor ! Subscribe(testee2)
    ProfileActor ! Subscribe(testee3)

    Thread.sleep(10000)
    println(testee1.loadreport + " last performance report ")
    testee1.loadreport.size should be === cpucount
    testee1.reportcount should be >= 8
    testee2.reportcount should be >= 8
    testee3.reportcount should be >= 8

    ProfileActor ! Unsubscribe(testee1)
    ProfileActor ! Unsubscribe(testee2)
    ProfileActor ! Unsubscribe(testee3)


    testee1 ! "exit"
    testee2 ! "exit"
    testee3 ! "exit"
  }

}
