package com.example.postsfeedapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postsfeedapp.R;
import com.example.postsfeedapp.models.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PostModel> data;

    public MyAdapter(Context context, ArrayList<PostModel> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.tvName.setText(data.get(position).getName());
        holder.tvComment.setText(data.get(position).getComment());
        Picasso.with(context)
                .load(data.get(position).getPhotoURL())
                .placeholder(R.drawable.ic_image_holder)
                .error(R.drawable.ic_broken_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvComment;
        AppCompatImageView imageView;

        public MyHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            tvComment = view.findViewById(R.id.tv_comment);
            imageView = view.findViewById(R.id.img);

        }
    }
}
