// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.components;

import java.util.Arrays;
import java.io.Serializable;

public class Frame implements Serializable
{
    private static final long serialVersionUID = 5563990242673656983L;
    private int fileNameSize;
    private String filename;
    private int numFrameEntries;
    private boolean booleanX;
    private Entry[] frameEntries;
    private String format;
    
    public Frame() {
        this.fileNameSize = 0;
        this.filename = "---";
        this.numFrameEntries = 0;
        this.booleanX = false;
        this.frameEntries = null;
        this.setFormat("CUSTOM");
    }
    
    public Frame(final int fileNameSize, final String filename, final int fileEntries, final boolean booleanX, final Entry[] entries, final String format) {
        this.fileNameSize = fileNameSize;
        this.filename = filename;
        this.numFrameEntries = fileEntries;
        this.booleanX = booleanX;
        this.frameEntries = Arrays.copyOf(entries, entries.length);
        this.setFormat(format);
    }
    
    public static long getSerialversionuid() {
        return 5563990242673656983L;
    }
    
    public int getFileNameSize() {
        return this.fileNameSize;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public int getNumFrameEntries() {
        return this.numFrameEntries;
    }
    
    public boolean getBooleanX() {
        return this.booleanX;
    }
    
    public Entry[] getFrameEntries() {
        return this.frameEntries;
    }
    
    public void setFileNameSize(final int fileNameSize) {
        this.fileNameSize = fileNameSize;
    }
    
    public void setFilename(final String filename) {
        this.filename = filename;
    }
    
    public void setNumFrameEntries(final int fileEntries) {
        this.numFrameEntries = fileEntries;
    }
    
    public void setBooleanX(final boolean booleanX) {
        this.booleanX = booleanX;
    }
    
    public void setFrameEntries(final Entry[] entries) {
        this.frameEntries = entries;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public void setFormat(final String format) {
        this.format = format;
    }
}
