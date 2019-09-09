package com.example.carloz.gymapp;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carloz.gymapp.adaptador.AdaptadorInstructorClientesAsignados;
import com.example.carloz.gymapp.items.ItemClienteInstructor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EliminarNutriologo extends AppCompatActivity {

    EditText etxtBusqueda;
    ImageButton btnBuscar;
    String nombre, apellido, registro;
    ArrayList<ItemClienteInstructor> listaClientes;
    RecyclerView recyclerClientes;
    TextView txtvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_nutriologo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar_EliminarNutriologo);
        etxtBusqueda = (EditText) findViewById(R.id.etxtBuscar_EliminarNutriologo);
        txtvTitulo = (TextView) findViewById(R.id.txtvNoActionEliminar_EliminarNutriologo);

        Typeface Thin = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");

        txtvTitulo.setTypeface(Thin);

        listaClientes = new ArrayList<>();

        recyclerClientes = (RecyclerView) findViewById(R.id.listvClientesEliminar_EliminarNutriologo);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexionBDBusqueda();
            }
        });
    }

    private void conexionBDBusqueda() {
        InputMethodManager manager = (InputMethodManager)getSystemService(EliminarNutriologo.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(btnBuscar.getWindowToken(),0);
        listaClientes.clear();
        String url = "http://thegymlife.online/php/admin/Administrador_Buscar_Nutriologo.php?buscar="+etxtBusqueda.getText().toString();
        url = url.replaceAll(" ", "%20");

        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //String valor = response.getString("Estado");
                                    JSONArray jsonArray = response.getJSONArray("Nutriologos");

                                    AdaptadorInstructorClientesAsignados adapter = new AdaptadorInstructorClientesAsignados(listaClientes);

                                    adapter.contexto= EliminarNutriologo.this;
                                    for (int i =0; i<jsonArray.length();i++){
                                        JSONObject clientes = jsonArray.getJSONObject(i);
                                        registro =clientes.getString("Nutriologo_Registro");
                                        nombre =clientes.getString("Nutriologo_Nombre");
                                        apellido =clientes.getString("Nutriologo_Apellido");

                                        listaClientes.add(new ItemClienteInstructor(nombre,registro,apellido,"0"));
                                    }

                                    recyclerClientes.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(EliminarNutriologo.this,"Error php",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(EliminarNutriologo.this);
        x.add(peticion);

    }


}

