package home.stanislavpoliakov.meet16_practice;

import android.content.Context;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import home.stanislavpoliakov.meet16_practice.databinding.ViewHolderBinding;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<DownloadedPicture> data;
    private Callback mFragment;

    public MyAdapter(Callback fragment, List<DownloadedPicture> data) {
        this.mFragment = fragment;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderBinding binding = ViewHolderBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.holderImage.setImageBitmap(data.get(position));
        holder.bind(data.get(position));
    }

    public void setData(List<DownloadedPicture> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView holderImage;
        ViewHolderBinding binding;

        public MyViewHolder(ViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener((v) -> {
                mFragment.holderClicked(getAdapterPosition());
            });
        }

        public void bind(DownloadedPicture downloadedPicture){
            binding.setDownloadedPicture(downloadedPicture);
            binding.executePendingBindings();
        }


        /*public MyViewHolder(View itemView) {
            super(itemView);
            holderImage = itemView.findViewById(R.id.holderImage);


        }*/
    }
}
