package my.edu.tarc.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class EditProfileActivity extends AppCompatActivity {
    private LinearLayout llEditProfile ;
    private Button buttonEdit,buttonSave,buttonChangePw;
    private ConstraintLayout llViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //link UI
        llViewProfile=(ConstraintLayout) findViewById(R.id.viewProfile);
        llEditProfile=(LinearLayout)findViewById(R.id.editProfile);
        buttonEdit=(Button)findViewById(R.id.buttonEdit);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonChangePw=(Button)findViewById(R.id.buttonChangePw);

        //hide the editProfile Layout, show the View Profile layout first
        llViewProfile.setVisibility(View.VISIBLE);
        llEditProfile.setVisibility(View.GONE);

        //get data first
        Intent getDataIntent=new Intent(EditProfileActivity.this,ChangePwActivity.class);
        String firstname=getDataIntent.getStringExtra(LoginActivity.FIRSTNAME);
        String lastname=getDataIntent.getStringExtra(LoginActivity.LASTNAME);
        String ic=getDataIntent.getStringExtra(LoginActivity.NRIC);
        String email=getDataIntent.getStringExtra(LoginActivity.EMAIL);
        String phoneNo=getDataIntent.getStringExtra(LoginActivity.PHONENO);
        double salary=getDataIntent.getDoubleExtra();
        Bundle bundle =getIntent().getExtras();
        if(bundle!=null){
            GetDataIntent.putExtras(bundle);
        }
        startActivity(GetDataIntent);


        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewProfile.setVisibility(View.GONE);
                llEditProfile.setVisibility(View.VISIBLE);

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewProfile.setVisibility(View.VISIBLE);
                llEditProfile.setVisibility(View.GONE);
            }
        });
        buttonChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditProfileActivity.this,ChangePwActivity.class);
                startActivity(intent);

            }
        });

    }

}
