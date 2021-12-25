package com.example.praktikum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum.R;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.CommentModel;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CommentModel> arrayList  = new ArrayList<>();
    DBHelper databaseHelper;

    //constructor
    public CommentAdapter(Context context, ArrayList<CommentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        databaseHelper = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvUserComment;
        RatingBar rbUserRating;
        public ViewHolder(@NonNull View view) {
            super(view);
            tvUsername = view.findViewById(R.id.username);
            rbUserRating = view.findViewById(R.id.userRating);
            tvUserComment = view.findViewById(R.id.userComment);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //infalate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    //get data, set data, handle view click in method
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvUsername.setText(arrayList.get(position).getUsername());
        holder.rbUserRating.setRating(arrayList.get(position).getRating());
        holder.tvUserComment.setText(arrayList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
