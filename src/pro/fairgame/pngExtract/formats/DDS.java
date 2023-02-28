// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.formats;

public class DDS
{
    public static final String format = "DDS";
    public static final byte[] header;
    public static final byte[] tail;
    
    static {
        header = new byte[] { 68, 68, 83, 32 };
        tail = new byte[] { 58, -1, -1, -1, -1, -17, -66, -83, -34 };
    }
    
    public static boolean isDDS(final byte[] input) {
        return input[0] == DDS.header[0] && input[1] == DDS.header[1] && input[2] == DDS.header[2] && input[3] == DDS.header[3];
    }
}
