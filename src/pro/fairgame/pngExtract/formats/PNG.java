// 
// Decompiled by Procyon v0.6.0
// 

package pngExtract.formats;

public class PNG
{
    public static final String format = "PNG";
    public static final byte[] header;
    public static final byte[] tail;
    public static final byte[] ending;
    
    static {
        header = new byte[] { -119, 80, 78, 71 };
        tail = new byte[] { 73, 69, 78, 68, -82, 66 };
        ending = new byte[] { 66, 96, -126, -17, -66, -83, -34 };
    }
    
    public static boolean isPNG(final byte[] input) {
        return input[0] == PNG.header[0] && input[1] == PNG.header[1] && input[2] == PNG.header[2] && input[3] == PNG.header[3];
    }
}
