package com.example.cocoa_tester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reportes extends AppCompatActivity {

    //  Resultados de humedad alta
    TextView c_buena_h_alta;
    TextView c_media_h_alta;
    TextView c_baja_h_alta;

    //  Resultados de humedad baja
    TextView c_buena_h_baja;
    TextView c_media_h_baja;
    TextView c_baja_h_baja;

    int countCalidadAlta = 0;
    int countCalidadMedia = 0;
    int countCalidadBaja = 0;
    int countCalidadAlta2 = 0;
    int countCalidadMedia2 = 0;
    int countCalidadBaja2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        c_buena_h_alta = findViewById(R.id.txtBuena_Alta);
        c_media_h_alta = findViewById(R.id.txtMedia_Alta);
        c_baja_h_alta = findViewById(R.id.txtBaja_Alta);
        c_buena_h_baja = findViewById(R.id.txtBuena_Baja);
        c_media_h_baja = findViewById(R.id.txtMedia_Baja);
        c_baja_h_baja = findViewById(R.id.txtBaja_Baja);


        AltaHumedadCount();
        BajaHumedadCount();

        c_buena_h_alta.setText(String.valueOf(countCalidadAlta));
        c_media_h_alta.setText(String.valueOf(countCalidadMedia));
        c_baja_h_alta.setText(String.valueOf(countCalidadBaja));
        c_buena_h_baja.setText(String.valueOf(countCalidadAlta2));
        c_media_h_baja.setText(String.valueOf(countCalidadMedia2));
        c_baja_h_baja.setText(String.valueOf(countCalidadBaja2));
    }

    private void AltaHumedadCount() {
        try {
            FileInputStream fis = openFileInput("registros.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.contains("AltaHumedad")) {
                    if (linea.contains("alta")) {
                        countCalidadAlta++;
                        //Log.d("Conteo", "Calidad Alta: " + countCalidadAlta);
                    } else if (linea.contains("media")) {
                        countCalidadMedia++;
                        //Log.d("Conteo", "Calidad Media: " + countCalidadMedia);
                    } else if (linea.contains("baja")) {
                        countCalidadBaja++;
                        //Log.d("Conteo", "Calidad Baja: " + countCalidadBaja);
                    }
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void BajaHumedadCount() {
        try {
            FileInputStream fis = openFileInput("registros2.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.contains("BajaHumedad")) {
                    if (linea.contains("alta")) {
                        countCalidadAlta2++;
                        //Log.d("Conteo", "Calidad Alta: " + countCalidadAlta);
                    } else if (linea.contains("media")) {
                        countCalidadMedia2++;
                        //Log.d("Conteo", "Calidad Media: " + countCalidadMedia);
                    } else if (linea.contains("baja")) {
                        countCalidadBaja2++;
                        //Log.d("Conteo", "Calidad Baja: " + countCalidadBaja);
                    }
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goReporteA(View vista){
        Intent myIntent = new Intent(this, ReporteA.class);
        startActivity(myIntent);
    }

    //public void goReporteB(View vista){
    //    Intent myIntent = new Intent(this, ReporteB.class);
    //    startActivity(myIntent);
    //}
}