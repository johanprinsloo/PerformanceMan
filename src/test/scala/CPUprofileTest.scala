package org.performanceman


import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


class CPUprofileTest extends FlatSpec with ShouldMatchers {

  var cpucount: Int = 0

  "A CPU profile " should "return a finite number of CPU's" in {
    cpucount = CPUprofile.getCPUcount
    println(cpucount)
    CPUprofile.getCPUcount should be >= (0)
  }

  "A CPU profile " should "return a realistic frequency" in {
    println(CPUprofile.getCPUFrequency)
    CPUprofile.getCPUFrequency should be >= (0.0)
  }

  "A CPU profile " should "return a list of the same size as cpu count" in {
    var coreloads = CPUprofile.getCPUload
    println(coreloads)
    coreloads.get.length should be >= cpucount
  }

  "A CPU profile " should "return realistic load values" in {
    var coreloads = CPUprofile.getCPUload
    println(coreloads)

    for (load <- coreloads.get) {
      load should be >= (0.0)
    }

  }
}
