package com.pegadaian.filmapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pegadaian.filmapps.R;
import com.pegadaian.filmapps.model.Film;
import com.pegadaian.filmapps.view.Dashboard;
import com.pegadaian.filmapps.view.FilmDescription;

import java.io.File;
import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.GridFilmHolder> {

    private ArrayList<Film> listFilm;
    private Context context;

    public FilmAdapter(ArrayList<Film> listFilm, Context context) {
        this.listFilm = listFilm;
        this.context = context;
    }

    @NonNull
    @Override
    public GridFilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv, parent, false);
        return new GridFilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridFilmHolder holder, int position) {
        final Film film = listFilm.get(position);
        holder.txtName.setText(film.getFilmName());
        Bitmap bm = BitmapFactory.decodeFile(film.getFilmImg());
        holder.imageView.setImageBitmap(bm);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FilmDescription.class);
                intent.putExtra("idFilm", film.getKode());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class GridFilmHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imageView;
        CardView cardView;

        GridFilmHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtFilmName);
            imageView = view.findViewById(R.id.imgView);
            cardView = view.findViewById(R.id.gridView);
        }
    }


}
