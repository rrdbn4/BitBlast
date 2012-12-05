package com.smulk.bitblast.activities;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.smulk.bitblast.R;

public class Debug extends Activity
{
  TextView someValue;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    setContentView(R.layout.debug);
    someValue = (TextView) findViewById(R.id.debugView1);
    super.onCreate(savedInstanceState);
  }
}
