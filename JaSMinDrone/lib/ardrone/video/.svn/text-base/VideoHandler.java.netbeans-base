/*
 * Copyright (c) <2011>, <Shigeo Yoshida> modified by <Jeremie Hoelter>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 The names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lib.ardrone.video;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Calendar;

import parrot.video.ReadRawFileImage;

import lib.ardrone.listeners.ImageListener;
import lib.ardrone.network.SocketManagerVideo;
import lib.ardrone.ARDroneEntity;
import lib.ardrone.Global;

public class VideoHandler extends Thread {

    private boolean kill = false;
    private ARDroneEntity drone = null;
    private SocketManagerVideo socketManagerVideo = null;
    private ReadRawFileImage rrfi = null;
    private ImageListener imageListener;
    private long time1, time2;
    public double timediff, fps = 0;

    public VideoHandler(ARDroneEntity drone) {
        this.drone = drone;
        rrfi = new ReadRawFileImage();
        initialize();
    }

    private void initialize() {
        socketManagerVideo = SocketManagerVideo.getInstance(drone);
    }

    public void run() {
        if (!drone.hasPassiveMulticast()) {
            tickleVideoPort();
        }
        time1 = Calendar.getInstance().getTimeInMillis();

        byte[] buf = new byte[153600];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        BufferedImage image = null;

        drone.videoHandlerDone = true;

        while (!kill) {

            try {
                if (!drone.localHost) {
                if (!drone.videoError) {
                    socketManagerVideo.videoSocketReceivePacket(packet);

                    if (!drone.getInetAddress().equals(packet.getAddress())) {
                        //System.out.println("I am video mgr of:"+drone.name+" IP:"+drone.inetaddr+" I received video packet from :"+packet.getAddress());
                    } else {
                        
                        image = rrfi.readUINT_RGBImage(buf);
                        if (imageListener != null) {
                            imageListener.imageUpdated(image);
                        }


                        time2 = Calendar.getInstance().getTimeInMillis();
                        timediff = (double) (time2 - time1) / 1000;
                        if (time2 != time1) {
                            fps = 1 / timediff; //System.out.println("Video FPS: "+ fps);
                            time1 = time2;
                        }

                    }
                }
                }
            } catch (IOException e) {
                drone.videoError = true;

            }

        }
    }

    /**
     * Allows to reinitialiaze the video communication after a connection loss
     *
     * @author Yannick Presse
     */
    public void reinitVideoConnect() {
        tickleVideoPort();

    }

    private void tickleVideoPort() {
        byte multi;

        if (drone.hasMulticast()) {
            multi = 0x02;  //multicast
            System.out.println("Video Multicast");
        } else {
            multi = 0x01;  //unicast
            System.out.println("Video Unicast");
        }
        byte[] buf = {multi, 0x00, 0x00, 0x00};
        DatagramPacket packet = new DatagramPacket(buf, 4, drone.getInetAddress(), Global.VIDEO_PORT);
        socketManagerVideo.videoSocketSendPacket(packet);
    }

    public void kill() {
        kill = true;
    }

    public void setImageListener(ImageListener imageListener) {
        this.imageListener = imageListener;
    }

    public void removeImageListener() {
        this.imageListener = null;
    }
}