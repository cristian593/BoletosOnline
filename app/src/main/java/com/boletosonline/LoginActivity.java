package com.boletosonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText usuario, password;
    private TextView crearCuenta;
    private CheckBox recuerdame;
    private Button btnIngresar;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("BIENVENIDO");

        usuario = findViewById(R.id.usuario);
        password = findViewById(R.id.password);
        crearCuenta = findViewById(R.id.crear_cuenta);
        recuerdame = findViewById(R.id.recuerdame);
        btnIngresar = findViewById(R.id.ingresar);

        //Inicializamos el paperDb
        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info log","Button Clicked" );
                loginUser();
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                intent.putExtra("USUARIO","NUEVO");
                startActivity(intent);
            }
        });


    }

    private void loginUser() {

        String numCedula = usuario.getText().toString();
        String pass = password.getText().toString();

        if(TextUtils.isEmpty(numCedula)){
            Toast.makeText(this, "por favor ingrese el usuario", Toast.LENGTH_LONG).show();
        }else  if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
        }else{

            loadingBar.setTitle("Ingresando...");
            loadingBar.setMessage("Por favo espere mientras verificamos la cuenta");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            //Validamos el usuario y password 
            ValidarUsuario( numCedula, pass);
        }

    }

    private void ValidarUsuario(final String numCedula, final String pass) {

        if (recuerdame.isChecked()){
            Paper.book().write(Prevalent.userCedulaKey, numCedula);
            Paper.book().write(Prevalent.userPasswordKey, pass);
        }
        //Creamos la referencia para acceder a la Bd
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        //consultanos el usuario
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Usuarios").child(numCedula).exists()){

                    Usuario usuarioDatos = dataSnapshot.child("Usuarios").child(numCedula).getValue(Usuario.class);

                  //  Log.i("Info ","Button Clicked"+usuarioDatos.getNombre() );


                    if(usuarioDatos.getCedula().equals(numCedula)){

                        if(usuarioDatos.getContraseña().equals(pass)){

                            Toast.makeText(LoginActivity.this, "Bienvenido....", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           //Limpiamos la pila de actividades
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //Guardamos el usuario que se logeo
                            Prevalent.usuarioActual = usuarioDatos;
                            startActivity(intent);
                        }else {

                            //Cuando la password es incorrecta
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password Incorrecta",
                                    Toast.LENGTH_LONG).show();
                            password.setText("");
                        }

                    }
                }else {
                    Toast.makeText(LoginActivity.this, "La cuenta "+numCedula+" no existe",
                            Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Por favor Crea una nueva cuenta", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
