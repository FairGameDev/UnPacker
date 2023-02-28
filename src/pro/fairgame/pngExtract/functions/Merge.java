// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import java.awt.Graphics2D;
import pngExtract.components.Entry;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.util.zip.DataFormatException;
import java.io.IOException;
import pngExtract.components.Frame;
import pngExtract.components.Packet;
import java.io.InputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;

public class Merge
{
    private static final boolean info = false;
    private static String loadLocation;
    private static BufferedImage frameImage;
    private static int combinedHeight;
    private static int combinedWidth;
    
    static {
        Merge.loadLocation = null;
        Merge.frameImage = null;
    }
    
    public static void merge(final String inputFileName) throws IOException, DataFormatException {
        final FileInputStream fis = new FileInputStream(inputFileName);
        final XMLDecoder decoder = new XMLDecoder(fis);
        final Packet pack = (Packet)decoder.readObject();
        decoder.close();
        Merge.loadLocation = inputFileName.substring(0, inputFileName.lastIndexOf(92));
        printInfoString("+++" + Merge.loadLocation);
        final Frame[] frames = pack.getFrames();
        for (int i = 0; i < pack.getNumImages(); ++i) {
            if (frames[i] != null) {
                loadFrame(frames[i], String.valueOf(Merge.loadLocation) + "\\" + frames[i].getFilename());
            }
        }
        Merge.loadLocation = null;
        Merge.frameImage = null;
        Merge.combinedHeight = 0;
        Merge.combinedWidth = 0;
    }
    
    private static void loadFrame(final Frame frame, final String frameFolderPath) throws IOException, DataFormatException {
        final Entry[] entries = frame.getFrameEntries();
        Merge.frameImage = null;
        Merge.combinedWidth = 0;
        Merge.combinedHeight = 0;
        int i = 0;
        while (i < frame.getNumFrameEntries()) {
            if (frame.getFormat().equals("PNG")) {
                loadPNGEntry(entries[i], String.valueOf(frameFolderPath) + "\\" + entries[i].getName() + ".png");
                ++i;
            }
            else {
                if (frame.getFormat().equals("DDS")) {
                    throw new DataFormatException("DDS format is not supported.");
                }
                throw new DataFormatException("format is not supported.");
            }
        }
        if (frame.getFormat().equals("PNG")) {
            final BufferedImage combined = new BufferedImage(Merge.frameImage.getWidth() + 1, Merge.frameImage.getHeight(), Merge.frameImage.getTransparency());
            final Graphics2D g2 = combined.createGraphics();
            if (Merge.frameImage != null) {
                g2.drawImage(Merge.frameImage, 0, 0, null);
            }
            ImageIO.write(combined, "PNG", new File(String.valueOf(frameFolderPath) + "." + frame.getFormat().toLowerCase()));
            return;
        }
        if (frame.getFormat().equals("DDS")) {
            throw new DataFormatException("DDS format is not supported.");
        }
        throw new DataFormatException("format is not supported.");
    }
    
    private static void loadPNGEntry(final Entry entry, final String entryFilePath) throws IOException {
        final BufferedImage entryImage = ImageIO.read(new File(entryFilePath));
        Merge.combinedWidth = Math.max(Merge.combinedWidth, entry.getXCoord() + entry.getWidth());
        Merge.combinedHeight = Math.max(Merge.combinedHeight, entry.getYCoord() + entry.getHeight());
        final BufferedImage combined = new BufferedImage(Merge.combinedWidth, Merge.combinedHeight, entryImage.getTransparency());
        final Graphics2D g2 = combined.createGraphics();
        if (Merge.frameImage != null) {
            g2.drawImage(Merge.frameImage, 0, 0, null);
        }
        g2.drawImage(entryImage, entry.getXCoord(), entry.getYCoord(), null);
        Merge.frameImage = combined;
    }
    
    private static void loadDDSEntry(final Entry entry, final String entryFilePath) throws IOException {
        final BufferedImage entryImage = ImageIO.read(new File(entryFilePath));
        Merge.combinedWidth = Math.max(Merge.combinedWidth, entry.getXCoord() + entry.getWidth());
        Merge.combinedHeight = Math.max(Merge.combinedHeight, entry.getYCoord() + entry.getHeight());
        final BufferedImage combined = new BufferedImage(Merge.combinedWidth, Merge.combinedHeight, 6);
        final Graphics2D g2 = combined.createGraphics();
        if (Merge.frameImage != null) {
            g2.drawImage(Merge.frameImage, 0, 0, null);
        }
        g2.drawImage(entryImage, entry.getXCoord(), entry.getYCoord(), null);
        Merge.frameImage = combined;
    }
    
    private static void printInfoString(final String outputString) {
    }
}
