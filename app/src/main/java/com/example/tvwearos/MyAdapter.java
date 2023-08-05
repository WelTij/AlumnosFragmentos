package com.example.tvwearos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Alumnos> alumnosList;
    private ArrayList<Alumnos> filteredAlumnosList;

    public MyAdapter(Context context, ArrayList<Alumnos> alumnosList) {
        this.context = context;
        this.alumnosList = alumnosList;
        this.filteredAlumnosList = new ArrayList<>(alumnosList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alumnos alumnos = filteredAlumnosList.get(position);
        holder.matricula.setText(alumnos.matricula);
        holder.nombre.setText(alumnos.nombre);
        holder.apellidos.setText(alumnos.apellidos);
        holder.curp.setText(alumnos.curp);
        holder.sexo.setText(alumnos.sexo);
        holder.edad.setText(alumnos.edad);

        Glide.with(context)
                .load(alumnos.image_url)
                .placeholder(R.drawable.carga)
                .apply(new RequestOptions().placeholder(R.drawable.carga))
                .into(holder.titleImage);
    }

    @Override
    public int getItemCount() {
        return filteredAlumnosList.size();
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Alumnos> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(alumnosList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Alumnos alumnos : alumnosList) {
                    if (alumnos.matricula.toLowerCase().contains(filterPattern) ||
                            alumnos.nombre.toLowerCase().contains(filterPattern) ||
                            alumnos.apellidos.toLowerCase().contains(filterPattern) ||
                            alumnos.curp.toLowerCase().contains(filterPattern) ||
                            alumnos.sexo.toLowerCase().contains(filterPattern) ||
                            alumnos.edad.toLowerCase().contains(filterPattern)) {
                        filteredList.add(alumnos);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredAlumnosList.clear();
            filteredAlumnosList.addAll((ArrayList<Alumnos>) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView titleImage;
        TextView matricula;
        TextView nombre;
        TextView apellidos;
        TextView curp;
        TextView sexo;
        TextView edad;

        ViewHolder(View view) {
            super(view);
            titleImage = view.findViewById(R.id.title_image);
            matricula = view.findViewById(R.id.matricula);
            nombre = view.findViewById(R.id.nombre);
            apellidos = view.findViewById(R.id.apellidos);
            curp = view.findViewById(R.id.curp);
            sexo = view.findViewById(R.id.sexo);
            edad = view.findViewById(R.id.edad);
        }
    }
}
