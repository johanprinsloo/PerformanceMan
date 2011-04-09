package org.performanceman

import actors.Actor
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

class CPUprofileActorTest extends FlatSpec with ShouldMatchers with BeforeAndAfterAll {

  class TestActor extends Actor {
    var intresponse = 0
    var doubleresponse = 0.0
    var loadreport:  List[Double] = List.empty

    def act = {
      loop {
      react {
        case numport: CPUnumReport => { intresponse = numport.cpunum }
        case freport: CPUfreqReport => { doubleresponse = freport.cpufreq }
        case load_report: CPUloadReport => {  loadreport = load_report.cpuloads; println(loadreport) }
        case "exit" => exit()
      }
      }
    }
  }

  var testee  = new TestActor

  override def beforeAll() {
    ProfileActor.start
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
    val cpucount = CPUprofile.getCPUcount
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

}
