package my.edu.tarc.loginregister;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private LinearLayout llEditProfile ;
    private Button buttonEdit,buttonSave,buttonChangePw;
    private ConstraintLayout llViewProfile;

    private TextView tvFirstName,tvLastName,tvIC,tvEmail,tvPhoneNo,tvSalary,tvDob;
    private EditText etFirstName,etLastName,etIC,etEmail,etPhoneNo,etSalary,etDob;
    public String firstname,lastname,IC,email,phoneNo,dob,username;
    public double salary;
    private ProgressDialog pDialog;
    RequestQueue queue;

    //todo:here
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);

        //link UI
        llViewProfile=(ConstraintLayout) findViewById(R.id.viewProfile);
        llEditProfile=(LinearLayout)findViewById(R.id.editProfile);
        buttonEdit=(Button)findViewById(R.id.buttonEdit);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonChangePw=(Button)findViewById(R.id.buttonChangePw);

        tvDob=(TextView)findViewById(R.id.textViewDob);
        tvEmail=(TextView)findViewById(R.id.textViewEmail);
        tvFirstName=(TextView)findViewById(R.id.textViewFirstName);
        tvIC=(TextView)findViewById(R.id.textViewIC);
        tvLastName=(TextView)findViewById(R.id.textViewLastName);
        tvPhoneNo=(TextView)findViewById(R.id.textViewPhoneNo);
        tvSalary=(TextView)findViewById(R.id.textViewSalary);

        etDob=(EditText) findViewById(R.id.editTextDob);
        etEmail=(EditText)findViewById(R.id.editTextEmail);
        etFirstName=(EditText)findViewById(R.id.editTextFirstName);
        etIC=(EditText)findViewById(R.id.editTextIC);
        etLastName=(EditText)findViewById(R.id.editTextLastName);
        etPhoneNo=(EditText)findViewById(R.id.editTextPhoneNo);
        etSalary=(EditText)findViewById(R.id.editTextSalary);

        //hide the editProfile Layout, show the View Profile layout first
        llViewProfile.setVisibility(View.VISIBLE);
        llEditProfile.setVisibility(View.GONE);

        //get data first
        Intent getDataIntent=getIntent();

        firstname=getDataIntent.getStringExtra(LoginActivity.FIRSTNAME);
        lastname=getDataIntent.getStringExtra(LoginActivity.LASTNAME);
        IC=getDataIntent.getStringExtra(LoginActivity.NRIC);
        email=getDataIntent.getStringExtra(LoginActivity.EMAIL);
        phoneNo=getDataIntent.getStringExtra(LoginActivity.PHONENO);
        salary=getDataIntent.getDoubleExtra(LoginActivity.SALARY,-1);
        dob=getDataIntent.getStringExtra(LoginActivity.DOB);
        username=getDataIntent.getStringExtra(LoginActivity.USERNAME);

        //set data to all textView
        tvFirstName.setText(firstname);
        tvLastName.setText(lastname);
        tvIC.setText(IC);
        tvSalary.setText("RM"+salary+"");
        tvEmail.setText(email);
        tvPhoneNo.setText(phoneNo);
        tvDob.setText(dob);

        //todo take it bae
/*
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
               // Log.d(RegisterActivity.TAG, "onDateSet: yyyy/mm/dd: " + month + "/" + day + "/" + year);

                String date = year + "/" + month + "/" + day;
                etDob.setText(date);
            }
        };
*/

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //turn to editProfileLayout
                llViewProfile.setVisibility(View.GONE);
                llEditProfile.setVisibility(View.VISIBLE);
                etFirstName.setText(firstname);
                etLastName.setText(lastname);
                etIC.setText(IC);
                etSalary.setText(salary+"");
                etEmail.setText(email);
                etPhoneNo.setText(phoneNo);
                etDob.setText(dob);

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //store data in variable
                firstname=etFirstName.getText().toString();
                lastname=etLastName.getText().toString();
                IC=etIC.getText().toString();
                String sal=etSalary.getText().toString();
                salary=Double.parseDouble(sal);
                email=etEmail.getText().toString();
                phoneNo=etPhoneNo.getText().toString();
                dob=etDob.getText().toString();

                //save data
                try {
                    if (!pDialog.isShowing())
                        pDialog.setMessage("Saving.....");
                    pDialog.show();
                    makeServiceCall(EditProfileActivity.this, getString(R.string.updateProfile_url), username);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent changePwIntent=new Intent(EditProfileActivity.this,ChangePwActivity.class);
                Bundle bundle =getIntent().getExtras();
                if(bundle!=null){
                    changePwIntent.putExtras(bundle);
                }
                startActivity(changePwIntent);

            }

        });

    }

    public void makeServiceCall(Context context, String url, final String username) {
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
                                int success = jsonObject.getInt("success");
                                String message=jsonObject.getString("message");
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                if (success==1) {//UPDATED success
                                    llViewProfile.setVisibility(View.VISIBLE);
                                    llEditProfile.setVisibility(View.GONE);
                                    //set data to all textView
                                    tvFirstName.setText(firstname);
                                    tvLastName.setText(lastname);
                                    tvIC.setText(IC);
                                    tvSalary.setText("RM"+salary+"");
                                    tvEmail.setText(email);
                                    tvPhoneNo.setText(phoneNo);
                                    tvDob.setText(dob);

                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                    builder.setTitle("Failed to update");
                                    builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
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
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstName", firstname);
                    params.put("lastName", lastname);
                    params.put("ic", IC);
                    params.put("dob", dob);
                    params.put("email", email);
                    params.put("phoneNo", phoneNo);
                    params.put("salary",salary+"");
                    params.put("username", username);

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
