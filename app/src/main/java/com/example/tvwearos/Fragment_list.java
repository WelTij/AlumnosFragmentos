package com.example.tvwearos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_list extends Fragment implements SearchView.OnQueryTextListener {

    //Variables para el fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //Lista y RecyclerView
    private RecyclerView recyclerView;
    private ArrayList<Alumnos> alumnosList;
    private MyAdapter myAdapter;

    //animacion de lottie
    private LottieAnimationView lottieAnimationView;
    private Handler handler = new Handler();
    private static final int LOADING_DELAY = 3000;

    public Fragment_list() {
    }

    public static Fragment_list newInstance(String param1, String param2) {
        Fragment_list fragment = new Fragment_list();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view);

        // RecyclerView en grid de 2 columnas :)
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2 );
        recyclerView.setLayoutManager(layoutManager);

        // Buscador
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (myAdapter != null) {
            myAdapter.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }


    private void fetchData() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        recyclerView.setVisibility(View.GONE);

        // Conexión a la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.3.43/api-tv/public/api")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Servicios servicios = retrofit.create(Servicios.class);

        Call<List<Alumnos>> call = servicios.getData();
        call.enqueue(new Callback<List<Alumnos>>() {
            @Override
            public void onResponse(Call<List<Alumnos>> call, Response<List<Alumnos>> response) {
                if (response.isSuccessful()) {
                    alumnosList = new ArrayList<>(response.body());
                    myAdapter = new MyAdapter(getContext(), alumnosList);
                    recyclerView.setAdapter(myAdapter);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieAnimationView.setVisibility(View.GONE);
                            lottieAnimationView.cancelAnimation();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }, LOADING_DELAY);
                } else {
                    Log.d("TAG", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Alumnos>> call, Throwable t) {
                lottieAnimationView.setVisibility(View.GONE);
            }
        });
    }
}
