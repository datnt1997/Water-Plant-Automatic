package kris.hazen.dev.fulliot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;

import kris.hazen.dev.fulliot.R;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    private LoadingButton mLoadingBtn, mReset;
    private View animateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoadingBtn = (LoadingButton)findViewById(R.id.btnLogin);
        mReset = (LoadingButton)findViewById(R.id.btnReset);
        edUsername = (EditText)findViewById(R.id.edUsername);
        edPassword = (EditText)findViewById(R.id.edPassword);

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUsername.setText("");
                edPassword.setText("");
                edUsername.requestFocus();
            }
        });

        animateView = findViewById(R.id.animate_view);
        mLoadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

    }

    private void checkLogin(){

        final String userName = edUsername.getText().toString();
        if(userName.length() == 0){
            edUsername.setError(getResources().getString(R.string.errorUsername));
            return;
        }

        final String password = edPassword.getText().toString();
        if(password.length() == 0){
            edPassword.setError(getResources().getString(R.string.errorPassword));
            return;
        }

        mLoadingBtn.startLoading();
        //send login request

        //demo
        edUsername.setEnabled(false);
        edPassword.setEnabled(false);
        mLoadingBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                edUsername.setEnabled(true);
                edPassword.setEnabled(true);
                if("admin".endsWith(userName) && "admin".equals(password)){
                    //login success
                    mLoadingBtn.loadingSuccessful();
                    mLoadingBtn.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
                        @Override
                        public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            mLoadingBtn.reset();
                        }
                    });
                }else{
                    mLoadingBtn.loadingFailed();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.wrongUsernameOrPass),Toast.LENGTH_SHORT).show();
                }
            }
        },3000);
    }


}
