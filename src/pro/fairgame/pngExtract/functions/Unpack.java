// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.functions;

import java.io.FileNotFoundException;
import pngExtract.formats.DDS;
import pngExtract.formats.PNG;
import pngExtract.components.Entry;
import pngExtract.components.Frame;
import java.io.IOException;
import pngExtract.components.Packet;
import java.beans.XMLEncoder;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class Unpack
{
    private static final boolean info = false;
    private static final boolean debug = false;
    private static final int bufferSize = 1024;
    private static File inputFile;
    private static String saveLocation;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static long bytesRead;
    private static String format;
    private static boolean EOF_reached;
    
    static {
        Unpack.inputFile = null;
        Unpack.saveLocation = null;
        Unpack.fis = null;
        Unpack.fos = null;
        Unpack.bytesRead = 0L;
        Unpack.format = "CUSTOM";
        Unpack.EOF_reached = false;
    }
    
    public static String unpack(final String inputFileName) throws IOException {
        System.out.println("##" + inputFileName);
        Unpack.inputFile = new File(inputFileName);
        Unpack.saveLocation = inputFileName.substring(0, inputFileName.lastIndexOf(".pack"));
        new File(Unpack.saveLocation).mkdirs();
        createFileInputStream(Unpack.inputFile, 0L);
        final Packet pack = readInitialData();
        final String xmlFileName = String.valueOf(Unpack.saveLocation) + "/" + pack.getPacketName() + ".xml";
        final XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(xmlFileName)));
        e.writeObject(pack);
        e.close();
        Unpack.inputFile = null;
        Unpack.saveLocation = null;
        Unpack.fos.close();
        Unpack.fis.close();
        Unpack.bytesRead = 0L;
        Unpack.format = "CUSTOM";
        Unpack.EOF_reached = false;
        return xmlFileName;
    }
    
    static Packet readInitialData() throws IOException {
        final int numImages = readInt();
        printInfoString(String.format("%d images are located in this file.\n", numImages));
        Unpack.bytesRead += 4L;
        final Frame[] frames = new Frame[numImages];
        for (int i = 0; i < numImages && !Unpack.EOF_reached; ++i) {
            frames[i] = readImageDataFrame();
        }
        return new Packet(Unpack.inputFile.getName().split(".pack")[0], numImages, frames);
    }
    
    static Frame readImageDataFrame() throws IOException {
        final int fileNameSize = readInt();
        printInfoString(String.format("\n\nfileName length: %d\n", fileNameSize));
        final byte[] fileNameBytes = new byte[fileNameSize];
        final char[] fileNameChars = new char[fileNameSize];
        Unpack.fis.read(fileNameBytes);
        for (int i = 0; i < fileNameSize; ++i) {
            fileNameChars[i] = (char)fileNameBytes[i];
        }
        final String fileName = new String(fileNameBytes);
        printInfoString(String.format("Name of file: \"%s\"\n", fileName));
        final int fileEntries = readInt();
        printDebugString(String.format("fileEntries: %d\n", fileEntries));
        final boolean booleanX = readInt() != 0;
        printInfoString(String.format("boolean: %b\n", booleanX));
        Unpack.bytesRead += 4 + fileNameSize + 4 + 4;
        final Entry[] entries = new Entry[fileEntries];
        for (int j = 0; j < fileEntries; ++j) {
            printInfoString(String.format("\nEntry%d:\n", j));
            entries[j] = readImageDataEntry();
        }
        readImageData(fileName);
        return new Frame(fileNameSize, fileName, fileEntries, booleanX, entries, Unpack.format);
    }
    
    static Entry readImageDataEntry() throws IOException {
        final int entryNameSize = readInt();
        printInfoString(String.format("  entryName length: %d\n", entryNameSize));
        final byte[] entryNameBytes = new byte[entryNameSize];
        final char[] entryNameChars = new char[entryNameSize];
        Unpack.fis.read(entryNameBytes);
        for (int i = 0; i < entryNameSize; ++i) {
            entryNameChars[i] = (char)entryNameBytes[i];
        }
        final String entryName = new String(entryNameBytes);
        printInfoString(String.format("  entryName: \"%s\"\n", entryName));
        final int entryXcoord = readInt();
        printInfoString(String.format("  entryXcoord: %d\n", entryXcoord));
        final int entryYcoord = readInt();
        printInfoString(String.format("  entryYcoord: %d\n", entryYcoord));
        final int entryWidth = readInt();
        printInfoString(String.format("  entryWidth: %d\n", entryWidth));
        final int entryHeight = readInt();
        printInfoString(String.format("  entryHeight: %d\n", entryHeight));
        final int entryXoffset = readInt();
        printInfoString(String.format("  entryXoffset: %d\n", entryXoffset));
        final int entryYoffset = readInt();
        printInfoString(String.format("  entryYoffset: %d\n", entryYoffset));
        final int entryActualWidth = readInt();
        printInfoString(String.format("  entryActualWidth: %d\n", entryActualWidth));
        final int entryActualHeight = readInt();
        printInfoString(String.format("  entryActualHeight: %d\n", entryActualHeight));
        Unpack.bytesRead += 4 + entryNameSize + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4;
        return new Entry(entryNameSize, entryName, entryXcoord, entryYcoord, entryHeight, entryWidth, entryXoffset, entryYoffset, entryActualHeight, entryActualWidth);
    }
    
    static boolean readImageData(final String fileName) throws IOException {
        final byte[] input = new byte[1024];
        Unpack.format = "CUSTOM";
        int len = 1;
        Unpack.fis.read(input);
        File file;
        if (PNG.isPNG(input)) {
            Unpack.format = "PNG";
            file = new File(String.valueOf(Unpack.saveLocation) + "\\" + fileName + ".png");
        }
        else {
            if (!DDS.isDDS(input)) {
                System.out.println(String.valueOf(Unpack.saveLocation) + "\\" + fileName + ".???");
                throw new FileNotFoundException("No .dds or .png files found.");
            }
            Unpack.format = "DDS";
            file = new File(String.valueOf(Unpack.saveLocation) + "\\" + fileName + ".dds");
        }
        createFileOutputStream(file);
        createFileInputStream(Unpack.inputFile, Unpack.bytesRead);
        while (len > 0) {
            len = Unpack.fis.read(input);
            if (len < 0) {
                printDebugString(String.format("EOF reached\n", new Object[0]));
                return Unpack.EOF_reached = true;
            }
            for (int i = 0; i < len - 5 - 1; ++i) {
                if (input[i] == PNG.tail[0]) {
                    if (input[i + 1] == PNG.tail[1] && input[i + 2] == PNG.tail[2] && input[i + 3] == PNG.tail[3] && input[i + 4] == PNG.tail[4] && input[i + 5] == PNG.tail[5]) {
                        Unpack.fos.write(input, 0, i + 5);
                        Unpack.fis.close();
                        Unpack.bytesRead += i + 5 + 7;
                        createFileInputStream(Unpack.inputFile, Unpack.bytesRead);
                        return true;
                    }
                }
                else if (input[i] == DDS.tail[0] && input[i + 1] == DDS.tail[1] && input[i + 2] == DDS.tail[2] && input[i + 3] == DDS.tail[3] && input[i + 4] == DDS.tail[4] && input[i + 5] == DDS.tail[5] && input[i + 6] == DDS.tail[6] && input[i + 7] == DDS.tail[7] && input[i + 8] == DDS.tail[8]) {
                    Unpack.fos.write(input, 0, i + 9);
                    Unpack.fis.close();
                    Unpack.bytesRead += i + 9;
                    createFileInputStream(Unpack.inputFile, Unpack.bytesRead);
                    return true;
                }
            }
            Unpack.fos.write(input, 0, len);
            Unpack.bytesRead += len;
        }
        return false;
    }
    
    private static void createFileInputStream(final File file, final long bytesToSkip) {
        try {
            (Unpack.fis = new FileInputStream(file)).skip(bytesToSkip);
        }
        catch (final IOException e) {
            System.out.printf("Failed to create FileInputStream for: %s", file.getAbsolutePath());
        }
        printDebugString(String.format("created FileInputStream  for: %s\n", file.getAbsolutePath()));
    }
    
    private static void createFileOutputStream(final File file) {
        try {
            Unpack.fos = new FileOutputStream(file);
        }
        catch (final IOException e) {
            System.out.printf("Failed to create FileOutputStream for: %s", file.getAbsolutePath());
        }
        printDebugString(String.format("created FileOutputStream for: %s\n", file.getAbsolutePath()));
    }
    
    private static int readInt() throws IOException {
        final byte[] numImagesBytes = new byte[4];
        Unpack.fis.read(numImagesBytes, 0, 4);
        return convertByteArrayToInt(numImagesBytes);
    }
    
    private static int convertByteArrayToInt(final byte[] bytes) {
        int val = 0;
        val += (bytes[3] << 24 & 0xFF000000);
        val += (bytes[2] << 16 & 0xFF0000);
        val += (bytes[1] << 8 & 0xFF00);
        val += (bytes[0] & 0xFF);
        printDebugString(String.format("%d converted %x %x %x %x -> %d\n", Unpack.bytesRead, bytes[3], bytes[2], bytes[1], bytes[0], val));
        return val;
    }
    
    private static void printInfoString(final String outputString) {
    }
    
    private static void printDebugString(final String outputString) {
    }
}
