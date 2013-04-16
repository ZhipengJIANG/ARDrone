package lib.utils;

import java.util.Date;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int intOfFloat(float f) {
        FloatBuffer fb = null;
        IntBuffer ib = null;
        ByteBuffer bb = ByteBuffer.allocate(4);
        fb = bb.asFloatBuffer();
        ib = bb.asIntBuffer();
        fb.put(0, f);
        return ib.get(0);
    }
}
