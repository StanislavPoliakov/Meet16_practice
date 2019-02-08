package home.stanislavpoliakov.meet16_practice;

import android.graphics.Bitmap;

/**
 * MVP-Presenter
 */
public class Presenter implements MainContract.MVPPresenter {
    private MainContract.MVPView mView;
    private Model mModel;

    public Presenter() {
        mModel = Model.getInstance();
        mModel.attachPresenter(this);
    }

    /**
     * Обрабатываем запрос из Модели на отрисовку картинки в нижнем фрагменте
     * @param bitmap картинка, которую необходимо отрисовать
     */
    @Override
    public void showData(Bitmap bitmap) {
        mView.showImage(bitmap);
    }

    /**
     * Привязываем отображение к презентору
     * @param view отображение (нижний фрагмент)
     */
    public void attachView(MainContract.MVPView view) {
        this.mView = view;
    }
}
