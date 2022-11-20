package com.example.examen2p;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity{
    private ImageView imagen;
    private EditText txtnombre,txtnumero,txtlatitud,txtlongitud;
    private Button salvar;
    private int validar_dato=1;
    private static final int REQUESTCODECAMARA=100;
    private static final int REQUESTTAKEFOTO=101;
    private static final int REQUEST_CODE=1;
    private String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagen = (ImageView) findViewById(R.id.imagen);
        txtnombre = (EditText) findViewById(R.id.nombre);
        txtnumero = (EditText) findViewById(R.id.numero);
        txtlatitud = (EditText) findViewById(R.id.latitud);
        txtlongitud = (EditText) findViewById(R.id.longitud);
        salvar = (Button) findViewById(R.id.salvar);
        txtlongitud.setText(getIntent().getStringExtra("cordsx"));
        txtlatitud.setText(getIntent().getStringExtra("cordsy"));
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificar_campos();
            }
        });
    }
    public boolean verificar(String dato,int numero){
        String opcion1="[A-Z,Ñ][a-z,ñ]{2,20}";
        String opcion2="[A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}";
        String opcion3="[A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}";
        String opcion4="[A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}[ ][A-Z,Ñ][a-z,ñ]{2,20}";
        String opcion5="[0-9]{8,10}";
        String opcion6="[0-9,_,.,-]{4,200}";
        switch(numero){
            case 1:{
                return dato.matches(opcion1+"|"+opcion2+"|"+opcion3+"|"+opcion4);
            }
            case 2:{
                return dato.matches(opcion5);
            }
            case 3:{
                return dato.matches(opcion6);
            }
            case 4:{
                return dato.matches(opcion6);
            }
            default:{
                return false;
            }
        }
    }
    public void verificar_campos(){
        if(verificar(txtnombre.getText().toString().trim(),validar_dato)){
            validar_dato=2;
            if(verificar(txtnumero.getText().toString().trim(),validar_dato)){
                validar_dato=3;
                if(verificar(txtlatitud.getText().toString().trim(),validar_dato)){
                    validar_dato=4;
                    if(verificar(txtlongitud.getText().toString().trim(),validar_dato)){
                        validar_dato=1;
                        insertar();
                    }
                    else{
                        Toast.makeText(this,"Conexion interrumpida",Toast.LENGTH_SHORT).show();
                        validar_dato=1;
                    }
                }
                else{
                    Toast.makeText(this,"Conexion interrumpida",Toast.LENGTH_SHORT).show();
                    validar_dato=1;
                }
            }
            else{
                Toast.makeText(this,"Numero no valido",Toast.LENGTH_SHORT).show();
                validar_dato=1;
            }
        }
        else{
            Toast.makeText(this,"Nombre no valido",Toast.LENGTH_SHORT).show();
            validar_dato=1;
        }
    }
    public void insertar(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://apk.salasar.xyz/examen2p.php", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Guardado exitoso", Toast.LENGTH_SHORT).show();
                txtnombre.setText("");
                txtnumero.setText("");
                txtnombre.setEnabled(false);
                txtnumero.setEnabled(false);
                salvar.setEnabled(false);
                txtlatitud.setText("");
                txtlongitud.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("Id","");
                parametros.put("nombre",txtnombre.getText().toString());
                parametros.put("numero",txtnumero.getText().toString());
                parametros.put("latitud",txtlatitud.getText().toString());
                parametros.put("longitud",txtlongitud.getText().toString());
                parametros.put("foto",currentPhotoPath);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void contactos_salvados(View view){
        Intent contactos=new Intent(this,ActivityLista.class);
        startActivity(contactos);
    }
    public void dispatchTakePictureIntent(){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUESTTAKEFOTO);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    //Agrega la foto al cuadro
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTTAKEFOTO && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            currentPhotoPath = Base64.getEncoder().encodeToString(bytes);
            salvar.setEnabled(true);
            txtnombre.setEnabled(true);
            txtnumero.setEnabled(true);
        }
    }
    public void permisos_camara(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUESTCODECAMARA);
        } else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUESTCODECAMARA){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Permiso Denegado",Toast.LENGTH_LONG).show();
            }
        }
        else{
            if(requestCode==REQUEST_CODE&&grantResults.length>0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    coordenadas();
                }
                else{
                    Toast.makeText(this,"Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void permisos1_gps(View view){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
        else{
            coordenadas();
        }
    }
    public void coordenadas(){
        Intent mapa=new Intent(this,ActivityMapa.class);
        startActivity(mapa);
    }
}