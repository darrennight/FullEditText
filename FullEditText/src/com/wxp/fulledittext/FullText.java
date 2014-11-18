package com.wxp.fulledittext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.EditText;

public class FullText extends EditText {

	Paint mPaint;
	boolean mFirstDown = true;
	float mClickPosX = 0;
	float mClickPosY = 0;
	/*
	 * 在点击位置之前插入空格
	 */
	int mSpaceCount = 0;
	float mSpaceWidth = 0;
	String mSpaceString = "";
	/*
	 * 点击的是第几行
	 */
	int mClickLine = 0;
	/*
	 * 保存上一次点击的位置，防止重复初始话
	 */
	int mLastClickLine = 0;
	int mFullTextWidth = 0;
	int mFullTextHeight = 0;

	float mLineHeight = 0;

	int selection = 0;
	Editable editable;
	List<String> mContentList=new ArrayList<String>();
	String mclickContent="";
	int mSelectSatrt= 0;
	
	int mLines=0;
	 int tempStart=0;
	public FullText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FullText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public FullText(Context context) {
		super(context);
		init(context);

	}

	public void init(Context context) {
		DBUG.e("textsize"+getTextSize());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setTextSize(getTextSize());
		DBUG.e("mPainttextsize"+mPaint.getTextSize());
		mSpaceWidth = mPaint.measureText("2");
		mLineHeight = getLineHeight();
	
		
		FontMetrics fontMetrics = mPaint.getFontMetrics();
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	
         
	}
/*
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		mFullTextWidth = widthSize-15;

		mFullTextHeight = heightSize;
		mLines = (int) (mFullTextHeight/mLineHeight);
	for (int i = mLines; i >=0; i--) {
			getEditableText().append("\n");
		}
		setMeasuredDimension(mFullTextWidth, mFullTextHeight);
	}*/

	/**
	 * 初始化点击的位置之上的所有行
	 */
	public void addLineAbove(int line) {
		for (int i = 0; i < line; i++) {

		}
	}

	/**
	 * 在点击位置之前插入空格
	 */
	public String addSpaceBefore(int count) {
              String temp="";
              for (int i = 0; i < count; i++) {
				temp+="2";
			}
              return temp;
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		   super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 editable = getEditableText();
		tempStart = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			if (mFirstDown) {
				mSelectSatrt = getSelectionStart();
				mClickPosX = event.getX();
				mClickPosY = event.getY();
				mSpaceCount = (int) (mClickPosX / mSpaceWidth);
				mClickLine = (int) (mClickPosY / mLineHeight);
				mFirstDown = false;
	
			}

			break;

		case MotionEvent.ACTION_UP:
			Log.e("wxp", "mSelectSatrt:"+mSelectSatrt);
			
			
			editable.delete(mSelectSatrt, mSelectSatrt);
			while ((mPaint.measureText(editable.toString(), 0, mSelectSatrt+tempStart))<mClickPosX) {
				editable.append("2");			
				tempStart +=1;
			}
			DBUG.e("插入了"+tempStart);
			//setSelection(mSelectSatrt+tempStart);
			//setSelection(0);
		   
		   new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setSelection(mSelectSatrt+tempStart);
			}
		});
			
/*			if (mClickLine == 0) {
				int tempStart=0;					
				while ((mPaint.measureText(editable.toString(), 0, mSelectSatrt+tempStart))<mClickPosX) {
					editable.append("2");			
					tempStart +=1;
					//mContentList.add(0, );
				}
			}*/
			
			
			
/*			if (mClickLine == 0&&mLastClickLine==0) {
				int singlelinelength=getText().length();
				
				if (mSelectSatrt>=singlelinelength) {
					int tempStart=0;					
					while ((mPaint.measureText(editable.toString(), 0, mSelectSatrt+tempStart))<mClickPosX) {
						editable.append("2");			
						tempStart +=1;
					}
					if ((mPaint.measureText(editable.toString()+"2", 0, mSelectSatrt+1))>=mFullTextWidth) {
						editable.append("\n");		
					}
					
					mLastClickLine=0;
				}else {
					
				}
				
					
			} else if(mClickLine>mLastClickLine){
				for (int i = mLastClickLine; i < mClickLine; i++) {
					editable.append("\n");
				}				
				Log.e("wxp", "mClickLine:"+mClickLine+ "  mLastClickLine"+mLastClickLine);

				editable.append(addSpaceBefore(mSpaceCount));
				mLastClickLine = mClickLine;
			} else if (mClickLine==mLastClickLine&&mClickLine!=0) {
				int selectsatrt=getSelectionStart();
				int linestart = getTheLineStart(selectsatrt);
				Log.e("wxp", "这一行开始："+linestart);
				//editable.insert(linestart+1, "wxp");
				
				int tempStart=0;				
				while ((mPaint.measureText(editable.toString(), linestart, mSelectSatrt+tempStart))<mClickPosX) {
					editable.append("2");			
					tempStart +=1;
				}
				//mLastClickLine = mClickLine;
			}*/
			
		
			Log.e("wxp", "getText:"+getText().toString());
			mFirstDown = true;
			break;

		default:
			break;

		}

		// invalidate();

		return super.onTouchEvent(event);
	}

	
	/**
	 * 通过检测换行符号来判断光标点击的这一行开头的位置
	 * @return
	 */
	public int getTheLineStart(int selectStart){
		int start=selectStart-1;
		Log.e("wxp", "当前字符是"+String.valueOf(editable.charAt(start)));
		while (!String.valueOf(editable.charAt(start)).equals("\n")) {
			start-=1;			
		}
		Log.e("wxp", "这一行开头位置是"+start);
		return start;
	}
	

	public void insertWordContent(int i,String w){
		Log.e("wxp","insertWordContent");
		if (mPaint.measureText(mContentList.get(i)+w)>mFullTextWidth) {
			mContentList.add(i,mContentList.get(i)+"\n");
			mContentList.add(i+1, w);
			mContentList.remove(mLines);
		} else {
			mContentList.add(i,mContentList.get(i)+w);
		}
		
		Log.e("wxp",mContentList.get(i));
	}
	public void insertLineContent(int i,String content){
		mContentList.add(i, content);
		mContentList.remove(mLines);
	}
	
	public int dpToPx(int dp){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
