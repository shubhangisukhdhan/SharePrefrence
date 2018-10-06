package shubhangi.shareprefernce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] sms = null;
        String message ="";

        if (bundle != null){


            Object[] pdus = (Object[])bundle.get("pdus");
            sms = new SmsMessage[pdus.length];
            for (int i = 0;i< sms.length;i++){
                sms[i]= SmsMessage.createFromPdu((byte[])pdus[i]);

                message +="\r\nMessage:";
                message+= sms[i].getMessageBody().toString();
                message+="\r\n";

                String sender = sms[i].getOriginatingAddress();
                message+=sender;


            }

            Intent smsIntent = new Intent("sms");
            smsIntent.putExtra("sms",message);
            LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

        }


    }
}
