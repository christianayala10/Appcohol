package com.example.linux1.appcohol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class PopupEliminarCuenta extends AppCompatActivity {

    /* Elementos del layout */
    private Button bt_si;
    private Button bt_no;

    /* Variables */
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_eliminar_cuenta);

        /* Relacionar los elementos */
        bt_no = (Button) findViewById(R.id.bt_eliminarcuenta_no);
        bt_si = (Button) findViewById(R.id.bt_eliminarcuenta_si);

        /* Eliminar cuenta */
        bt_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = ParseUser.getCurrentUser();
                user.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText( PopupEliminarCuenta.this, "La cuenta ha sido eliminada", Toast.LENGTH_SHORT).show();
                            user.logOut();
                            setResult(1);
                            finish(); //Para volver
                        } else {
                            Toast.makeText( PopupEliminarCuenta.this, "No ha sido posible eliminar la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        /* Volver */
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(2);
                finish();
            }
        });

    }
}