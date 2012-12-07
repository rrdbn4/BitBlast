package com.offkilter.android.bitblast.utils;

import android.graphics.Bitmap;

public class BBButton
{
	private int minX, maxX, minY, maxY;
	private int top, left;
	
	public void create(int left, int top, Bitmap src)
	{
		this.minX = left;
		this.maxX = left + src.getWidth();
		this.minY = top;
		this.maxY = top + src.getHeight();
		this.left = left;
		this.top = top;
	}
	
	public void create(int minX, int maxX, int minY, int maxY)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public boolean isClicked(float x, float y)
	{
		return x > minX && x < maxX && y > minY && y < maxY;
	}
	
	public int getTop(){ return top; }
	public int getLeft(){ return left; }
}
