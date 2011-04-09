package org.performanceman

import actors.Actor

case class Subscribe( actor: Actor )
case class Unsubscribe( actor: Actor )
case class CPUnumRequest( actor: Actor )
case class CPUfreqRequest( actor: Actor )
case class CPUloadRequest( actor: Actor )
case class UpdateFrequency( frequency: Int )
case class CPUloadReport( cpuloads: List[Double] )
case class CPUfreqReport( cpufreq: Double )
case class CPUnumReport( cpunum: Int )
case class Ping( client: Actor, update: Int )

private case class Pulse()

private object PulseActor extends Actor {
  start
  def act = {
    loop {
      react {
        case ping: Ping => { Thread.sleep(ping.update); ping.client ! Pulse }
        case "exit" => exit()
        case m => println("Unknown message " + m + " to PulseActor - this incident will be reported..")
      }
    }
  }
}

object ProfileActor extends Actor {
  var update = 5000
  var cpucount = 1
  var cpufreq = 0.0
  var cpuload: List[Double] = List.empty
  var observers: Set[Actor] = Set.empty

  start

  def act = {
    PulseActor.start
    pulse
    loop {
      react {
        case sub: Subscribe => subscribe( sub.actor )
        case unsub: Unsubscribe => unsubscribe( unsub.actor )
        case request: CPUnumRequest => { request.actor ! CPUnumReport( cpucount ) ; reply(true) }
        case request: CPUfreqRequest => { request.actor ! CPUfreqReport( cpufreq ) ;reply(true) }
        case request: CPUloadRequest => { request.actor ! CPUloadReport( cpuload ) ; reply(true) }
        case newfreq: UpdateFrequency => { update = newfreq.frequency; reply(true) }
        case Pulse => pulse
        case "exit" => println("ProfileActor exit @"  + System.currentTimeMillis ); exit()
        case m => println("Unknown message " + m + " to ProfileActor - this incident will be reported..")
      }
    }
  }

  def subscribe( actor: Actor ){
    observers += actor
  }

  def unsubscribe( actor: Actor ){
     observers -= actor
  }

  def pulse {
    println("ProfileActor pulse @"  + System.currentTimeMillis + " on thread: " + currentThread.getId() )
    cpucount = CPUprofile.getCPUcount
    cpufreq = CPUprofile.getCPUFrequency
    cpuload = CPUprofile.getCPUload.getOrElse{ List(0.0) }
    observers foreach { observer => observer ! CPUloadReport( cpuload ) }
    PulseActor ! Ping(this, update)
  }

}