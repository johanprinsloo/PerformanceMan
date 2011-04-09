package org.performanceman

import actors.Actor
import actors.Actor._

class TestActor extends Actor {
    var intresponse = 0
    var doubleresponse = 0.0
    //var loadreport:  List[Double] = List.empty

    def act = {
      loop {
      react {
        case numport: CPUnumReport => { intresponse = numport.cpunum }
        case freport: CPUfreqReport => { doubleresponse = freport.cpufreq }
        case load_report: CPUloadReport => reportLoad( load_report.cpuloads )
        case "exit" => exit()
        case m => println("unknown "+m)
      }
      }
    }

   def reportLoad(loads: List[Double]) = {
       println( "load reported @ " + System.currentTimeMillis )
       loads foreach println
   }
  }

object Runner {

  def main(args: Array[String]): Unit = {
    println(CPUprofile.getCPUcount)
    println(CPUprofile.getCPUFrequency)
    val cpuperfr = CPUprofile.getCPUload
    println(cpuperfr)

    cpuperfr match {
      case None => println("no data from CPUprofile.getCPUload ")
      case Some( _ ) => println("some  data from CPUprofile.getCPUload ")
    }

    if (cpuperfr.isDefined) {
      for (cpupr <- cpuperfr.get) {
        println(cpupr)
      }
    }

    val testee = new TestActor
    testee.start

    val resp1 = ProfileActor !? CPUloadRequest( testee )
    Thread.sleep(5000)

    ProfileActor ! UpdateFrequency(1000)

    ProfileActor ! Subscribe( testee )

    Thread.sleep(30000)


    ProfileActor ! "exit"
    testee ! "exit"
    println("done")
  }
}