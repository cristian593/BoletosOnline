package com.boletosonline;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoletosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView txtBoletoNombre, txtBoletoRuta, txtBoletoAutobus, txtBoletoHorario, txtBoletoLugares;

    private ItemClickListener itemClickListener;

    public BoletosViewHolder(@NonNull View itemView){
        super(itemView);

        txtBoletoNombre = itemView.findViewById(R.id.boleto_nombre);
        txtBoletoRuta = itemView.findViewById(R.id.boleto_ruta);
        txtBoletoAutobus = itemView.findViewById(R.id.boleto_autobus);
        txtBoletoHorario = itemView.findViewById(R.id.boleto_horario);
        txtBoletoLugares = itemView.findViewById(R.id.boleto_lugares);


    }
    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
