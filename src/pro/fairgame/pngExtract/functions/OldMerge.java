// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.RenderedImage;
import pngExtract.components.Entry;
import javax.imageio.ImageIO;
import java.io.IOException;
import pngExtract.components.Frame;
import java.io.FileFilter;
import java.awt.image.BufferedImage;
import pngExtract.components.Packet;
import java.io.File;

public class OldMerge
{
    private static final boolean info = false;
    private static String loadLocation;
    private static File[] dirsToMerge;
    private static File[] filesToMerge;
    private static Packet packet;
    private static BufferedImage frameImage;
    private static int nextFreeXCoord;
    private static int nextFreeYCoord;
    private static int combinedHeight;
    private static int combinedWidth;
    
    static {
        OldMerge.loadLocation = null;
    }
    
    public static void merge(final String inputFolderName) throws IOException {
        OldMerge.loadLocation = inputFolderName;
        final File location = new File(OldMerge.loadLocation);
        OldMerge.dirsToMerge = location.listFiles(new FileChecker("dir"));
        (OldMerge.packet = new Packet()).setPacketName(OldMerge.loadLocation);
        OldMerge.packet.setNumImages(OldMerge.dirsToMerge.length);
        final Frame[] frames = new Frame[OldMerge.packet.getNumImages()];
        for (int i = 0; i < OldMerge.packet.getNumImages(); ++i) {
            frames[i] = loadFrame(OldMerge.dirsToMerge[i]);
        }
    }
    
    private static Frame loadFrame(final File frameDir) throws IOException {
        final Frame frame = new Frame();
        frame.setFilename(frameDir.getName());
        frame.setFileNameSize(frameDir.getName().length());
        OldMerge.filesToMerge = frameDir.listFiles(new FileChecker("png"));
        frame.setNumFrameEntries(OldMerge.filesToMerge.length);
        OldMerge.combinedWidth = 0;
        OldMerge.combinedHeight = ImageIO.read(OldMerge.filesToMerge[0]).getHeight();
        final Entry[] entries = new Entry[frame.getNumFrameEntries()];
        OldMerge.frameImage = new BufferedImage(1, 1, 3);
        OldMerge.nextFreeXCoord = 0;
        OldMerge.nextFreeYCoord = 0;
        for (int i = 0; i < frame.getNumFrameEntries(); ++i) {
            entries[i] = loadEntry(OldMerge.filesToMerge[i]);
        }
        frame.setFrameEntries(entries);
        frame.setBooleanX(true);
        ImageIO.write(OldMerge.frameImage, "PNG", new File(frameDir.getParentFile(), String.valueOf(frameDir.getName()) + ".png"));
        return frame;
    }
    
    private static Entry loadEntry(final File entryFile) throws IOException {
        final Entry entry = new Entry();
        final BufferedImage entryImage = ImageIO.read(entryFile);
        int currentFreeXCoord = OldMerge.nextFreeXCoord;
        int currentFreeYCoord = OldMerge.nextFreeYCoord;
        if (OldMerge.frameImage.getWidth() + entryImage.getWidth() < 1000) {
            OldMerge.combinedWidth = OldMerge.frameImage.getWidth() + entryImage.getWidth();
        }
        else if (OldMerge.nextFreeXCoord + entryImage.getWidth() > OldMerge.combinedWidth) {
            OldMerge.combinedHeight = OldMerge.frameImage.getHeight() + entryImage.getHeight();
        }
        if (currentFreeXCoord + entryImage.getWidth() > OldMerge.combinedWidth) {
            currentFreeXCoord = 0;
            currentFreeYCoord = OldMerge.frameImage.getHeight();
            OldMerge.nextFreeXCoord = 0;
            OldMerge.nextFreeYCoord = OldMerge.frameImage.getHeight();
        }
        if (OldMerge.nextFreeXCoord + entryImage.getWidth() <= OldMerge.combinedWidth) {
            OldMerge.nextFreeXCoord += entryImage.getWidth();
        }
        else {
            OldMerge.nextFreeXCoord = 0;
            OldMerge.nextFreeYCoord += entryImage.getHeight();
        }
        final BufferedImage combined = new BufferedImage(OldMerge.combinedWidth, OldMerge.combinedHeight, 3);
        final Graphics g = combined.getGraphics();
        g.drawImage(OldMerge.frameImage, 0, 0, null);
        g.drawImage(entryImage, currentFreeXCoord, currentFreeYCoord, null);
        OldMerge.frameImage = combined;
        entry.setNameSize(entryFile.getName().length());
        printInfoString(String.format("  loaded entryName length: %d\n", entry.getNameSize()));
        entry.setName(entryFile.getName());
        printInfoString(String.format("  loaded entryname: \"%s\"\n", entry.getName()));
        entry.setXCoord(OldMerge.nextFreeXCoord);
        printInfoString(String.format("  loaded entryXcoord: %d\n", entry.getXCoord()));
        entry.setYCoord(OldMerge.nextFreeYCoord);
        printInfoString(String.format("  loaded entryYcoord: %d\n", entry.getYCoord()));
        entry.setHeight(entryImage.getHeight());
        printInfoString(String.format("  loaded entryHeight: %d\n", entry.getHeight()));
        entry.setWidth(entryImage.getWidth());
        printInfoString(String.format("  loaded entryWidth: %d\n", entry.getWidth()));
        entry.setXOffset(0);
        printInfoString(String.format("  loaded entryXoffset: %d\n", entry.getXOffset()));
        entry.setYOffset(0);
        printInfoString(String.format("  loaded entryYoffset: %d\n", entry.getYOffset()));
        entry.setActualHeight(entryImage.getHeight());
        printInfoString(String.format("  loaded entryActualHeight: %d\n", entry.getActualHeight()));
        entry.setActualWidth(entryImage.getWidth());
        printInfoString(String.format("  loaded entryActualWidth: %d\n\n", entry.getActualWidth()));
        return entry;
    }
    
    private static void printInfoString(final String outputString) {
    }
    
    private static class FileChecker implements FileFilter
    {
        private static String option;
        
        FileChecker(final String option) {
            FileChecker.option = option;
        }
        
        @Override
        public boolean accept(final File file) {
            final String option;
            switch (option = FileChecker.option) {
                case "dir": {
                    return file.isDirectory();
                }
                case "png": {
                    return file.getName().toLowerCase().endsWith(".png");
                }
                default:
                    break;
            }
            return false;
        }
    }
}
