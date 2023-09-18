package com.example.cocoa_tester;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReporteB extends AppCompatActivity {
    private List<RegistroCacao> registros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        List<RegistroCacao> reversedRegistros = RegistrosInversos();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRegistros);
        RegistroCacaoAdapter adapter = new RegistroCacaoAdapter(this, reversedRegistros);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<RegistroCacao> RegistrosInversos() {
        List<RegistroCacao> reversedRegistros = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("registros2.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            String[] fechahora_split;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" - ");
                if (partes.length >= 5) {
                    String calidad = partes[0];
                    fechahora_split = partes[2].split(" ");
                    String fecha = fechahora_split[0];
                    String hora = fechahora_split[1];
                    String imagenPath = partes[3];
                    String porcentaje = partes[4];

                    RegistroCacao registro = new RegistroCacao(calidad, fecha, hora, imagenPath, porcentaje);
                    reversedRegistros.add(registro);
                }
            }

            br.close();
            isr.close();
            fis.close();

            Collections.reverse(reversedRegistros);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reversedRegistros;
    }
}
