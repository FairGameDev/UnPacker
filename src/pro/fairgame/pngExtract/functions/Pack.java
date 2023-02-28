// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import java.io.FileNotFoundException;
import pngExtract.formats.PNG;
import pngExtract.components.Entry;
import pngExtract.components.Frame;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.beans.XMLDecoder;
import pngExtract.components.Packet;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class Pack
{
    private static final boolean info = false;
    static final int bufferSize = 1024;
    private static String saveLocation;
    static FileInputStream fis;
    static FileOutputStream fos;
    static Packet pack;
    
    static {
        Pack.saveLocation = null;
        Pack.fis = null;
        Pack.fos = null;
    }
    
    public static void pack(final String inputFileName) throws IOException {
        final FileInputStream os = new FileInputStream(inputFileName);
        final XMLDecoder decoder = new XMLDecoder(os);
        Pack.pack = (Packet)decoder.readObject();
        decoder.close();
        Pack.saveLocation = inputFileName.substring(0, inputFileName.lastIndexOf(92));
        final File file = new File(String.valueOf(Pack.saveLocation.substring(0, Pack.saveLocation.lastIndexOf(92))) + "\\" + Pack.pack.getPacketName() + ".pack");
        createFileOutputStream(file);
        writeInitialData();
        Pack.saveLocation = null;
        Pack.fos.close();
        Pack.fis.close();
        Pack.pack = null;
    }
    
    static void writeInitialData() throws IOException {
        printInfoString(String.format("write numImages: %d\n", Pack.pack.getNumImages()));
        writeInt(Pack.pack.getNumImages());
        final Frame[] frames = Pack.pack.getFrames();
        for (int i = 0; i < frames.length && frames[i] != null; ++i) {
            writeImageDataFrame(frames[i]);
        }
    }
    
    static void writeImageDataFrame(final Frame frame) throws IOException {
        printInfoString(String.format("\n\nwrite fileName length: %d\n", frame.getFileNameSize()));
        writeInt(frame.getFileNameSize());
        printInfoString(String.format("write filename: \"%s\"\n", frame.getFilename()));
        Pack.fos.write(frame.getFilename().getBytes());
        printInfoString(String.format("write fileEntries: %d\n", frame.getNumFrameEntries()));
        writeInt(frame.getNumFrameEntries());
        printInfoString(String.format("write boolean: %b\n", frame.getBooleanX()));
        if (frame.getBooleanX()) {
            writeInt(1);
        }
        else {
            writeInt(0);
        }
        for (int i = 0; i < frame.getNumFrameEntries(); ++i) {
            printInfoString(String.format("\nEntry%d:\n", i));
            writeImageDataEntry(frame.getFrameEntries()[i]);
        }
        writeImageData(frame.getFilename(), frame.getFormat());
    }
    
    static void writeImageDataEntry(final Entry entry) throws IOException {
        printInfoString(String.format("  write entryName length: %d\n", entry.getNameSize()));
        writeInt(entry.getNameSize());
        printInfoString(String.format("  write entryname: \"%s\"\n", entry.getName()));
        Pack.fos.write(entry.getName().getBytes());
        printInfoString(String.format("  write entryXcoord: %d\n", entry.getXCoord()));
        writeInt(entry.getXCoord());
        printInfoString(String.format("  write entryYcoord: %d\n", entry.getYCoord()));
        writeInt(entry.getYCoord());
        printInfoString(String.format("  write entryWidth: %d\n", entry.getWidth()));
        writeInt(entry.getWidth());
        printInfoString(String.format("  write entryHeight: %d\n", entry.getHeight()));
        writeInt(entry.getHeight());
        printInfoString(String.format("  write entryXoffset: %d\n", entry.getXOffset()));
        writeInt(entry.getXOffset());
        printInfoString(String.format("  write entryYoffset: %d\n", entry.getYOffset()));
        writeInt(entry.getYOffset());
        printInfoString(String.format("  write entryActualWidth: %d\n", entry.getActualWidth()));
        writeInt(entry.getActualWidth());
        printInfoString(String.format("  write entryActualHeight: %d\n", entry.getActualHeight()));
        writeInt(entry.getActualHeight());
    }
    
    static void writeImageData(final String fileName, final String format) throws IOException {
        int len = 1;
        final byte[] input = new byte[1024];
        final File file = new File(String.valueOf(Pack.saveLocation) + "\\" + fileName + "." + format.toLowerCase());
        createFileInputStream(file);
        do {
            len = Pack.fis.read(input);
            Pack.fos.write(input, 0, len);
        } while (len == 1024);
        if (format.equals("PNG")) {
            Pack.fos.write(PNG.ending);
        }
        else if (!format.equals("DDS")) {
            throw new FileNotFoundException("No .dds or .png files to write.");
        }
    }
    
    private static void createFileInputStream(final File file) throws IOException {
        Pack.fis = new FileInputStream(file);
    }
    
    private static void createFileOutputStream(final File file) throws IOException {
        Pack.fos = new FileOutputStream(file);
    }
    
    private static void writeInt(final int value) throws IOException {
        Pack.fos.write(convertIntToByteArray(value), 0, 4);
    }
    
    private static byte[] convertIntToByteArray(final int val) {
        final byte[] buffer = { (byte)val, (byte)(val >>> 8), (byte)(val >>> 16), (byte)(val >>> 24) };
        return buffer;
    }
    
    private static void printInfoString(final String outputString) {
    }
}
