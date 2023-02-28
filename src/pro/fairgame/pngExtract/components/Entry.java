// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.components;

import java.io.Serializable;

public class Entry implements Serializable
{
    private static final long serialVersionUID = 5248883186833906057L;
    private int nameSize;
    private String name;
    private int xCoord;
    private int yCoord;
    private int height;
    private int width;
    private int xOffset;
    private int yOffset;
    private int actualHeight;
    private int actualWidth;
    
    public Entry() {
        this.nameSize = 0;
        this.name = "---";
        this.xCoord = 0;
        this.yCoord = 0;
        this.height = 0;
        this.width = 0;
        this.xOffset = 0;
        this.yOffset = 0;
        this.actualHeight = 0;
        this.actualWidth = 0;
    }
    
    public Entry(final int nameLength, final String name, final int xCoord, final int yCoord, final int height, final int width, final int xOffset, final int yOffset, final int actualHeight, final int actualWidth) {
        this.nameSize = nameLength;
        this.name = name;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.height = height;
        this.width = width;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.actualHeight = actualHeight;
        this.actualWidth = actualWidth;
    }
    
    public static long getSerialversionuid() {
        return 5248883186833906057L;
    }
    
    public int getNameSize() {
        return this.nameSize;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getXCoord() {
        return this.xCoord;
    }
    
    public int getYCoord() {
        return this.yCoord;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getXOffset() {
        return this.xOffset;
    }
    
    public int getYOffset() {
        return this.yOffset;
    }
    
    public int getActualHeight() {
        return this.actualHeight;
    }
    
    public int getActualWidth() {
        return this.actualWidth;
    }
    
    public void setNameSize(final int nameSize) {
        this.nameSize = nameSize;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setXCoord(final int xCoord) {
        this.xCoord = xCoord;
    }
    
    public void setYCoord(final int yCoord) {
        this.yCoord = yCoord;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public void setXOffset(final int xOffset) {
        this.xOffset = xOffset;
    }
    
    public void setYOffset(final int yOffset) {
        this.yOffset = yOffset;
    }
    
    public void setActualHeight(final int actualHeight) {
        this.actualHeight = actualHeight;
    }
    
    public void setActualWidth(final int actualWidth) {
        this.actualWidth = actualWidth;
    }
}
