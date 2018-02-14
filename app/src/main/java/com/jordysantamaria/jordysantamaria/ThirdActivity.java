package com.jordysantamaria.jordysantamaria;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
    private final int PICTURE_FROM_CAMERA = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Forzar y cargar icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        imgBtnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imgBtnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        //Boton para realizar llamada telefonica
        imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberPhone = editTextPhone.getText().toString();
                if(numberPhone != null && !numberPhone.isEmpty()) {
                    //Comprobar version actual de android que estamos ocupando
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //Comprobar si ha aceptado, no ha aceptado o nunca se le ha preguntado.
                        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                            //Ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+numberPhone));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
                            startActivity(i);
                        } else {
                            // Ha denegado o es la primera vez que se le pregunta
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                //No se le ha preguntado aún
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            } else {
                                //Ha denegado
                                Toast.makeText(ThirdActivity.this, "Por favor, da permisos a la aplicación.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package: " + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }

                        //requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
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
                    Toast.makeText(ThirdActivity.this, "Has declinado el acceso", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Boton para la dirección web
        imgBtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = editTextWeb.getText().toString();
                String email = "santmjoy@gmail.com";

                if (url != null && !url.isEmpty()) {

                    //INTENT WEB
                    //Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
                    Intent intentWeb = new Intent();

                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://"+url));

                    //INTENT CONTACTOS
                    Intent IntentContacts = new Intent(Intent.ACTION_VIEW,  Uri.parse("content://contacts/people"));

                    //INTENT PARA CORREO RAPIDO
                    Intent IntentMailTo = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));

                    //INTENT MAIL COMPLETO
                    Intent mail = new Intent(Intent.ACTION_SEND, Uri.parse(email));
                    mail.setType("plain/text");
                    mail.putExtra(Intent.EXTRA_SUBJECT, "Mail's title");
                    mail.putExtra(Intent.EXTRA_TEXT, "Hi there, I love MyForm app, but...");
                    mail.putExtra(Intent.EXTRA_EMAIL, new String[] {"fernando@gmail.com", "antonio@gmail.com"});

                    startActivity(Intent.createChooser(mail, "Eliga el cliente de correo"));

                    //Telefono parte 2
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 666111666"));

                    //startActivity(mail);

                }

            }
        });

        // Abrir Camara
        imgBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamera, PICTURE_FROM_CAMERA);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICTURE_FROM_CAMERA :

                if(resultCode == Activity.RESULT_OK) {
                    String result = data.toUri(0);
                    Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Hubo un error con la imagen, intentelo de nuevo.", Toast.LENGTH_LONG).show();
                }

                break;

            default :
                super.onActivityResult(requestCode, resultCode, data);
        }
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
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
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
