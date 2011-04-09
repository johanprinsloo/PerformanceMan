package org.performanceman

import org.hyperic.sigar.{CpuPerc, SigarException, CpuInfo, Sigar}

object CPUprofile {
  private var sigar = new Sigar
  private var cpuInfoList: List[CpuInfo] = null

  try {
    cpuInfoList = sigar.getCpuInfoList.view.toList
  }
  catch {
    case e: SigarException => {
      e.printStackTrace
    }
  }

  def getCPUcount: Int = {
    return cpuInfoList.length
  }

  def getCPUFrequency: Double = {
    return cpuInfoList(0).getMhz
  }

  def getCPUload: Option[ List[Double] ] = {
    var percnum: List[Double] = null
    try {
      percnum = sigar.getCpuPercList.view.toList.map{
        i => i.getCombined * 100.0 }
    }
    catch {
      case e: SigarException => { e.printStackTrace }
      return None
    }
    return Some( percnum )
  }

} //object