package com.example.cocoa_tester;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class RegistroCacaoAdapter extends RecyclerView.Adapter<RegistroCacaoAdapter.RegistroViewHolder> {

    private List<RegistroCacao> registros;
    private Context context;

    public RegistroCacaoAdapter(Context context, List<RegistroCacao> registros) {
        this.context = context;
        this.registros = registros;
    }

    @NonNull
    @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro, parent, false);
        return new RegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
        RegistroCacao registro = registros.get(position);

        holder.txtCalidad.setText("Calidad: " + registro.getCalidad());
        holder.txtFecha.setText("Fecha: " + registro.getFecha());
        holder.txtHora.setText("Hora: " + registro.getHora());
        holder.txtPorcentaje.setText("Porcentaje: " +registro.getPorcentaje());


        holder.btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imagePath = registro.getImagenPath();
                String archivo_imagen = new File(context.getFilesDir(), imagePath).getAbsolutePath();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_imagen);

                ImageView imagen = dialog.findViewById(R.id.imagenDialog);

                Glide.with(context).load(archivo_imagen).into(imagen);

                Button btnCerrarDialog = dialog.findViewById(R.id.btnCerrarDialog);
                btnCerrarDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    public static class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView txtCalidad;
        TextView txtFecha;
        TextView txtHora;
        TextView txtPorcentaje;
        Button btnImagen;

        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCalidad = itemView.findViewById(R.id.txtCalidad);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtPorcentaje = itemView.findViewById(R.id.txtPct);
            btnImagen = itemView.findViewById(R.id.btnImagen);
        }
    }

}
