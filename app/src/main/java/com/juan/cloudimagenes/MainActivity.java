package com.juan.cloudimagenes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    Map uploadResutl;
    Button btnTomar;
    Button btnSubir;
    Button btnBajar;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map config = new HashMap();
        config.put("cloud_name","dmg0z49kl");
        config.put("api_key","969467741463182");
        config.put("api_secret","Tsmwr4OiRVDaEfjvI0iMO6Ry6Es");
        final  Cloudinary cloudinary= new Cloudinary(config);

        btnBajar=findViewById(R.id.btnDownload);


        btnTomar=findViewById(R.id.btnTomarFoto);
        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1,null);
            }
        });

        btnSubir=findViewById(R.id.btnUpload);
        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap= ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                UploadImagen task = new UploadImagen();
                task.execute(bitmap,cloudinary);
            }
        });

        imagen=(ImageView) findViewById(R.id.imagenFoto);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap= (Bitmap) data.getExtras().get("data");
        imagen.setImageBitmap(bitmap);
    }


    class  UploadImagen extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            Bitmap bitmap =(Bitmap) objects[0];
            Cloudinary cloudinary= (Cloudinary) objects[1];
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,80,baos);
            ByteArrayInputStream bais= new ByteArrayInputStream(baos.toByteArray());
            try {
                uploadResutl = cloudinary.uploader().upload(bais, ObjectUtils.asMap("public_id","foto1"));
                Log.d("URL", "LA URL ES: "+uploadResutl.get("url"));
            }
            catch (IOException e)
            {
                Log.d("Cloudinary", e.getMessage());
            }
            return null;
        }
    }

}

