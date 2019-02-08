package home.stanislavpoliakov.meet16_practice;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

/**
 * Класс отображения View в MVVM
 */
public class UpperFragment extends Fragment implements Callback{
    private MyViewModel viewModel;
    private MyAdapter mAdapter;
    private RecyclerView recyclerView;
    private EditText searchEdit;

    public static UpperFragment newInstance() {
        return new UpperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * Метод инициализация компонентов
     * @param view, которая содержит компоненты
     */
    private void init(View view) {

        // Приявзываем ViewModel
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEdit = view.findViewById(R.id.searchEdit);

        // Обрабатываем окончание ввода (Enter)
        searchEdit.setOnKeyListener(((v, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER) {
                viewModel.keywordSubmitted(((EditText) v).getText().toString());

                // Отображаем изменения, если данные поменялись
                Observer<List<DownloadedPicture>> observer = this::updateView;
                viewModel.bitmapCollection.observe(this, observer);
                return true;
            }
            return false;
        }));
    }

    @UiThread
    private void updateView(List<DownloadedPicture> pictureList) {
        getActivity().runOnUiThread(() -> {
            if (mAdapter == null) initRecyclerView(pictureList);
            else updateRecyclerView(pictureList);
        });
    }

    private void initRecyclerView(List<DownloadedPicture> data) {
        mAdapter = new MyAdapter(this, data);
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void updateRecyclerView(List<DownloadedPicture> data) {
        mAdapter.setData(data);
    }

    @Override
    public void holderClicked(int itemPosition) {
        viewModel.itemClicked(itemPosition);
    }
}
