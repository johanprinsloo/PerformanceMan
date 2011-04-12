Performance Man
===============

A portable performance monitoring library for scala based on [Sigar](http://www.hyperic.com/products/sigar).

Usage
------
###Subscribe
Subscribe your Actor to the performance monitor actor:

 `class TestActor extends Actor {
    var loadreport: List[Double] = List.empty
    var reportcount = 0

    def act = {
      loop {
        react {
          case load_report: CPUloadReport => {
            loadreport = load_report.cpuloads
            reportcount = reportcount + 1
          }
        }
      }
    }
  } 

  val testee1  = new TestActor
 
  testee1.start

  ProfileActor ! Subscribe(testee1)
`

<object src="https://gist.github.com/914709.js?file=PerformanceManSubscribe.scala"></object>

###Asyncronous
Anonymous actor based callback mechanism:

###Synchronous
A thin wrapper around the Sigar libs:
 



