package com.boletosonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UsuarioActivity extends AppCompatActivity {

    private EditText usuarioNombre, usuarioCedula, usuarioTelefono, usuarioMail, targetaNombre, targetaNumero, targetaFecha, targetaCodigoCvc;
    private Button btnGuardar;
    private String uNombre, uCedula, uTelefono, uMail, tNombre, tNumero, tFecha, tCodigo, usuarioRecivido;
    private ProgressDialog loadingBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Bundle tipoUsuario = this.getIntent().getExtras();
        usuarioRecivido = tipoUsuario.getString("USUARIO");

        usuarioNombre = findViewById(R.id.usuario_nombre);
        usuarioCedula = findViewById(R.id.usuario_identificacion);
        usuarioTelefono = findViewById(R.id.usuario_telefono);
        usuarioMail = findViewById(R.id.usuario_correo);
        targetaNombre = findViewById(R.id.nombre_en_tarjeta);
        targetaNumero = findViewById(R.id.numero_de_tarjeta);
        targetaFecha = findViewById(R.id.fecha_caducidad_tarjeta);
        targetaCodigoCvc = findViewById(R.id.codigo_cvc__tarjeta);

        btnGuardar = findViewById(R.id.btn_guardar);

        loadingBar = new ProgressDialog(this);

        if(usuarioRecivido.equals("EXISTENTE")){
            MostrarInformacionUsuario();

        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapturarInformacion();

            }
        });

    }

    private void MostrarInformacionUsuario() {

        Toast.makeText(this, ":0"+Prevalent.usuarioActual.getNombre(), Toast.LENGTH_LONG).show();
        usuarioNombre.setText(Prevalent.usuarioActual.getNombre());
        usuarioCedula.setText(Prevalent.usuarioActual.getCedula());
        usuarioTelefono.setText(Prevalent.usuarioActual.getTelefono());
        usuarioMail.setText(Prevalent.usuarioActual.getContraseña());
        targetaNombre.setText(Prevalent.usuarioActual.getNombreTarjeta());
        targetaNumero.setText(Prevalent.usuarioActual.getNumeroTarjeta());
        targetaFecha.setText(Prevalent.usuarioActual.getFechacaducidadtarjeta());
        targetaCodigoCvc.setText(Prevalent.usuarioActual.getCodigocvctarjeta());

    }


    private void CapturarInformacion() {
        uNombre = usuarioNombre.getText().toString();
        uCedula = usuarioCedula.getText().toString();
        uTelefono = usuarioTelefono.getText().toString();
        uMail = usuarioMail.getText().toString();
        tNombre = targetaNombre.getText().toString();
        tNumero = targetaNumero.getText().toString();
        tFecha = targetaFecha.getText().toString();
        tCodigo = targetaCodigoCvc.getText().toString();

        if (TextUtils.isEmpty(uNombre)) {
            Toast.makeText(this, "Por favor Ingrese Su Nombre", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(uCedula)) {
            Toast.makeText(this, "Por favor Ingrese Su Numero de Cedula", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(uTelefono)) {
            Toast.makeText(this, "Por favor Ingrese Su Numero de Telefono", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(uMail)) {
            Toast.makeText(this, "Por favor Ingrese Su Contraseña", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tNombre)) {
            Toast.makeText(this, "Por favor Ingrese el Nombre de la Targeta", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tNumero)) {
            Toast.makeText(this, "Por favor Ingrese el Numero de la Targeta", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tFecha)) {
            Toast.makeText(this, "Por favor Ingrese la Fecha de Caducidad de la Targeta", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tCodigo)) {
            Toast.makeText(this, "Por favor Ingrese el Codigo CVC de la Targeta", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Guardando..");
            loadingBar.setMessage("Espere mientras guardamos su informacion");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidarUsuario(uNombre, uCedula, uTelefono, uMail, tNombre, tNumero, tFecha, tCodigo);
        }
    }

    private void ValidarUsuario(final String uNombre, final String uCedula, final String uTelefono, final String uMail,
                                final String tNombre, final String tNumero, final String tFecha, final String tCodigo) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (!(dataSnapshot.child("Usuarios").child(uCedula).exists())){

                    //Creamos el nuevo usuario
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("nombre",uNombre);
                    userDataMap.put("cedula",uCedula);
                    userDataMap.put("telefono",uTelefono);
                    userDataMap.put("contraseña",uMail);
                    userDataMap.put("nombreTarjeta",tNombre);
                    userDataMap.put("numeroTarjeta",tNumero);
                    userDataMap.put("fechacaducidadtarjeta",tFecha);
                    userDataMap.put("codigocvctarjeta",tCodigo);

                    rootRef.child("Usuarios").child(uCedula).setValue(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                MainActivity.registroCompleto = true;
                                Toast.makeText(UsuarioActivity.this, " Informacion Guardada ",
                                        Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(UsuarioActivity.this, MainActivity.class);
                                    startActivity(intent);
                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(UsuarioActivity.this, " Error de coneccion, por favor intente mas tarde ",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }else {
                    //El mnumero ingresado ya esta registrado
                    //Actualizamos la informacion del usuario
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("nombre",uNombre);
                    userDataMap.put("cedula",uCedula);
                    userDataMap.put("telefono",uTelefono);
                    userDataMap.put("contraseña",uMail);
                    userDataMap.put("nombreTarjeta",tNombre);
                    userDataMap.put("numeroTarjeta",tNumero);
                    userDataMap.put("fechacaducidadtarjeta",tFecha);
                    userDataMap.put("codigocvctarjeta",tCodigo);

                    rootRef.child("Usuarios").child(uCedula).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                MainActivity.registroCompleto = true;
                                Toast.makeText(UsuarioActivity.this, " Informacion Guardada ",
                                        Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(UsuarioActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(UsuarioActivity.this, " Error de coneccion, por favor intente mas tarde ",
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

}


