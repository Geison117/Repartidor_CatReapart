package com.example.rapartidorcatrepart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rapartidorcatrepart.Common.Common;
import com.example.rapartidorcatrepart.Models.Repartidor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario;
    EditText txtContrasena;
    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txt_usuario);
        txtContrasena = (EditText) findViewById(R.id.txt_contrasena);
        btnEntrar = (Button) findViewById(R.id.btn_entrar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_admin = database.getReference("Repartidor");

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialogo = new ProgressDialog(MainActivity.this);
                dialogo.setMessage("Por favor espera");
                dialogo.show();

                table_admin.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialogo.dismiss();
                        if (snapshot.child(txtUsuario.getText().toString()).exists()){
                            Repartidor repartidor = snapshot.child(txtUsuario.getText().toString()).getValue(Repartidor.class);
                            repartidor.setTelefono(txtUsuario.getText().toString());

                            if (repartidor.getContrasena().equals(txtContrasena.getText().toString())) {
                                Intent inicio = new Intent(MainActivity.this, MenuAdmin.class);
                                Common.repartidor_actual = repartidor;
                                startActivity(inicio);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Contraseña errónea", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "El administrador no existe", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




    }
}