package com.offkilter.android.bitblast.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//OKScheduler (Off Kilter Scheduler)
//Author: Robert Dunn


public class OKTimer implements Runnable
{
  private Thread thread;
  private Object target;
  private String method;
  private Method invoker;
  private float interval, elapsed;
  private boolean isRunning = false;

  //Calls the specified method once then kills the timer
  public void setMethod(Object target, String selector, float seconds)
  {
    this.target = target;
    method = selector;
    interval = seconds;
    try
    {
      Class<?> cls = target.getClass();
      invoker = cls.getMethod(method);
    }
    catch (SecurityException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    thread = new Thread(this);
    thread.start();
  }
  
  @Override
  public void run()
  {
    while(isRunning)
    {
      try
      {
        Thread.sleep(1);
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      elapsed += 0.001f;
      if(elapsed >= interval)
        invokeMethod();
    }  
  }
  
  private void invokeMethod()
  {
    elapsed = 0;
    interval = 0;
    isRunning = false;
    thread.stop();
    try
    {
      invoker.invoke(target);
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
