package my.edu.tarc.loginregister;

import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private TextView textViewUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUsername=(TextView)findViewById(R.id.textViewFirstName);
        Intent intent=getIntent();
        String name=intent.getStringExtra(LoginActivity.LASTNAME);
        textViewUsername.setText(name);
    }
}
