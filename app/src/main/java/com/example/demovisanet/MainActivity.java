package com.example.demovisanet;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import lib.visanet.com.pe.visanetlib.VisaNet;
import lib.visanet.com.pe.visanetlib.presentation.custom.VisaNetViewAuthorizationCustom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Busco el boton y lo assigno a una variable
        Button botonComprar= findViewById(R.id.btncomprar);
        //Luego le digo al boton que escuche cuando le dan click
        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cuando le des click al boton vas a mostrarle el formulario
                //asegurate que estas son os usuarios y passwords

                Map<String, Object> data = new HashMap<>();

                data.put(VisaNet.VISANET_CHANNEL, VisaNet.Channel.MOBILE);

                data.put(VisaNet.VISANET_COUNTABLE, true);

                data.put(VisaNet.VISANET_USERNAME, "integraciones.visanet@necomplus.com");

                data.put(VisaNet.VISANET_PASSWORD, "d5e7nk$M");

                data.put(VisaNet.VISANET_MERCHANT, "342062522");

                data.put(VisaNet.VISANET_PURCHASE_NUMBER, "1799");

                data.put(VisaNet.VISANET_AMOUNT, "15.22");

//Registro de MDDs complementarios (Ingreso opcional)
                HashMap<String, String> MDDdata = new HashMap<String, String>();
                MDDdata.put("19", "LIM");
                MDDdata.put("20", "AQP");
                MDDdata.put("21", "AFKI345");
                MDDdata.put("94", "ABC123DEF");

                data.put(VisaNet.VISANET_MDD, MDDdata);

                //Especificación de endpoint (Ingreso obligatorio)
                data.put(VisaNet.VISANET_END_POINT_URL, "https://apitestenv.vnforapps.com/");

                //Personalización (Ingreso opcional)
                VisaNetViewAuthorizationCustom custom = new VisaNetViewAuthorizationCustom();

                //Personalización 1: Configuración del nombre del comercio en el formulario de pago
                custom.setLogoTextMerchant(true);
                custom.setLogoTextMerchantText("Text logo");
                custom.setLogoTextMerchantTextColor(R.color.visanet_black);
                custom.setLogoTextMerchantTextSize(20);

                //Personalización 2: Configuración del color del botón pagar en el formulario de pago
                custom.setButtonColorMerchant(R.color.visanet_red);
                custom.setInputCustom(true);
                //En el MainActivity es el nombre del activity en  este caso es MainActivity
                try {
                    VisaNet.authorization(MainActivity.this, data, custom);
                } catch (Exception e) {
                    Log.i("Text", "onClick: " + e.getMessage());
                }
            }
        });



    }

    //ok ahora tenemos que hacer escuchar la respuesta en esta actividad
    //pare eso tienes que override el listener de la actividad

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VisaNet.VISANET_AUTHORIZATION) {
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    String JSONString = data.getExtras().getString("keySuccess");
                    Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    toast1.show();
                } else {
                    String JSONString = data.getExtras().getString("keyError");
                    JSONString = JSONString != null ? JSONString : "";
                    Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
            else {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Cancel...", Toast.LENGTH_LONG);
                toast1.show();
            }
        }
    }
}
