package home.stanislavpoliakov.meet16_practice;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.Bitmap;

@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageBitmap") })
public class DownloadedPicture extends BaseObservable{
    public String description;

    @Bindable
    public Bitmap bitmap;

    /*public DownloadedPicture(String description, Bitmap bitmap) {
        this.description = description;
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.downloadedPicture);
    }*/

    /*@Bindable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.bitmap);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;*/

}
