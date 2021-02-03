package com.example.rapartidorcatrepart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.rapartidorcatrepart.Models.Order;
import com.example.rapartidorcatrepart.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class ListaComidaEspera extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    DatabaseReference request1;
    DatabaseReference request2;
    TextView txtTotalPrice;
    Button btnPlace;
    String idSolicitud;

    FirebaseRecyclerAdapter<Order, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comida_espera);

        idSolicitud = getIntent().getStringExtra("idOrden");

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Solicitudes/" + idSolicitud + "/comidas");
        request1 = database.getReference("Solicitudes");
        request2 = database.getReference("Repartidor");
        recyclerView = (RecyclerView) findViewById(R.id.listaComidaEspera);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice  = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnAsignar);

        btnPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog();
                }
        });
        cargarLista();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListaComidaEspera.this);
        alertDialog.setTitle("Entrega de pedido");
        alertDialog.setMessage("¿Ya entregó este pedido?");

        alertDialog.setIcon(R.drawable.ic_baseline_face_24);


        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference request3  = database.getReference("Solicitudes/" + idSolicitud);;
                request3.child("status").setValue("2");
                Toast.makeText(ListaComidaEspera.this, "Repartidor asignado con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void cargarLista() {

        adapter = new FirebaseRecyclerAdapter<Order, CartViewHolder>(
                Order.class,
                R.layout.item_carrito,
                CartViewHolder.class,
                request.orderByKey()
        ) {

            @Override
            protected void populateViewHolder(CartViewHolder cartViewHolder, Order order, int i) {
                cartViewHolder.txt_comida.setText(order.getProductoNombre());

                Locale locale = new Locale("es", "CO");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                int precio = (Integer.parseInt(order.getPrecio()))*(Integer.parseInt(order.getCantidad()));

                cartViewHolder.txt_precio.setText(fmt.format(precio));

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound("" + order.getCantidad(), Color.RED);
                cartViewHolder.img.setImageDrawable(drawable);
            }
        };

        Locale locale = new Locale("es", "CO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        recyclerView.setAdapter(adapter);
        String total = getIntent().getStringExtra("total");
        txtTotalPrice.setText(fmt.format(Integer.parseInt(total)));
    }
}