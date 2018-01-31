package com.jordysantamaria.jordysantamaria;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;
    private ImageButton imgBtnPhone;
    private ImageButton imgBtnWeb;
    private ImageButton imgBtnCamera;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        //editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        imgBtnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        //imgBtnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        //imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberPhone = editTextPhone.getText().toString();
                if(numberPhone != null && !numberPhone.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        OlderVersions(numberPhone);
                    }
                } else {
                    Toast.makeText(ThirdActivity.this, "Es necesario ingresar el numero de Telefono", Toast.LENGTH_SHORT).show();
                }
            }

            private void OlderVersions(String numberPhone) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+numberPhone));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ThirdActivity.this, "Has declinado en el acceso", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PHONE_CALL_CODE:

                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {

                    //Comprobar si ha sido aceptado o denegada la peticion
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Acepto dar permisos
                        String phoneNumber = editTextPhone.getText().toString();

                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+phoneNumber));
                        startActivity(intentCall);
                        
                    } else {
                        //No concedío su permiso
                        Toast.makeText(ThirdActivity.this, "Has declinado el acceso", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
