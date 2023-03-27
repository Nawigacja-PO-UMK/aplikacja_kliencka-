package com;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RangingWithRTT
{
    public static void main(String[] args) throws IOException
    {
        InetAddress target = InetAddress.getByName("192.168.*.**");

        int numPackets = 1;
        long total = 0;
        int receivedPackets = 0;

        for(int i = 0; i < numPackets; i++) {
            long time = System.nanoTime();
            boolean received = target.isReachable(3000);
            long back = System.nanoTime();

            if(received)
            {
                long rtt = back - time;
                System.out.println("Packet " + i + " RTT: " + rtt + "ns");
                total += rtt;
                receivedPackets++;
            }
            else
            {
                System.out.println("Packet " + i + " lost");
            }
        }

        if(receivedPackets > 0)
        {
            long averageRtt = total/receivedPackets;
            System.out.println("Average RTT: " + averageRtt + "ns");

            double distance = (averageRtt * 10e9) / 2.0 * 299792458.0;
            System.out.println("Distance from source: (+/-) " + distance + "m");
        }
        else
        {
            System.out.println("No packets received");
        }
    }
}
