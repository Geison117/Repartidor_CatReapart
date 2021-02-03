package com.example.rapartidorcatrepart.ui.pedido_enviado;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapartidorcatrepart.Interface.ItemClickListener;
import com.example.rapartidorcatrepart.ListaComidaEspera;
import com.example.rapartidorcatrepart.Models.Solicitud;
import com.example.rapartidorcatrepart.R;
import com.example.rapartidorcatrepart.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class pedido_enviado extends Fragment {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Solicitud, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    private PedidoEnviadoViewModel mViewModel;

    public static pedido_enviado newInstance() {
        return new pedido_enviado();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mViewModel =
                new ViewModelProvider(this).get(PedidoEnviadoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pedido_pendiente, container, false);

        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Solicitudes");
        recyclerView = (RecyclerView) root.findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);

        layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PedidoEnviadoViewModel.class);
        // TODO: Use the ViewModel
    }

    private void loadOrders(){

        Query pendientes = requests.orderByChild("status").equalTo("1");
        adapter = new FirebaseRecyclerAdapter<Solicitud, OrderViewHolder>(
                Solicitud.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                pendientes
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Solicitud solicitud, int i) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(conseguirStatus(solicitud.getStatus()));
                orderViewHolder.txtOrderAddress.setText(solicitud.getDireccion());
                orderViewHolder.txtOrderPhone.setText(solicitud.getTelefono());
                orderViewHolder.txtUsuario.setText(solicitud.getNombre());

                orderViewHolder.setItemListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent listaComida = new Intent(getContext(), ListaComidaEspera.class);
                        listaComida.putExtra("idOrden", adapter.getRef(position).getKey());
                        listaComida.putExtra("total", solicitud.getTotal());
                        listaComida.putExtra("status", "Entregado");
                        startActivity(listaComida);
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String conseguirStatus(String status) {
        if (status.equals("0")){
            return "En espera";
        }
        else if (status.equals("1")){
            return "Viene en camino";
        }
        else{
            return "Entregado";
        }
    }

}