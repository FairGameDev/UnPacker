// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import model.DDSFile;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import pngExtract.components.Entry;
import java.io.File;
import pngExtract.components.Frame;
import java.util.zip.DataFormatException;
import java.io.IOException;
import pngExtract.components.Packet;
import java.io.InputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;

public class Split
{
    private static final boolean info = false;
    private static String saveLocation;
    
    static {
        Split.saveLocation = null;
    }
    
    public static void split(final String inputFileName) throws IOException, DataFormatException {
        final FileInputStream fis = new FileInputStream(inputFileName);
        final XMLDecoder decoder = new XMLDecoder(fis);
        final Packet pack = (Packet)decoder.readObject();
        decoder.close();
        Split.saveLocation = inputFileName.substring(0, inputFileName.lastIndexOf(92));
        for (int i = 0; i < pack.getNumImages(); ++i) {
            saveFrame(pack.getFrames()[i]);
        }
        Split.saveLocation = null;
    }
    
    private static void saveFrame(final Frame frame) throws IOException, DataFormatException {
        if (frame != null) {
            new File(String.valueOf(Split.saveLocation) + "/" + frame.getFilename()).mkdirs();
            int i = 0;
            while (i < frame.getNumFrameEntries()) {
                if (frame.getFormat().equals("PNG")) {
                    savePNGEntry(frame.getFrameEntries()[i], frame.getFilename());
                    ++i;
                }
                else {
                    if (frame.getFormat().equals("DDS")) {
                        throw new DataFormatException("DDS format is not supported.");
                    }
                    throw new DataFormatException("format is not supported.");
                }
            }
        }
    }
    
    private static boolean savePNGEntry(final Entry entry, final String folder) throws IOException {
        printEntryInfo(entry);
        final BufferedImage image = ImageIO.read(new File(String.valueOf(Split.saveLocation) + "\\" + folder + "." + "PNG".toLowerCase()));
        final BufferedImage subImage = image.getSubimage(entry.getXCoord(), entry.getYCoord(), entry.getWidth(), entry.getHeight());
        final File outputfile = new File(String.valueOf(Split.saveLocation) + "/" + folder + "/" + entry.getName() + "." + "PNG".toLowerCase());
        ImageIO.write(subImage, "PNG".toLowerCase(), outputfile);
        return true;
    }
    
    private static boolean saveDDSEntry(final Entry entry, final String folder) throws IOException, DataFormatException {
        printEntryInfo(entry);
        final DDSFile ddsFile = new DDSFile(new File(String.valueOf(Split.saveLocation) + "\\" + folder + "." + "DDS".toLowerCase()));
        ddsFile.loadImageData();
        final BufferedImage entryImage = ddsFile.getData();
        final BufferedImage subImage = entryImage.getSubimage(entry.getXCoord(), entry.getYCoord(), entry.getWidth(), entry.getHeight());
        final File outputfile = new File(String.valueOf(Split.saveLocation) + "/" + folder + "/" + entry.getName() + "." + "PNG".toLowerCase());
        ImageIO.write(subImage, "PNG".toLowerCase(), outputfile);
        return true;
    }
    
    private static void printEntryInfo(final Entry entry) {
        printInfoString(String.format("  write entryName length: %d\n", entry.getNameSize()));
        printInfoString(String.format("  write entryname: \"%s\"\n", entry.getName()));
        printInfoString(String.format("  write entryXcoord: %d\n", entry.getXCoord()));
        printInfoString(String.format("  write entryYcoord: %d\n", entry.getYCoord()));
        printInfoString(String.format("  write entryHeight: %d\n", entry.getHeight()));
        printInfoString(String.format("  write entryWidth: %d\n", entry.getWidth()));
        printInfoString(String.format("  write entryXoffset: %d\n", entry.getXOffset()));
        printInfoString(String.format("  write entryYoffset: %d\n", entry.getYOffset()));
        printInfoString(String.format("  write entryActualHeight: %d\n", entry.getActualHeight()));
        printInfoString(String.format("  write entryActualWidth: %d\n\n", entry.getActualWidth()));
    }
    
    private static void printInfoString(final String outputString) {
    }
}
