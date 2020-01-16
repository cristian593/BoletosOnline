package com.boletosonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class CompraBoletoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView fecha;
    private String rutaRecivida,  fechaActual;
    public static String horarioSeleccionado;

    private Button btnHorario1,btnHorario2, btnHorario3;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_boleto);

        fecha = findViewById(R.id.titulo1);
        btnHorario1 = findViewById(R.id.btn_horario1);
        btnHorario2 = findViewById(R.id.btn_horario2);
        btnHorario3 = findViewById(R.id.btn_horario3);

        Bundle datoRecivido = this.getIntent().getExtras();
        rutaRecivida = datoRecivido.getString("RUTA");

        getSupportActionBar().setTitle(rutaRecivida);

        MostrarFecha();
        ColocarHorario();

        btnHorario1.setOnClickListener(this);
        btnHorario2.setOnClickListener(this);
        btnHorario3.setOnClickListener(this);



    }

    private void MostrarFecha() {
        long date = System.currentTimeMillis();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        fechaActual = formatoFecha.format(date);
        fecha.setText(fechaActual);
    }

    private void ColocarHorario() {
        switch (rutaRecivida){

            case "Riobamba-Quito":
                btnHorario1.setText("08:00");
                btnHorario2.setText("12:00");
                btnHorario3.setText("18:00");
                break;
            case "Riobamba-Guayaquil":
                btnHorario1.setText("07:00");
                btnHorario2.setText("15:00");
                btnHorario3.setText("18:00");
                break;
            case "Quito-Riobamba":
                btnHorario1.setText("08:00");
                btnHorario2.setText("12:00"+"");
                btnHorario3.setText("18:00");
                break;
            case "Guayaquil-Riobamba":
                btnHorario1.setText("08:00");
                btnHorario2.setText("12:00");
                btnHorario3.setText("18:00"+"");

                break;
                default:
                    break;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_horario1:
                horarioSeleccionado = fechaActual+"-"+btnHorario1.getText();
                intent = new Intent(CompraBoletoActivity.this, AutobusActivity.class);
                intent.putExtra("HSELECCIONADO", horarioSeleccionado);
                intent.putExtra("RUTA", rutaRecivida);
                startActivity(intent);

                break;
            case R.id.btn_horario2:
                horarioSeleccionado = fechaActual+"-"+btnHorario2.getText().toString();
                intent = new Intent(CompraBoletoActivity.this, AutobusActivity.class);
                intent.putExtra("HSELECCIONADO", horarioSeleccionado);
                intent.putExtra("RUTA", rutaRecivida);

                startActivity(intent);

                break;
            case R.id.btn_horario3:
                horarioSeleccionado = fechaActual+"-"+btnHorario3.getText().toString();
                intent = new Intent(CompraBoletoActivity.this, AutobusActivity.class);
                intent.putExtra("HSELECCIONADO", horarioSeleccionado);
                intent.putExtra("RUTA", rutaRecivida);
                startActivity(intent);
                break;
            default:
            break;

        }

        }
}
