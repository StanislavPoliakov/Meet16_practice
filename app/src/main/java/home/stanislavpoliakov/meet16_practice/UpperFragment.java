package home.stanislavpoliakov.meet16_practice;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class UpperFragment extends Fragment implements MainContract.MVVMView, Callback{
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        viewModel.attachView(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEdit = view.findViewById(R.id.searchEdit);

        searchEdit.setOnKeyListener(((v, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER) {
                viewModel.keywordSubmitted(((EditText) v).getText().toString());

                Observer<List<Bitmap>> observer = this::updateView;
                //viewModel.getDataFromModel(((EditText) v).getText().toString()).observe(this, observer);
                viewModel.bitmapCollection.observe(this, observer);
                v.clearFocus();
                return true;
            }
            return false;
        }));
    }

    @Override
    public void updateView(List<Bitmap> bitmapList) {
        getActivity().runOnUiThread(() -> {
            if (mAdapter == null) initRecyclerView(bitmapList);
            else updateRecyclerView(bitmapList);
        });
    }

    private void initRecyclerView(List<Bitmap> data) {
        mAdapter = new MyAdapter(this, data);
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void updateRecyclerView(List<Bitmap> data) {
        mAdapter.setData(data);
    }

    @Override
    public void holderClicked(int itemPosition) {
        viewModel.itemClicked(itemPosition);
    }
}
