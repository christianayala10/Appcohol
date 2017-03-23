package com.example.linux1.appcohol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RandomActivity extends AppCompatActivity {

    /* Elementos del layout */
    private Button bt_aleatorio;

    /* Variables */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        /* Relacionar los elementos */
        bt_aleatorio = (Button) findViewById(R.id.bt_random_aleatorio);


        /* Generar Cocktel aleatorio */
        bt_aleatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}