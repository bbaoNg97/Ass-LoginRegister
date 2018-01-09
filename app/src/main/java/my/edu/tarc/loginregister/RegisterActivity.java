package my.edu.tarc.loginregister;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextFirstName, editTextLastName;
    private EditText editTextIC1, editTextIC2, editTextIC3;
    private EditText editTextDob;
    private EditText editTextEmail;
    private EditText editTextHp1, editTextHp2;
    private EditText editTextSalary;
    private EditText editTextRegUsername, editTextRegPw, editTextConfirmPw;
    private Button buttonRegister;
    private ImageButton imageButton;
    private static final int PICK_IMAGE_REQUEST = 1;

    RequestQueue queue;
    List<User> usernameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameList = new ArrayList<>();
        //link UI
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextIC1 = (EditText) findViewById(R.id.editTextIC1);
        editTextIC2 = (EditText) findViewById(R.id.editTextIC2);
        editTextIC3 = (EditText) findViewById(R.id.editTextIC3);
        editTextDob = (EditText) findViewById(R.id.editTextDoB);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextHp1 = (EditText) findViewById(R.id.editTextHp1);
        editTextHp2 = (EditText) findViewById(R.id.editTextHp2);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);
        editTextRegUsername = (EditText) findViewById(R.id.editTextRegUsername);
        editTextRegPw = (EditText) findViewById(R.id.editTextRegPw);
        editTextConfirmPw = (EditText) findViewById(R.id.editTextConfrimPw);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        //redirect to gallery
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                //todo: select a photo
                Toast.makeText(getApplicationContext(), "Salary slip is successfully added.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void OnReset(View v) {
        editTextFirstName.setText("");
        editTextLastName.setText("");
        editTextIC1.setText("");
        editTextIC2.setText("");
        editTextIC3.setText("");
        editTextDob.setText("");
        editTextEmail.setText("");
        editTextHp1.setText("");
        editTextHp2.setText("");
        editTextSalary.setText("");
        editTextRegUsername.setText("");
        editTextRegPw.setText("");
        editTextConfirmPw.setText("");
    }

    public void OnRegister(View v) {
        getUsername(getApplicationContext(), getString(R.string.get_username_url));
        User user = new User();
        String password = editTextRegPw.getText().toString();
        String confirmPw = editTextConfirmPw.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String nric = "";
        nric = editTextIC1.getText().toString() + editTextIC2.getText().toString() + editTextIC3.getText().toString();
        String email = editTextEmail.getText().toString();
        String hp = "";
        hp = editTextHp1.getText().toString() + editTextHp2.getText().toString();
        String username = editTextRegUsername.getText().toString();
        String dob = editTextDob.getText().toString();

        if (!password.equals(confirmPw)) {
            Toast.makeText(getApplicationContext(), "Error: Password must same with confirm password!", Toast.LENGTH_LONG).show();
        } else {

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setNric(nric.toString());
            user.setDob(dob);
            user.setEmail(email);
            user.setPhoneNo(hp);
            user.setSalary(Double.parseDouble(editTextSalary.getText().toString()));
            user.setUsername(username);
            user.setPassword(password);

            try {
                makeServiceCall(this, getString(R.string.insert_user_url), user);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(LoginIntent);
        }


    }

    public void makeServiceCall(Context context, String url, final User user) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

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
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstName", user.getFirstName());
                    params.put("lastName", user.getLastName());
                    params.put("IC", user.getNric());
                    params.put("dob", user.getDob());
                    params.put("email", user.getEmail());
                    params.put("phoneNo", user.getPhoneNo());
                    params.put("salary", Double.toString(user.getSalary()));
                    params.put("username", user.getUsername());
                    params.put("password", user.getPassword());


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

    private void getUsername(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //everytime i listen to the server, i clear the list
                            usernameList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                //jason object that contains code,title,credit
                                String username = courseResponse.getString("username");
                                String title = courseResponse.getString("title");
                                String credit = courseResponse.getString("credit");
                                Course course = new Course(code, title, credit);
                                caList.add(course);
                            }
                            loadCourse();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
