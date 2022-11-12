package com.example.ejercicio2simexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ejercicio2simexamen.databinding.ActivityAgregarProductoBinding;
import com.example.ejercicio2simexamen.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class agregar_producto_activity extends AppCompatActivity {

    private ActivityAgregarProductoBinding binding;
    private ArrayList<Producto> productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        binding = ActivityAgregarProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.txtNombreProducto.getText().toString().isEmpty() &&
                        !binding.txtCantidadProducto.toString().isEmpty() &&
                        !binding.txtPrecioProducto.getText().toString().isEmpty()) {

                    Producto producto = new Producto(binding.txtNombreProducto.getText().toString(),
                            Integer.parseInt(binding.txtCantidadProducto.getText().toString()) ,
                           Float.parseFloat(binding.txtPrecioProducto.getText().toString())) ;

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("producto", producto);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(agregar_producto_activity.this, "RELLENA LOS DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}