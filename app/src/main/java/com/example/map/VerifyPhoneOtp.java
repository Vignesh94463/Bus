package com.example.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;



public class VerifyPhoneOtp extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText editText;

    private TextView mtimerText;
    private static  final long Start_Time = 60000;
    private CountDownTimer mcountDownTimer;
    private boolean mTimerRunning;
    private  String phonenumber,userstatus;

    private long mTimeLeft = Start_Time;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_otp);



        mtimerText = findViewById(R.id.timerText);
        startTimer();
        imageView=(ImageView)findViewById(R.id.appicon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VerifyPhoneOtp.this,SendPhoneOtp.class);
                startActivity(intent);
                Toast.makeText(VerifyPhoneOtp.this,"work",Toast.LENGTH_LONG).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.enterOtp);
        phonenumber = getIntent().getStringExtra("phonenumber");

        userstatus=getIntent().getStringExtra("userstatus");

        sendVerificationCode(phonenumber);

        findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    editText.setError("enter code....");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }


    private void startTimer() {
        mtimerText.setOnClickListener(null);
        mcountDownTimer = new CountDownTimer(mTimeLeft,1000) {

            @Override
            public void onTick(long l) {
                mTimeLeft = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                mtimerText.setText("Resend code");
                sendVerificationCode(phonenumber);
                mtimerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTimeLeft = Start_Time;
                        startTimer();
                        sendVerificationCode(phonenumber);
                        Toast.makeText(VerifyPhoneOtp.this,"Resending Code",Toast.LENGTH_LONG).show();
                    }
                });


            }
        }.start();
        mTimerRunning = true;
    }

    private void updateCountDownText(){
        int minutes = (int) mTimeLeft / 1000 / 60;
        int seconds = (int) mTimeLeft / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        mtimerText.setText(timeLeftFormatted);

    }
    private  void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);

    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                   // Toast.makeText(VerifyPhoneOtp.this,userstatus,Toast.LENGTH_LONG).show();
                    if(userstatus.equals("false")){
                        Intent intent = new Intent(VerifyPhoneOtp.this,TabBottomParent.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(VerifyPhoneOtp.this,MapsActivityDriver.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }


                }else{
                    Toast.makeText(VerifyPhoneOtp.this,"error : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private  void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,59, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneOtp.this,"error : "+e.getMessage(),Toast.LENGTH_LONG).show();


        }
    };

}
