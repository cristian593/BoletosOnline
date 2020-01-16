package com.boletosonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private ImageView imgLogo;
    private Button btnRuta1, btnRuta2, btnRuta3, btnRuta4,  btnBoletos;
    private Intent intent;

    public static Boolean registroCompleto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgLogo = (ImageView) findViewById(R.id.logo);
        btnRuta1 = findViewById(R.id.btn_ruta1);
        btnRuta2 = findViewById(R.id.btn_ruta2);
        btnRuta3 = findViewById(R.id.btn_ruta3);
        btnRuta4 = findViewById(R.id.btn_ruta4);
        btnBoletos = findViewById(R.id.btn_boletos);
        Picasso.with(this).load(R.drawable.autobus2).fit().into(imgLogo);

        btnRuta1.setOnClickListener(this);
        btnRuta2.setOnClickListener(this);
        btnRuta3.setOnClickListener(this);
        btnRuta4.setOnClickListener(this);
        btnBoletos.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mi_cuenta:
                intent = new Intent(MainActivity.this, UsuarioActivity.class);
                intent.putExtra("USUARIO","EXISTENTE");
                startActivity(intent);
                break;
            case R.id.mis_boletos:
                intent = new Intent(MainActivity.this, MisBoletosActivity.class);
                startActivity(intent);
                break;
            case R.id.salir:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_ruta1:

                intent = new Intent(MainActivity.this, CompraBoletoActivity.class);
                intent.putExtra("RUTA","Riobamba-Quito");
                startActivity(intent);

                break;
            case R.id.btn_ruta2:

                intent = new Intent(MainActivity.this, CompraBoletoActivity.class);
                intent.putExtra("RUTA","Riobamba-Guayaquil");
                startActivity(intent);

                break;
            case R.id.btn_ruta3:
                intent = new Intent(MainActivity.this, CompraBoletoActivity.class);
                intent.putExtra("RUTA","Quito-Riobamba");
                startActivity(intent);
                break;
            case R.id.btn_ruta4:
                intent = new Intent(MainActivity.this, CompraBoletoActivity.class);
                intent.putExtra("RUTA","Guayaquil-Riobamba");
                startActivity(intent);
                break;

            case R.id.btn_boletos:
                intent = new Intent(MainActivity.this, MisBoletosActivity.class);
                startActivity(intent);
                break;

                default:
                    break;
        }
    }
}
