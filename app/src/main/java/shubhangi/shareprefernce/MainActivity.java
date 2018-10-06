package shubhangi.shareprefernce;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtName,edtAddress,edtPhone;
    Button btnSave,btnGet;
    TextView texMessage;

    SharedPreferences pref;
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";
    private static final int REQUEST_PERMISSION_SMS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        texMessage = findViewById(R.id.texMessage);

        btnSave = findViewById(R.id.btnSave);
        btnGet = findViewById(R.id.btnGet);

        pref= getApplicationContext().getSharedPreferences("My App",MODE_PRIVATE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},
                    REQUEST_PERMISSION_SMS);
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String name = edtName.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                SharedPreferences.Editor editor = pref.edit();

                editor.putString(NAME,name);
                editor.putString(ADDRESS,address);
                editor.putString(PHONE,phone);
                editor.apply();
            }
        });

    }
    public void GetData(View view){

        String name = pref.getString(NAME,"");
        String address = pref.getString(ADDRESS,"");
        String phone = pref.getString(PHONE,"");

        edtName.setText(name);
        edtAddress.setText(address);
        edtPhone.setText(phone);

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_SMS){

            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            }
            else {

                Toast.makeText(this,"This permission mandatary",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.READ_SMS},REQUEST_PERMISSION_SMS);
            }
        }
    }

    protected void onResume() {

        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReciver,new IntentFilter("sms"));
    }
    protected void onPause() {

        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReciver);

    }


    private BroadcastReceiver smsReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sms = intent.getStringExtra("sms");
            texMessage.setText(sms);

        }
    };


}
