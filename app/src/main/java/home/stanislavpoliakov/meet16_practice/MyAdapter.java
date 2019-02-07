package home.stanislavpoliakov.meet16_practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Bitmap> data;
    private Callback mFragment;

    public MyAdapter(Callback fragment, List<Bitmap> data) {
        this.mFragment = fragment;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.holderImage.setImageBitmap(data.get(position));
    }

    public void setData(List<Bitmap> data) {
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

        public MyViewHolder(View itemView) {
            super(itemView);
            holderImage = itemView.findViewById(R.id.holderImage);

            itemView.setOnClickListener((v) -> {
                mFragment.holderClicked(getAdapterPosition());
            });
        }
    }
}
