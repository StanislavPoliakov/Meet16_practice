package home.stanislavpoliakov.meet16_practice;

import android.graphics.Bitmap;

import java.util.List;

class BuisnessData {
    private List<String> bitmapDescriptions;
    private List<Bitmap> bitmapCollection;

    public List<String> getBitmapDescriptions() {
        return bitmapDescriptions;
    }

    public void setBitmapDescriptions(List<String> bitmapDescriptions) {
        this.bitmapDescriptions = bitmapDescriptions;
    }

    public List<Bitmap> getBitmapCollection() {
        return bitmapCollection;
    }

    public void setBitmapCollection(List<Bitmap> bitmapCollection) {
        this.bitmapCollection = bitmapCollection;
    }
}
