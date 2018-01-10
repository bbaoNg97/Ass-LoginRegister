package my.edu.tarc.loginregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangePwActivity extends AppCompatActivity {
    private Button buttonReset,buttonSave;
    private EditText editTextOldPw,editTextNewPw,editTextConfirmNewPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        //link UI
        buttonReset=(Button)findViewById(R.id.buttonReset);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        editTextConfirmNewPw=(EditText)findViewById(R.id.editTextConfirmNewPw);
        editTextNewPw=(EditText)findViewById(R.id.editTextNewPw);
        editTextOldPw=(EditText)findViewById(R.id.editTextOldPw);



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo save data
                Intent intent=new Intent(ChangePwActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void OnReset(View v) {
        editTextOldPw.setText("");
        editTextNewPw.setText("");
        editTextConfirmNewPw.setText("");

    }
}
