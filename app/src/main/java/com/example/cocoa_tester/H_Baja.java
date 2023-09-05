package com.example.cocoa_tester;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.example.cocoa_tester.ml.HWmodel;
import com.example.cocoa_tester.ml.LWmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class H_Baja extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 300;

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

    public void goConsejoBaja (View vista){
        Intent myIntent = new Intent(this, ConsejosBaja.class);
        startActivity(myIntent);
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
                classifyImage(image);

                String calidad = result.getText().toString();
                calidad = calidad.replace("Calidad ", "");

                String humedad = "BajaHumedad";
                String hora = obtenerHoraActual();
                String nombreImagen = guardarImagen(image);
                guardarRegistroEnArchivo(calidad, humedad, hora, nombreImagen);
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
                            classifyImage(image);

                            String calidad = result.getText().toString();
                            calidad = calidad.replace("Calidad ", "");

                            String humedad = "BajaHumedad";
                            String hora = obtenerHoraActual();
                            String nombreImagen = guardarImagen(image);
                            guardarRegistroEnArchivo(calidad, humedad, hora, nombreImagen);

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

    public void classifyImage(Bitmap image)
    {
        try {
            LWmodel model = LWmodel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int [imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for(int i = 0; i < imageSize; i++)
            {
                for(int j = 0 ; j < imageSize; j++)
                {
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            LWmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Calidad alta", "Calidad media", "Calidad baja"};
            System.out.printf(classes[maxPos]);
            result.setText(classes[maxPos]);
            if (classes[maxPos] == "Calidad alta"){
                result.setTextColor(Color.GREEN);
            } else if (classes[maxPos] == "Calidad media") {
                result.setTextColor(Color.YELLOW);
            } else {
                result.setTextColor(Color.RED);
            }

            // Releases model resources if no longer used.
            //model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    private String obtenerHoraActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String guardarImagen(Bitmap imagen) {
        String nombreArchivo = "imagen_" + System.currentTimeMillis() + ".jpg";
        try {
            FileOutputStream fos = openFileOutput(nombreArchivo, Context.MODE_PRIVATE);
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarRegistroEnArchivo(String calidad, String humedad, String hora, String nombreImagen) {
        try {
            File archivo = new File(getFilesDir(), "registros2.txt");

            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            FileInputStream fis = openFileInput("registros2.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            // Verificar si el registro ya existe en el archivo
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(" - ");
                if (campos.length >= 4) {
                    String calidadExistente = campos[0];
                    String humedadExistente = campos[1];
                    String horaExistente = campos[2];
                    String nombreImagenExistente = campos[3];

                    if (calidad.equals(calidadExistente) && humedad.equals(humedadExistente) &&
                            hora.equals(horaExistente) && nombreImagen.equals(nombreImagenExistente)) {
                        // Registro duplicado, no se guarda nuevamente
                        br.close();
                        isr.close();
                        fis.close();
                        return;
                    }
                }
            }

            // Si el registro no existe, agregarlo al archivo
            br.close();
            isr.close();
            fis.close();

            FileOutputStream fos = openFileOutput("registros2.txt", MODE_APPEND);
            String registro = calidad + " - " + humedad + " - " + hora + " - " + nombreImagen + "\n";
            fos.write(registro.getBytes());
            fos.close();

            //  Borrar cuando sea nacesario
            imprimirRegistros();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  NO TOCAR!!! ESTO ES PARA VERIFICAR EL ALMACENAMIENTO :)
    private void imprimirRegistros() {
        try {
            FileInputStream fis = openFileInput("registros2.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            while ((linea = br.readLine()) != null) {
                Log.d("Registros", linea);
            }

            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}