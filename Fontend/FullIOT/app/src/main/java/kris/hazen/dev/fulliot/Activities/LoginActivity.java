package kris.hazen.dev.fulliot.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kris.hazen.dev.fulliot.R;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btLogin, btReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = (EditText)findViewById(R.id.edUsername);
        edPassword = (EditText)findViewById(R.id.edPassword);
        btLogin = (Button)findViewById(R.id.btnLogin);
        btReset = (Button)findViewById(R.id.btnReset);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUsername.setText("");
                edPassword.setText("");
                edUsername.requestFocus();
            }
        });

    }
}
