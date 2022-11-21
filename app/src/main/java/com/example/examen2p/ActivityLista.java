package com.example.examen2p;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examen2p.configuracion.datos;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
public class ActivityLista extends AppCompatActivity {
    private ArrayList<datos> contactolista;
    private ArrayList<String> id;
    private ArrayList<String> nombre;
    private ArrayList<String> numero;
    private ArrayList<String> latitud;
    private ArrayList<String> longitud;
    private ArrayList<String> foto;
    private ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        lista=(ListView) findViewById(R.id.lista_basedatos);
        ObtenerLista();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent modificar=new Intent(view.getContext(),ActivityModificar.class);
                modificar.putExtra("id",id.get(i));
                modificar.putExtra("nombre",nombre.get(i));
                modificar.putExtra("numero",numero.get(i));
                modificar.putExtra("latitud",latitud.get(i));
                modificar.putExtra("longitud",longitud.get(i));
                modificar.putExtra("foto",foto.get(i));
                startActivity(modificar);
            }
        });
    }
    public void agregar(View view){
        Intent principal=new Intent(this,MainActivity.class);
        principal.putExtra("entrada","0");
        principal.putExtra("entrada1","0");
        startActivity(principal);
    }
    private void ObtenerLista() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://apk.salasar.xyz/examen2p1.php?codigo=1234", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray contactoarray=jsonObject.getJSONArray("contacto");
                    datos persona=null;
                    contactolista=new ArrayList<datos>();
                    for(int i=0;i<contactoarray.length();i++){
                        JSONObject rowcontacto=contactoarray.getJSONObject(i);
                        persona=new datos(
                                rowcontacto.getString("id"),
                                rowcontacto.getString("nombre"),
                                rowcontacto.getString("numero"),
                                rowcontacto.getString("latitud"),
                                rowcontacto.getString("longitud"),
                                rowcontacto.getString("foto")
                        );
                        contactolista.add(persona);
                    }
                    fllList();
                }
                catch (Throwable error){
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void fllList() {
        id=new ArrayList<String>();
        nombre=new ArrayList<String>();
        numero=new ArrayList<String>();
        latitud=new ArrayList<String>();
        longitud=new ArrayList<String>();
        foto=new ArrayList<String>();
        for(int i=0;i<contactolista.size();i++){
            id.add(
                    contactolista.get(i).getId()
            );
            nombre.add(
                    contactolista.get(i).getNombre()
            );
            numero.add(
                    contactolista.get(i).getNumero()
            );
            latitud.add(
                    contactolista.get(i).getLatitud()
            );
            longitud.add(
                    contactolista.get(i).getLongitud()
            );
            foto.add(
                    contactolista.get(i).getFoto()
            );
        }
        lista.setAdapter(new Adaptador(this,id,nombre,numero,latitud,longitud,foto));
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}