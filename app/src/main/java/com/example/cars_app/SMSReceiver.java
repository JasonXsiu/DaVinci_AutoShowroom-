package com.example.cars_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";
    /*
     * This method 'onReceive' will be invoked with each new incoming SMS
     * */
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * Use the Telephony class to extract the incoming messages from the intent
         * */
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();

            /*
             * Now, for each new message, send a broadcast contains the new message to MainActivity
             * The MainActivity has to tokenize the new message and update the UI
             * */
            //Here you declare Intent to tell Android that there is an upcoming event to come
            Intent msgIntent = new Intent();

            //which type of message that are going to send? (tune the radio channel)
            msgIntent.setAction(SMS_FILTER);

            //Add extended data to the intent.
            msgIntent.putExtra(SMS_MSG_KEY, message);

            //Broadcast the given intent to all interested BroadcastReceivers.
            context.sendBroadcast(msgIntent);
        }
    }
}

