package com.example.examen2p;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Base64;
public class Adaptador extends BaseAdapter {
    private static LayoutInflater inflater=null;
    Context contexto;
    ArrayList<String> id;
    ArrayList<String> nombre;
    ArrayList<String> numero;
    ArrayList<String> latitud;
    ArrayList<String> longitud;
    ArrayList<String> fotos;
    private ImageView foto;
    private TextView txtnombre,txtnumero,txtlatitud,txtlongitud;
    public Adaptador(Context contexto, ArrayList<String> id, ArrayList<String> nombre, ArrayList<String> numero, ArrayList<String> latitud, ArrayList<String> longitud, ArrayList<String> fotos){
        this.contexto=contexto;
        this.id=id;
        this.nombre=nombre;
        this.numero=numero;
        this.latitud=latitud;
        this.longitud=longitud;
        this.fotos=fotos;
        inflater=(LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista=inflater.inflate(R.layout.elemento_lista,null);
        foto=(ImageView) vista.findViewById(R.id.foto);
        txtnombre=(TextView) vista.findViewById(R.id.txtnombre);
        txtnumero=(TextView) vista.findViewById(R.id.txtnumero);
        txtlatitud=(TextView) vista.findViewById(R.id.txtlatitud);
        txtlongitud=(TextView) vista.findViewById(R.id.txtlongitud);
        txtnombre.setText(nombre.get(i));
        txtnumero.setText("Telefono: "+numero.get(i));
        txtlatitud.setText("Y: "+latitud.get(i));
        txtlongitud.setText("X: "+longitud.get(i));
        byte[] bytes= Base64.getDecoder().decode(fotos.get(i));
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        foto.setImageBitmap(bitmap);
        foto.setTag(i);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubicacion=new Intent(contexto,ActivityUbicacion.class);
                ubicacion.putExtra("etiqueta",nombre.get((Integer)view.getTag()));
                ubicacion.putExtra("Y",latitud.get((Integer)view.getTag()));
                ubicacion.putExtra("X",longitud.get((Integer)view.getTag()));
                contexto.startActivity(ubicacion);
            }
        });
        /*txtnombre.getTag(i);
        txtnombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modificar=new Intent(view.getContext(),ActivityModificar.class);
                modificar.putExtra("id",id.get(i));
                modificar.putExtra("nombre",nombre.get(i));
                modificar.putExtra("numero",numero.get(i));
                modificar.putExtra("latitud",latitud.get(i));
                modificar.putExtra("longitud",longitud.get(i));
                modificar.putExtra("foto",foto.get(i));
                contexto.startActivity(modificar);
            }
        });*/
        return vista;
    }
    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
