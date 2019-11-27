package com.pegadaian.filmapps.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pegadaian.filmapps.R;
import com.pegadaian.filmapps.database.FilmDatabase;
import com.pegadaian.filmapps.model.Film;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormInput extends AppCompatActivity {

    private EditText edtName;
    private EditText edtRating;
    private EditText edtDirector;
    private EditText edtGenre;
    private EditText edtDescription;
    private EditText edtRuntime;
    private TextView dateRelease;
    private TextView urlImage;
    private Button btnImage, btnSave;
    private ImageView viewImg;
    private Calendar calendar = Calendar.getInstance();

    FilmDatabase filmDatabase = new FilmDatabase(this);
    private static int RESULT_LOAD_IMAGE = 1;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_input);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        LinearLayout layoutDate = findViewById(R.id.layoutDate);
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FormInput.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        edtName = findViewById(R.id.edtFilmName);
        edtRating = findViewById(R.id.edtRating);
        edtDirector = findViewById(R.id.edtDirector);
        edtGenre = findViewById(R.id.edtGenre);
        edtDescription = findViewById(R.id.edtDescription);
        edtRuntime = findViewById(R.id.edtRuntime);
        dateRelease = findViewById(R.id.dateRelease);
        viewImg = findViewById(R.id.imgFilm);
        btnImage = findViewById(R.id.uploadImg);
        urlImage = findViewById(R.id.urlImage);
        btnSave = findViewById(R.id.btnSaveFilm);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if (id != null) {
            Film film = filmDatabase.getFilmById(id);
            edtName.setText(film.getFilmName());
            edtDirector.setText(film.getFilmDirector());
            dateRelease.setText(film.getFilmRelease());
            edtRuntime.setText(film.getFilmRuntime());
            edtGenre.setText(film.getFilmGenre());
            edtDescription.setText(film.getFilmDescription());
            edtRating.setText(film.getFilmRating());
            Bitmap bm = BitmapFactory.decodeFile(film.getFilmImg());
            urlImage.setText(film.getFilmImg());
            viewImg.setImageBitmap(bm);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFilm(v);
                }
            });
        } else {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFilm(v);
                }
            });
        }

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void insertFilm(View view) {
        Film film = new Film();
        film.setFilmName(edtName.getText().toString());
        film.setFilmRating(edtRating.getText().toString());
        film.setFilmDirector(edtDirector.getText().toString());
        film.setFilmGenre(edtGenre.getText().toString());
        film.setFilmDescription(edtDescription.getText().toString());
        film.setFilmRuntime(edtRuntime.getText().toString());
        film.setFilmRelease(dateRelease.getText().toString());
        film.setFilmImg(urlImage.getText().toString());
        if (id != null) {
            filmDatabase.updateFilm(film, id);
            showAlertUpdate();
        } else {
            filmDatabase.insertData(film);
            showAlertSuccess();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToNext();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                viewImg.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));
                urlImage.setText(picturePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void showAlertSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Insert Success");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(FormInput.this, Dashboard.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void showAlertUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update Success");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(FormInput.this, FilmDescription.class);
                intent.putExtra("idFilm", id);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateRelease.setText(dateFormat.format(calendar.getTime()));
    }

    /*
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()){
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }*/
}
