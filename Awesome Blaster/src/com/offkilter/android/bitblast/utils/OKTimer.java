package com.offkilter.android.bitblast.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

//OKTimer (Off Kilter Timer)
//Author: Robert Dunn

public class OKTimer implements Runnable
{
  private Object target;
  private String selector;
  private Method invocation;
  private float interval = 0;
  private float counter = 0;
  
  public void setMethodToCall(Object targ, String method, float seconds)
  {
    interval = seconds;
    selector = method;
    target = targ;
    
    try
    {
      Class<?> cls = target.getClass();
      invocation = cls.getMethod(selector);
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
  }
  
  public void startTimer()
  {
    if(invocation != null)
    {
      Thread thread = new Thread(this);
      thread.start();
    }
    else
      Log.e("Timer", "invocation has not been initialized");
  }

  @Override
  public void run()
  {
    while(true)
    { 
      try
      {
        Thread.sleep(1);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      counter += 0.001f;
      if(counter >= interval)
      {
        counter = 0;
        invokeMethod();
      }
    }
    
  }
  
  private void invokeMethod()
  {
    try
    {
      invocation.invoke(target);
    }
    catch (IllegalArgumentException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
