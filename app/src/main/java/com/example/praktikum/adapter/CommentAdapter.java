package com.example.praktikum.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.Chord;
import com.example.praktikum.Constant;
import com.example.praktikum.DetailChordActivity;
import com.example.praktikum.MainActivity;
import com.example.praktikum.R;
import com.example.praktikum.TambahChordActivity;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.CommentModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
