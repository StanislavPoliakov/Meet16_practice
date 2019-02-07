package home.stanislavpoliakov.meet16_practice;

import android.graphics.Bitmap;

public class Presenter implements MainContract.MVPPresenter {
    private MainContract.MVPView mView;
    private Model mModel;

    public Presenter() {
        mModel = Model.getInstance();
        mModel.attachPresenter(this);
    }

    @Override
    public void showData(Bitmap bitmap) {
        mView.showImage(bitmap);
    }

    public void attachView(MainContract.MVPView view) {
        this.mView = view;
    }
}
