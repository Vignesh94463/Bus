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
    private EditText otpTextBox;

    private TextView resendTimerText;
    private static  final long Start_Time = 60000;
    private CountDownTimer mcountDownTimer;
    private boolean resendTimerRunning;
    private  String mobileNumber,userStatus;

    private long mTimeLeft = Start_Time;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_otp);


        resendTimerText = findViewById(R.id.timerText);
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
        otpTextBox = findViewById(R.id.enterOtp);
        mobileNumber = getIntent().getStringExtra("mobileNumber");

        userStatus=getIntent().getStringExtra("userStatus");

        sendVerificationCode(mobileNumber);

        findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otpTextBox.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){

                    otpTextBox.setError("enter code....");
                    otpTextBox.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }


    private void startTimer() {

        resendTimerText.setOnClickListener(null);
        mcountDownTimer = new CountDownTimer(mTimeLeft,1000) {

            @Override
            public void onTick(long l) {
                mTimeLeft = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                resendTimerText.setText("Resend code");
                sendVerificationCode(mobileNumber);
                resendTimerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTimeLeft = Start_Time;
                        startTimer();
                        sendVerificationCode(mobileNumber);
                        Toast.makeText(VerifyPhoneOtp.this,"Resending Code",Toast.LENGTH_LONG).show();
                    }
                });


            }
        }.start();
        resendTimerRunning = true;
    }

    private void updateCountDownText(){
        int minutes = (int) mTimeLeft / 1000 / 60;
        int seconds = (int) mTimeLeft / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        resendTimerText.setText(timeLeftFormatted);

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
                    if(userStatus.equals("guardian")){

                        Intent intent = new Intent(VerifyPhoneOtp.this,TabBottomParent.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                    else if(userStatus.equals("driver")) {

                        Intent intent = new Intent(VerifyPhoneOtp.this,DriverDashBoard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("driverMobileNo",mobileNumber);

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
