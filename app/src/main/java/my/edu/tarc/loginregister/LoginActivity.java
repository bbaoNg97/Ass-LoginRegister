package my.edu.tarc.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    RequestQueue queue;
    private ProgressDialog pDialog;
    public static final String FIRSTNAME = "first name";
    public static final String LASTNAME = "last name";
    public static final String NRIC = "ic";
    public static final String DOB = "dob";
    public static final String EMAIL = "email";
    public static final String PHONENO = "phone No";
    public static final String SALARY = "salary";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TAG = "my.edu.tarc.LoginRegister";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextpassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        pDialog = new ProgressDialog(this);

        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        }


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

        try {
            if (!pDialog.isShowing())
                pDialog.setMessage("Logging in...");
            pDialog.show();
            makeServiceCall(this, getString(R.string.login_url), username, password);
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
                                    //get all data, because every activity have to use the data
                                    String firstName = jsonObject.getString("firstName");
                                    String lastName = jsonObject.getString("lastName");
                                    String IC = jsonObject.getString("IC");
                                    String dob = jsonObject.getString("dob");
                                    String email = jsonObject.getString("email");
                                    String phoneNo = jsonObject.getString("phoneNo");
                                    double salary = jsonObject.getDouble("salary");
                                    String userName = jsonObject.getString("username");
                                    String password = jsonObject.getString("password");

                                    Intent profileIntent = new Intent(LoginActivity.this, EditProfileActivity.class);

                                    //pass all these data to EditProfileActivity
                                    profileIntent.putExtra(FIRSTNAME, firstName);
                                    profileIntent.putExtra(LASTNAME, lastName);
                                    profileIntent.putExtra(NRIC, IC);
                                    profileIntent.putExtra(DOB, dob);
                                    profileIntent.putExtra(EMAIL, email);
                                    profileIntent.putExtra(PHONENO, phoneNo);
                                   profileIntent.putExtra(SALARY,salary);
                                    profileIntent.putExtra(PASSWORD, password);
                                    profileIntent.putExtra(USERNAME, userName);

                                    LoginActivity.this.startActivity(profileIntent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Invalid password or username");
                                    builder.setMessage("Invalid password or username. Please try again.").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!isConnected()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Connection Error");
                                builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                            } else
                                Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        }
                    }) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
