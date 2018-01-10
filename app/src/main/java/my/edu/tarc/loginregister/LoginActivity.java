package my.edu.tarc.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    public static final String LASTNAME = "LastName";
    RequestQueue queue;
    private ProgressDialog pDialog;

    public static final String TAG = "my.edu.tarc.LoginRegister";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextpassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
         pDialog=new ProgressDialog(this);
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        do{
            if (!isConnected()) {
                builder.setTitle("Connection Error");
                builder.setMessage("\tNo network.\nPlease try connect your network").setNegativeButton("Retry",null).create().show();


            }
        }while(isConnected());


        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);

            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLogin();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void OnLogin() {

        //store username and pw to compare with database
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        //todo: compare username and password
        try {
            if(!pDialog.isShowing())
                pDialog.setMessage("Logging in...");
            pDialog.show();
            makeServiceCall(this, getString(R.string.login_url), username,password);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final String username, final String password) {
        queue = Volley.newRequestQueue(context);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                if (success) {
                                    String lastName = jsonObject.getString("lastName");

                                    Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    MainIntent.putExtra(LASTNAME, lastName);
                                    LoginActivity.this.startActivity(MainIntent);
                                }
                                else {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Invalid password or username. Please try again.").setNegativeButton("Retry",null).create().show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
