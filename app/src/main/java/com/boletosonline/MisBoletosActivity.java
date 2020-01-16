package com.boletosonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MisBoletosActivity extends AppCompatActivity {

    private String TAG = "GenerateQrCode";
    private ImageView imgQR;

    private RecyclerView recyclerViewBoletos;
    private RecyclerView.LayoutManager layoutManager;

    private  FirebaseRecyclerAdapter<Boletos, BoletosViewHolder> adapter;

    private DatabaseReference BoletosRef;


    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    private String CodigoBoletoQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_boletos);

        getSupportActionBar().setTitle("Mis Boletos");

        imgQR = findViewById(R.id.imgQR);

        recyclerViewBoletos = findViewById(R.id.recyclerBoletos);
        recyclerViewBoletos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBoletos.setLayoutManager(layoutManager);

        BoletosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Prevalent.usuarioActual.getCedula()).child("misBoletos");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Boletos> options =
                new FirebaseRecyclerOptions.Builder<Boletos>()
                .setQuery(BoletosRef, Boletos.class).build();

        FirebaseRecyclerAdapter<Boletos, BoletosViewHolder> adapter =

        new FirebaseRecyclerAdapter<Boletos, BoletosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoletosViewHolder holder, int position, @NonNull final Boletos boletos) {

                holder.txtBoletoRuta.setText(boletos.getRuta());
                holder.txtBoletoNombre.setText(boletos.getNombre());
                holder.txtBoletoAutobus.setText(boletos.getAutobus());
                holder.txtBoletoHorario.setText(boletos.getHorario());
                holder.txtBoletoLugares.setText(boletos.getNumeroLugares());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CrearCodigoParaQR(boletos.getNombre(), boletos.getAutobus(), boletos.getRuta(), boletos.getHorario(), boletos.getNumeroLugares());

                        CrearQR();

                    }
                });

            }

            @NonNull
            @Override
            public BoletosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boletos_items, parent, false);
                BoletosViewHolder holder = new BoletosViewHolder(view);

                return holder;
            }
        };

        recyclerViewBoletos.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }


    private void CrearCodigoParaQR(String nombre, String autobus, String ruta, String horario, String numeroLugares) {
        CodigoBoletoQR = nombre+"-Autobus N "+autobus+"-"+ruta+"-"+horario+"-"+numeroLugares+" Lugares";
    }

    private void CrearQR() {
        if(CodigoBoletoQR.length()>0){
            WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerdimension = width<height ? width:height;
            smallerdimension = smallerdimension*3/4;
            qrgEncoder = new QRGEncoder(CodigoBoletoQR, null, QRGContents.Type.TEXT, smallerdimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                imgQR.setImageBitmap(bitmap);

            }catch (WriterException e){
                Log.v(TAG, e.toString());

            }
        }else{
            Toast.makeText(MisBoletosActivity.this, "No hay texto", Toast.LENGTH_SHORT).show();
        }
    }


}
