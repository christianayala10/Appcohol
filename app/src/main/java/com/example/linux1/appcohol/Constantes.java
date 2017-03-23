package com.example.linux1.appcohol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 21/03/2017.
 */

public class Constantes {

    public static final String[] COMPONENTES_NOMBRES = {

            "Coca-Cola", "KAS limon"

    };



    public static final List<ComponenteDatos> COMPONENTES_DATOS = new ArrayList<ComponenteDatos>() {
        {
            add( new ComponenteDatos("Coca-Cola", 10, 100, "Dia") );
            add( new ComponenteDatos("KAS limon", 8, 222, "Caprabo"));
        }
    };
}
