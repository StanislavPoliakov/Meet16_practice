package home.stanislavpoliakov.meet16_practice;

import android.graphics.Bitmap;

import java.util.List;

public interface MainContract {

    interface MVVMView {

        void updateView(List<Bitmap> bitmapList);
    }

    interface MVPView {

        void showImage(Bitmap image);
    }

    interface MVPPresenter {

        void showData(Bitmap bitmap);
    }


    interface MVVMViewModel {

        void setData(List<Bitmap> bitmapCollection);

        void keywordSubmitted(String keyword); // Callback
    }

}
