package j3.footpon.model;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class ImageCache {
    private ImageCache() {
        
    }
    private static Map<String,Bitmap> imageMap = new HashMap<String,Bitmap>();
    public static Bitmap getBitmap(String id) throws Exception {
        Bitmap bitmap = imageMap.get(id);
        if (bitmap == null) {
            throw new Exception("There is no image named " + id + ".");
        }
        return imageMap.get(id);
    }
    public static void setBitmap(String id, Bitmap bitmap) throws Exception {
        if (imageMap.containsKey(id)) {
            throw new Exception("Duplicate image named  " + id + ".");
        }
        imageMap.put(id, bitmap);
    }
}
