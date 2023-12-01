package com.example.proyectonacional2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://broker.emqx.io:1883";
    private static final String CLIENT_ID = "AndroidSample12312312312312312";
    private MqttCliente mqttHandler;
    EditText resenas;
    EditText nombrePe;
    Button envio;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Pelicula> ListPEL = new ArrayList<Pelicula>();
    ArrayAdapter<Pelicula> arrayAdapterPelicula;
    ListView lvListadoPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttHandler = new MqttCliente();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);


        nombrePe = findViewById(R.id.agreganombre);
        envio = findViewById(R.id.btnagregar);
        lvListadoPeliculas = findViewById(R.id.LVpeliculas);

        envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(resenas.getText());
                publicarMensaje("peliculas/resenas", text);

                Pelicula pel = new Pelicula();

                pel.setNombre(UUID.randomUUID().toString());
                pel.setResena(resenas.getText().toString());
                databaseReference.child("Mensaje").child(Pelicula.getNombre()).setValue(pel);

                resenas.setText("");
            }

        });
        iniciarFirebase();
        listarDatos();

    }

    private void iniciarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onDestroy(){
        mqttHandler.disconnect();
        super.onDestroy();
    }

    private void publicarMensaje (String topic, String mensaje){
        Toast.makeText(this, "Publicando: " + mensaje, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic, mensaje);
    }

    private void listarDatos() {
        databaseReference.child("Mensaje").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListPEL.clear();
                for (DataSnapshot objs : snapshot.getChildren()){
                    Pelicula lp =objs.getValue(Pelicula.class);
                    ListPEL.add(lp);
                    arrayAdapterPelicula =new ArrayAdapter<Pelicula>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,ListPEL);
                    lvListadoPeliculas.setAdapter(arrayAdapterPelicula);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }

