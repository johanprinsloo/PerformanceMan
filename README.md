Performance Man
===============

A portable performance monitoring library for scala based on [Sigar](http://www.hyperic.com/products/sigar).

Usage
------
###Subscribe
Subscribe your Actor to the performance monitor actor:
Create an Actor that can consume 'CPUloadReport' messages and subscribe it by sending a 'Subscribe' message.
 
``` scala
  class TestActor extends Actor {
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
```

###Asyncronous
Anonymous actor based callback mechanism:

``` scala
    ProfileActor ! CPUnumRequest( testee )
```

###Synchronous

A thin wrapper around the Sigar libs:

``` scala
   val cpucount = CPUprofile.getCPUcount
   val cpufreq = CPUprofile.getCPUFrequency
   val coreloads = CPUprofile.getCPUload
   for (load <- coreloads.get)  {
      println( load )
   }
```



