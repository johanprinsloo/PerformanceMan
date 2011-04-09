package org.performanceman;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuPerc;


public class CPUInfo {

    // declare the Sigar class (we need this because it gathers the
    // statistics)
    private Sigar sigar = new Sigar();

    // an array of CPU Info classes. The CPUInfo class is a blank data
    // holder class. Just like string it can hold any data.
    CpuInfo[] cpuInfoList = null;

    public CPUInfo() {

        // the output string is just going to hold our output string
        String output = "";

        // the try catch block means that if we get an error we are notified
        try {
            // get the CPU information from the sigar library
            cpuInfoList = sigar.getCpuInfoList();

            // if something foes wrong
        } catch (SigarException e) {
            // write a description of the problem to the output
            e.printStackTrace();

            // exit the constructor
            return;
        }

        // for each item in the cpu info array
        for (CpuInfo info : cpuInfoList) {
            // add the data to the output ( output += "something" means add
            // "something" to the end of output)
            output += "\nCPU\n";
            output += "Vendor: " + info.getVendor() + "\n";
            output += "Clock: " + info.getMhz() + "Mhz\n";
        }

        // finally, print the data to the output
        System.out.println(output);
    }


    public int getCPUcount() {
        return cpuInfoList.length;
    }


    public double getCPUFrequency() {
        return cpuInfoList[0].getMhz();
    }


    public double[] getCPUload() {
        double[] retval = null;
        try {
            CpuPerc[] perclist = sigar.getCpuPercList();
            int sz = perclist.length;
            retval = new double[sz];
            for (int i = 0; i < sz; i++) {
                retval[i] = perclist[i].getCombined();
            }
        } catch( Exception ex ){ ex.printStackTrace(); }

        return retval;
    }


    public static void main(String[] args) {
        CPUInfo main = new CPUInfo();
    }
}
