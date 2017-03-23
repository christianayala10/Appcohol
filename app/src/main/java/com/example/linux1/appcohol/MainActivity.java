package com.example.linux1.appcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linux1.appcohol.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    /* Elementos del layout */
    private EditText et_usuario;
    private EditText et_pass;
    private Button bt_login;
    private Button bt_registrar;

    /* Variables */
    private String usuario;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Relacionar los elementos */
        et_usuario = (EditText) findViewById(R.id.et_main_usuario);
        et_pass = (EditText) findViewById(R.id.et_main_pass);
        bt_login = (Button) findViewById(R.id.bt_main_login);
        bt_registrar = (Button) findViewById(R.id.bt_main_registrar);

        /* Registrar cuenta */
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        /* Iniciar sesion */
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = et_usuario.getText().toString();
                pass = et_pass.getText().toString();

                if( (usuario.equals("")) || (pass.equals("")) ){
                    Toast.makeText(MainActivity.this,"El usuario y la contraseña no pueden estar en blanco", Toast.LENGTH_SHORT ).show();
                } else {
                    ParseUser.logInInBackground(usuario, pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if ( user != null ) {
                                /* Sesion iniciada correctamente */
                                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this,"El usuario y la contraseña no concuerdan", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
