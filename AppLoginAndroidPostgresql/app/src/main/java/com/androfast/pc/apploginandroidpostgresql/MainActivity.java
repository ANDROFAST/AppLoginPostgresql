package com.androfast.pc.apploginandroidpostgresql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private EditText correo, contraseña;
    private Button login, registrar;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://192.168.8.133/postgresql/login/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.etxtEmail);
        contraseña = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);
        registrar = (Button) findViewById(R.id.btnRegistro);

        login.setOnClickListener(this);
        registrar.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnLogin:
                new IniciarSesion().execute();
                break;
            case R.id.btnRegistro:
                Intent i = new Intent(this, Registrar.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    class IniciarSesion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String Ccorreo = correo.getText().toString();
            String Cpassword = contraseña.getText().toString();
            try {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("email", Ccorreo));
                params.add(new BasicNameValuePair("contrasena", Cpassword));

                Log.d("solicitud!", "Empezando");
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);
                Log.d("Login", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("sesión correcta!", json.toString());
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(MainActivity.this);
                    Editor edit = sp.edit();
                    edit.putString("persona", Ccorreo);
                    edit.commit();

                    Intent i = new Intent(MainActivity.this, Mostrar.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Fallo Login!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}