package com.example.demovisanet;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lib.visanet.com.pe.visanetlib.VisaNet;
import lib.visanet.com.pe.visanetlib.presentation.custom.VisaNetViewAuthorizationCustom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public String generatePurchaseNumber() {
        Long time = (new Date().getTime())/100000;
        return time.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button botonComprar = findViewById(R.id.btncomprar);
        final Switch sLogo = findViewById(R.id.sLogo);
        final Switch sPagar = findViewById(R.id.sPagar);
        final TextView tAmount = findViewById(R.id.tAmount);
        final TextView tUserToken = findViewById(R.id.tUserToken);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("EMAIL", tUserToken.getText().toString());

                Map<String, Object> data = new HashMap<>();

                data.put(VisaNet.VISANET_CHANNEL, VisaNet.Channel.MOBILE);
                data.put(VisaNet.VISANET_COUNTABLE, true);
                data.put(VisaNet.VISANET_USERNAME, "integraciones.visanet@necomplus.com");
                data.put(VisaNet.VISANET_PASSWORD, "d5e7nk$M");
                data.put(VisaNet.VISANET_MERCHANT, "100128038");
                data.put(VisaNet.VISANET_PURCHASE_NUMBER, generatePurchaseNumber());
                data.put(VisaNet.VISANET_AMOUNT, tAmount.getText());
                data.put(VisaNet.VISANET_SHOW_AMOUNT, sPagar.isChecked());
                data.put(VisaNet.VISANET_USER_TOKEN, tUserToken.getText().toString());

                // Registro de MDD's
                HashMap<String, String> MDDdata = new HashMap<String, String>();
                // Consultar envío de MDD's
                MDDdata.put("4", "mail@domain.com");

                data.put(VisaNet.VISANET_MDD, MDDdata);

                // Especificación de endpoint (Ingreso obligatorio)
                data.put(VisaNet.VISANET_ENDPOINT_URL, "https://apitestenv.vnforapps.com/");

                VisaNetViewAuthorizationCustom custom = new VisaNetViewAuthorizationCustom();

                //Personalización 1: Configuración del nombre del comercio en el formulario de pago
                custom.setLogoTextMerchant(!sLogo.isChecked());
                    // Mostrar Nombre Del Comercio
                    custom.setLogoTextMerchantText("Merchant Name");
                    custom.setLogoTextMerchantTextColor(R.color.visanet_black);
                    custom.setLogoTextMerchantTextSize(50);
                    // Mostrar Logo Del Comercio
                    custom.setLogoImage(R.drawable.logo);

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

    // Respuesta
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
