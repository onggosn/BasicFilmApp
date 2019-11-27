package com.pegadaian.filmapps.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pegadaian.filmapps.R;
import com.pegadaian.filmapps.database.FilmDatabase;
import com.pegadaian.filmapps.model.Film;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FilmDescription extends AppCompatActivity {
    ImageView imgView;
    TextView txtName, txtGenre, txtRating, txtDir, txtDesc, txtRuntime, txtRelease;
    FilmDatabase filmDatabase = new FilmDatabase(this);
    String id, filmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_description);
        imgView = findViewById(R.id.imgDesc);
        txtName = findViewById(R.id.txtFilmNameDesc);
        txtGenre = findViewById(R.id.txtGenre);
        txtDir = findViewById(R.id.txtDirector);
        txtRating = findViewById(R.id.txtRating);
        txtRelease = findViewById(R.id.txtRelease);
        txtRuntime = findViewById(R.id.txtRuntime);
        txtDesc = findViewById(R.id.txtFilmDesc);

        Intent intent = getIntent();
        id = intent.getStringExtra("idFilm");
        getPesertaById(id);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnDelete :
                new AlertDialog.Builder(this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Do you want to delete "+ filmName +" film ? ")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filmDatabase.deleteFilm(id);
                        dialog.dismiss();
                        showAlertDelete();
                    }
                }).setNegativeButton("No", null).setIcon(android.R.drawable.ic_delete).show();
                return true;
            case R.id.btnEdit :
                new AlertDialog.Builder(this)
                        .setTitle("Edit Confirmation")
                        .setMessage("Do you want to edit "+ filmName +" film ? ")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentEdit = new Intent(FilmDescription.this, FormInput.class);
                        dialog.dismiss();
                        intentEdit.putExtra("id", id);
                        startActivity(intentEdit);
                        finish();
                    }
                }).setNegativeButton("No", null).setIcon(android.R.drawable.ic_menu_edit).show();
                return true;
        }
        return false;
    }

    public void getPesertaById(String id) {
        Film film = filmDatabase.getFilmById(id);
        Bitmap bm = BitmapFactory.decodeFile(film.getFilmImg());
        imgView.setImageBitmap(bm);
        txtName.setText(film.getFilmName());
        txtGenre.setText(film.getFilmGenre());
        txtDir.setText(film.getFilmDirector());
        txtRating.setText(film.getFilmRating());
        txtRelease.setText(film.getFilmRelease());
        txtRuntime.setText(film.getFilmRuntime());
        txtDesc.setText(film.getFilmDescription());
        txtDesc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        filmName = film.getFilmName();
    }

    public void showAlertDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Success");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(FilmDescription.this, Dashboard.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
