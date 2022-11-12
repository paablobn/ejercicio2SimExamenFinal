package com.example.ejercicio2simexamen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.AttributedCharacterIterator;
import java.text.NumberFormat;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    private List<Producto> objects;
    private int resource;
    private Context context;
    private MainActivity main;

    private NumberFormat numberFormat;

    public ProductosAdapter(List<Producto> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.main = (MainActivity)context;
        numberFormat = numberFormat.getCurrencyInstance();
    }

    @NonNull
    @Override
    public ProductosAdapter.ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        productoView.setLayoutParams(layoutParams);
        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosAdapter.ProductoVH holder, int position) {
        Producto producto = objects.get(position);
        holder.lbNombreProducto.setText(producto.getNombre());
        holder.lbCantidadProducto.setText(String.valueOf(producto.getCantidad()));
        holder.lbPrecioProducto.setText(numberFormat.format(producto.getPrecio()));

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto(producto, holder.getAdapterPosition()).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialog con todos los campos a editar
                // Necesita el todo
                // Necesito la posición
                editProducto(producto, holder.getAdapterPosition()).show();
            }
        });
    }

    private AlertDialog editProducto(Producto producto, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Editar Producto");
        builder.setCancelable(false);

        View productoView = LayoutInflater.from(context).inflate(R.layout.editar_layout, null);
        TextView txtNombreProducto = productoView.findViewById(R.id.txtNombreProductoEdit);
        TextView txtCantidadProducto = productoView.findViewById(R.id.txtCantidadProductoEdit);
        TextView txtPrecioProducto = productoView.findViewById(R.id.txtPrecioProductoEdit);
        builder.setView(productoView);

        txtNombreProducto.setText(producto.getNombre());
        txtCantidadProducto.setText(String.valueOf(producto.getCantidad()));
        txtPrecioProducto.setText(String.valueOf(producto.getPrecio()));

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                producto.setNombre(txtNombreProducto.getText().toString());
                producto.setCantidad(Integer.parseInt(txtCantidadProducto.getText().toString()));
                producto.setPrecio(Float.parseFloat(txtPrecioProducto.getText().toString()));
                objects.set(adapterPosition, producto);
                notifyDataSetChanged();
                main.calcularImporte();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    private AlertDialog eliminarProducto(Producto producto, int adapterPosition) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setCancelable(false);
        TextView mensaje = new TextView(context);
        mensaje.setText("¿ESTÁS SEGURO QUE QUIERES ELIMINARLO?");

        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.RED);
        mensaje.setPadding(50, 100, 50, 100);
        builder.setView(mensaje);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objects.remove(producto);
                notifyItemRemoved(adapterPosition);
                main.calcularImporte();
            }
        });
        return builder.create();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ProductoVH extends RecyclerView.ViewHolder {
        TextView lbNombreProducto;
        TextView lbCantidadProducto;
        TextView lbPrecioProducto;
        ImageButton btnEliminar;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            lbNombreProducto = itemView.findViewById(R.id.lbNombreProductoViewModel);
            lbCantidadProducto = itemView.findViewById(R.id.lbCantidadProductoViewModel);
            lbPrecioProducto = itemView.findViewById(R.id.lbPrecioProductoViewModel);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProductoViewModel);
        }
    }
}
