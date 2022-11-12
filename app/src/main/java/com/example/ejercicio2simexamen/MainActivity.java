package com.example.ejercicio2simexamen;

import android.content.Intent;
import android.hardware.lights.LightsManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.ejercicio2simexamen.databinding.ActivityMainBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> productos;

    private ProductosAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private ActivityResultLauncher<Intent> crearProductoLauncher;

    private NumberFormat numberFormat;

    private int cantidad;

    private float precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        productos = new ArrayList<>();
        inicializaActivitiesResultLaunchers();

        numberFormat = NumberFormat.getCurrencyInstance();
        calcularImporte();

        adapter = new ProductosAdapter(productos, R.layout.producto_card_view, this);
        layoutManager = new GridLayoutManager(this, 1);
        binding.conteinMain.contenedor.setAdapter(adapter);
        binding.conteinMain.contenedor.setLayoutManager(layoutManager);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, agregar_producto_activity.class);
                crearProductoLauncher.launch(intent);
            }
        });
    }

    private void inicializaActivitiesResultLaunchers() {
        crearProductoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null && result.getData().getExtras() != null) {
                                Producto producto = (Producto) result.getData().getExtras().getSerializable("producto");
                                productos.add(0, producto);
                                adapter.notifyItemInserted(0);
                                calcularImporte();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "CANCELADO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void calcularImporte() {
        for (Producto producto : productos) {
            cantidad += producto.getCantidad();
            precio += producto.getPrecio() * producto.getCantidad();
        }
        binding.conteinMain.lbCantidadProductos.setText(String.valueOf(cantidad));
        binding.conteinMain.lbImporteTotal.setText(numberFormat.format(precio));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("producto", productos);
        outState.putInt("cantidad", cantidad);
        outState.putFloat("precio", precio);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> datos = (ArrayList<Producto>) savedInstanceState.getSerializable("producto");
        productos.addAll(datos);
        adapter.notifyItemRangeInserted(0, productos.size());
        cantidad = savedInstanceState.getInt("cantidad");
        precio = savedInstanceState.getFloat("precio");
    }
}