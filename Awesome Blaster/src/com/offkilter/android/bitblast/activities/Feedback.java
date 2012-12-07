package com.offkilter.android.bitblast.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.offkilter.android.bitblast.data.PrefKeys;
import com.offkilter.android.bitblast.R;

public class Feedback extends Activity implements OnClickListener
{
  Button send;
  EditText name, email, subject, message;
  private final String EMAIL_ADDRESS = "robert@dunnrightsoftware.com";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.feedback);

    send = (Button) findViewById(R.id.bSend);
    name = (EditText) findViewById(R.id.etName);
    email = (EditText) findViewById(R.id.etEmail);
    subject = (EditText) findViewById(R.id.etSubject);
    message = (EditText) findViewById(R.id.etMessage);

    send.setOnClickListener(this);

    SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY,
        Context.MODE_WORLD_WRITEABLE);
    if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }

  @Override
  public void onClick(View v)
  {
    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setType("plain/text");
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { EMAIL_ADDRESS });
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BitBlast Contact: " + subject.getText().toString());
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message.getText().toString() + "\n--\n" + email.getText().toString());

    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    finish();
  }

}
