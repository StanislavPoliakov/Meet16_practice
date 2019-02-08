package home.stanislavpoliakov.meet16_practice;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Роль второй активности у меня выполняет фрагмент, который выводит изображение на всю рабочую область
 */
public class LowerFragment extends Fragment implements MainContract.MVPView {
    private ImageView imageView;
    private Presenter presenter = new Presenter();

    public static LowerFragment newInstance() {
        return new LowerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        imageView = view.findViewById(R.id.imageView);
    }

    /**
     * Даже не знаю, стоит ли в дополнение к аннтоции UiThread и методу runOnUiThread писать,
     * что работа по обновлению UI выполняется в UI-потоке и почему возникла такая необходимость :)
     * @param image
     */
    @UiThread
    @Override
    public void showImage(Bitmap image) {
        getActivity().runOnUiThread(() -> {
            imageView.setImageBitmap(image);
        });
    }
}
