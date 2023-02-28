// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.components;

import java.util.Arrays;

public class Packet
{
    private String packetName;
    private int numImages;
    private Frame[] frames;
    
    public Packet() {
        this.packetName = null;
        this.numImages = 0;
        this.frames = null;
    }
    
    public Packet(final String packetName, final int numImages, final Frame[] frames) {
        this.packetName = packetName;
        this.numImages = numImages;
        this.frames = Arrays.copyOf(frames, frames.length);
    }
    
    public String getPacketName() {
        return this.packetName;
    }
    
    public int getNumImages() {
        return this.numImages;
    }
    
    public Frame[] getFrames() {
        return this.frames;
    }
    
    public void setPacketName(final String packetName) {
        this.packetName = packetName;
    }
    
    public void setNumImages(final int numImages) {
        this.numImages = numImages;
    }
    
    public void setFrames(final Frame[] frames) {
        this.frames = frames;
    }
}
