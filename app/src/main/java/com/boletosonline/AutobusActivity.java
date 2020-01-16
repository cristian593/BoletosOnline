package com.boletosonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AutobusActivity extends AppCompatActivity {

    private String  horarioRecivido, ruta;
    private ImageView asientoLibre, asientoOcupado;
    private TextView numAutobus, lugaresLibres, lugaresOcupados, txtspinnerSelec, valorPagar;
    private Button btnComprar;

    private String autobus;
    private String lLibres;
    private String lOcupados;
    private String valorBoletos;

    private String IDboleto;
    private String saveCurrentTime;
    private String nombreU;

    private String numBoletosComprar;
    private Spinner spinnerNumBoletos;

    private ProgressDialog loadingBar;



    private int maxLugares = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autobus);

        loadingBar = new ProgressDialog(this);
        asientoLibre = findViewById(R.id.lugares_libres);
        Picasso.with(this).load(R.drawable.lugar1).fit().into(asientoLibre);

        asientoOcupado = findViewById(R.id.lugares_ocupados);
        Picasso.with(this).load(R.drawable.lugar2).fit().into(asientoOcupado);

        spinnerNumBoletos = findViewById(R.id.numBoletos);
        txtspinnerSelec = findViewById(R.id.txtSpinnerSelected);

        numAutobus = findViewById(R.id.numAutobus);
        lugaresLibres = findViewById(R.id.libres);
        lugaresOcupados = findViewById(R.id.ocupados);
        valorPagar = findViewById(R.id.txtvalor_pagar);
        btnComprar = findViewById(R.id.btn_comprar);



        Bundle datoRecivido = this.getIntent().getExtras();
        horarioRecivido = datoRecivido.getString("HSELECCIONADO");
        ruta = datoRecivido.getString("RUTA");

        getSupportActionBar().setTitle(ruta);

        MostrarInformaciondelAutobus(numAutobus, lugaresLibres, lugaresOcupados, valorPagar);

        ActivarSpinner();

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AutobusActivity.this, ":0", Toast.LENGTH_SHORT).show();
                //Comprovamos disponibilidad
                int nB = Integer.parseInt(numBoletosComprar);

                if(nB<=maxLugares){
                    showdialogo();
                }else {
                    Toast.makeText(AutobusActivity.this,"Lo sentimos no contamos con tantos Lugares Disponibles ",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void ActivarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AutobusActivity.this, R.array.numeros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumBoletos.setAdapter(adapter);

        spinnerNumBoletos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numBoletosComprar = parent.getItemAtPosition(position).toString();
                int nBC = Integer.parseInt(numBoletosComprar);
                int cB = Integer.parseInt(valorBoletos);
                int vp = nBC*cB;
                String CP = String.valueOf(vp);
                valorPagar.setText(CP);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showdialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AutobusActivity.this);
        builder.setTitle("Finalizar Compra")
                .setMessage("El costo de los Boletos " +valorPagar.getText()+"  dolares seran descontados de su tarjeta de credito registrada")
                .setCancelable(false)
                .setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int lL = Integer.parseInt(lLibres);
                        int lO = Integer.parseInt(lOcupados);
                        int nBC = Integer.parseInt(numBoletosComprar);
                        lL = lL - nBC;
                        lO = lO + nBC;
                        lLibres = String.valueOf(lL);
                        lOcupados = String.valueOf(lO);

                        ActualizarInformacionAutobus(lLibres, lOcupados, autobus, valorBoletos);
                        nombreU = Prevalent.usuarioActual.getNombre();
                        ActualizarCompras(nombreU, autobus, ruta, horarioRecivido, numBoletosComprar);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AutobusActivity.this,"Compra cancelada",Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();

    }

    private void ActualizarCompras(final String nombreU, final String autobus, final String rutaN, final String horarioRecivido, final String numBoletosComprar) {

        loadingBar.setTitle("Creando Boletos..");
        loadingBar.setMessage("Espere mientras creamos los boletos");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        IDboleto = ruta+"-"+horarioRecivido+"-"+saveCurrentTime;
        final DatabaseReference rootRefMyBoleto;
        rootRefMyBoleto = FirebaseDatabase.getInstance().getReference();
        rootRefMyBoleto.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Usuarios").child(Prevalent.usuarioActual.getCedula()).child("misBoletos").child(IDboleto).exists())){

                    //Creamos el nuevo boleto
                    HashMap<String, Object> userDataBoleto = new HashMap<>();
                    userDataBoleto.put("nombre",Prevalent.usuarioActual.getNombre());
                    userDataBoleto.put("autobus",autobus);
                    userDataBoleto.put("ruta",rutaN);
                    userDataBoleto.put("horario",horarioRecivido);
                    userDataBoleto.put("numeroLugares",numBoletosComprar);

                    rootRefMyBoleto.child("Usuarios").child(Prevalent.usuarioActual.getCedula())
                            .child("misBoletos").child(IDboleto).setValue(userDataBoleto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AutobusActivity.this, " Boletos Creados ",
                                        Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(AutobusActivity.this, " Error de coneccion, por favor intente mas tarde ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ActualizarInformacionAutobus(final String lLibres, final String lOcupados, final String autobus, final String valorBoletos) {
        loadingBar.setTitle("Comprando..");
        loadingBar.setMessage("Espere mientras guardamos su compra");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference rootRefbus;
        rootRefbus = FirebaseDatabase.getInstance().getReference();
        rootRefbus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("Rutas").child(ruta).child(horarioRecivido).exists())) {

                    HashMap<String, Object> autoBusMap = new HashMap<>();
                    autoBusMap.put("lugaresLibres",lLibres);
                    autoBusMap.put("lugaresOcupados",lOcupados);
                    autoBusMap.put("numeroBus",autobus);
                    autoBusMap.put("valorBoleto",valorBoletos);


                    rootRefbus.child("Rutas").child(ruta).child(horarioRecivido).updateChildren(autoBusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                loadingBar.dismiss();

                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(AutobusActivity.this, " Error de coneccion, por favor intente mas tarde ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void MostrarInformaciondelAutobus(final TextView numAutobus, final TextView lugaresLibres, final TextView lugaresOcupados, final TextView valorPagar) {

        DatabaseReference autobusRef = FirebaseDatabase.getInstance().getReference()
                .child("Rutas").child(ruta).child(horarioRecivido);
                autobusRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            autobus = dataSnapshot.child("numeroBus").getValue().toString();
                            lLibres = dataSnapshot.child("lugaresLibres").getValue().toString();
                            lOcupados = dataSnapshot.child("lugaresOcupados").getValue().toString();
                            valorBoletos = dataSnapshot.child("valorBoleto").getValue().toString();

                            numAutobus.setText("Autobus Numero "+autobus);
                            lugaresLibres.setText(lLibres);
                            lugaresOcupados.setText(lOcupados);

                            maxLugares = Integer.parseInt(lLibres);
                            valorPagar.setText(valorBoletos);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
