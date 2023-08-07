package com.example.cocoa_tester;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class H_Baja extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbaja);

        camera = findViewById(R.id.btnFoto);
        gallery = findViewById(R.id.btnGaleria);
        result = findViewById(R.id.txtResultado);
        imageView = findViewById(R.id.imgResultado);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                // Código para procesar la imagen tomada con la cámara
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                //classifyImage(image);
            } else if (requestCode == 1) {
                // Código para procesar la imagen seleccionada desde la galería
                if (data != null && data.getData() != null) {
                    Uri dat = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(dat);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);

                        if (image != null) {
                            int dimension = Math.min(image.getWidth(), image.getHeight());
                            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                            imageView.setImageBitmap(thumbnail);

                            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                            //classifyImage(image);

                        } else {
                            Log.e("H_Alta", "Error al obtener la imagen desde la galería.");
                        }

                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("H_Alta", "Error al obtener la imagen desde la galería.");
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}