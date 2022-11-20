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
import android.graphics.BitmapFactory;
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
public class ActivityModificar extends AppCompatActivity {
    private ImageView imagen;
    private EditText txtnombre,txtnumero,txtlatitud,txtlongitud;
    private String id;
    private Button actualizar,borrar;
    private static final int REQUESTCODECAMARA=100;
    private static final int REQUESTTAKEFOTO=101;
    private String currentPhotoPath;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        actualizar=(Button) findViewById(R.id.actualizar);
        borrar=(Button) findViewById(R.id.borrar);
        imagen = (ImageView) findViewById(R.id.imagen1);
        txtnombre = (EditText) findViewById(R.id.nombre1);
        txtnumero = (EditText) findViewById(R.id.numero1);
        txtlatitud = (EditText) findViewById(R.id.latitud1);
        txtlongitud = (EditText) findViewById(R.id.longitud1);
        byte[] bytes= Base64.getDecoder().decode(getIntent().getStringExtra("foto"));
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imagen.setImageBitmap(bitmap);
        txtnombre.setText(getIntent().getStringExtra("nombre"));
        txtnumero.setText(getIntent().getStringExtra("numero"));
        txtlatitud.setText(getIntent().getStringExtra("latitud"));
        txtlongitud.setText(getIntent().getStringExtra("longitud"));
        id=getIntent().getStringExtra("id");
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos_camara3();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }
    public void actualizar(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://apk.salasar.xyz/examen2p2.php", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Actualizacion exitoso", Toast.LENGTH_SHORT).show();
                txtnombre.setText("");
                txtnumero.setText("");
                txtnombre.setEnabled(false);
                txtnumero.setEnabled(false);
                txtlatitud.setText("");
                txtlongitud.setText("");
                Intent lista=new Intent(getApplicationContext(),ActivityLista.class);
                startActivity(lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("id",id);
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
    public void eliminar(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://apk.salasar.xyz/examen2p3.php", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Eliminacion exitoso", Toast.LENGTH_SHORT).show();
                txtnombre.setText("");
                txtnumero.setText("");
                txtnombre.setEnabled(false);
                txtnumero.setEnabled(false);
                txtlatitud.setText("");
                txtlongitud.setText("");
                Intent lista=new Intent(getApplicationContext(),ActivityLista.class);
                startActivity(lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("id",id);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
        }
    }
    public void permisos_camara3() {
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
    }
}