package com.example.linux1.appcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegistroActivity extends AppCompatActivity {

    /* Elementos del layout */
    private EditText et_usuario;
    private EditText et_pass;
    private EditText et_pass2;
    private EditText et_email;
    private CheckBox cb_mayor_edad;
    private Button bt_registrar;
    private Button bt_volver;

    /* Variables */
    private String usuario;
    private String pass;
    private String pass2;
    private String email;
    private Boolean mayor_edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        /* Relacionar los elementos */
        et_usuario = (EditText) findViewById(R.id.et_registro_usuario);
        et_pass = (EditText) findViewById(R.id.et_registro_pass);
        et_pass2 = (EditText) findViewById(R.id.et_registro_pass2);
        et_email = (EditText) findViewById(R.id.et_registro_email);
        cb_mayor_edad = (CheckBox) findViewById(R.id.cb_registro_mayoredad);
        bt_registrar = (Button) findViewById(R.id.bt_registro_registrar);
        bt_volver = (Button) findViewById(R.id.bt_registro_volver);

        /* Volver */
        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* Registrar */
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Obtener los campos */
                usuario = et_usuario.getText().toString();
                pass = et_pass.getText().toString();
                pass2 = et_pass2.getText().toString();
                email = et_email.getText().toString();
                mayor_edad = cb_mayor_edad.isChecked();

                /* Comprobar si se puede realizar el registro */
                if( (!pass.equals(pass2)) || (pass.equals("")) ){
                    Toast.makeText(RegistroActivity.this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                } else if( !mayor_edad ){
                    Toast.makeText(RegistroActivity.this, "Debes confirmar que eres mayor de edad", Toast.LENGTH_SHORT).show();
                } else if( usuario.equals("")) {
                    Toast.makeText(RegistroActivity.this, "Debes especificar un nombre de usuario", Toast.LENGTH_SHORT).show();
                } else {
                    /* Llevar a cabo el registro */
                    ParseUser user = new ParseUser();
                    user.setUsername(usuario);
                    user.setPassword(pass);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null ){
                                Toast.makeText(RegistroActivity.this, "Registrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                /* Tratamos de iniciar sesion con el usuario recien registrado */
                                ParseUser.logInInBackground(usuario, pass, new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if ( user != null ) {
                                            /* Sesion iniciada correctamente */
                                            Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
                                            startActivity(intent);
                                        } else {
                                            //Toast.makeText(RegistroActivity.this,"No ha sido posible iniciar sesion", Toast.LENGTH_SHORT ).show();
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(RegistroActivity.this, "El registro no ha podido efectuarse", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
