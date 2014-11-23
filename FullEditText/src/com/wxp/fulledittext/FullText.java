package com.wxp.fulledittext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences.Editor;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

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
	 int lineStart=0;
	 int lineCount = 0;
	 Handler mHandler;
	 int temp=0;
	 int dstart=0;
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
		mSpaceWidth = mPaint.measureText("w");
		mLineHeight = getLineHeight();
		mHandler = new Handler();
		
		FontMetrics fontMetrics = mPaint.getFontMetrics();
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
	
		
		OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				return false;
			}
		};
         
	}

	public void measureLine(){
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		mFullTextWidth = widthSize;

		mFullTextHeight = heightSize;
		mLines = (int) (mFullTextHeight/mLineHeight);

		setMeasuredDimension(mFullTextWidth, mFullTextHeight);
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
			   mSelectSatrt = getSelectionStart();
			   lineStart = getOffsetForPosition(0, mClickPosY);
			   lineCount = getLineCount();
			   if (mFirstDown) {	
					Log.e("wxp", "mFirstDown======"+mFirstDown);
					mClickPosX = event.getX();
					mClickPosY = event.getY();
					mSpaceCount = (int) (mClickPosX / mSpaceWidth);
					mClickLine = (int) (mClickPosY / mLineHeight);			
					mFirstDown = false;			
				}
			   if ((mClickLine+1)>lineCount) {
					setSelection(getText().length(), getText().length());
					for (int i = 0; i < (mClickLine+1-lineCount); i++) {
						editable.append("\n");
					}
					lineStart = getOffsetForPosition(0, mClickPosY);
					mSelectSatrt = getSelectionStart();
						while (mPaint.measureText(editable.toString(), lineStart, mSelectSatrt)<mClickPosX) {
							editable.append("0");
							mSelectSatrt++;
						}	
				  return super.onTouchEvent(event);
				} else   if ((mClickLine+1) == lineCount){
							int woqu =getOffsetForPosition(mClickPosX, mClickPosY);
							setSelection(woqu, woqu);
			    } else {
							int woqu =getOffsetForPosition(mClickPosX, mClickPosY);
							setSelection(woqu, woqu);
							lineStart = getOffsetForPosition(0, mClickPosY);
							 mSelectSatrt = getSelectionStart();
							
								while (mPaint.measureText(editable.toString(), lineStart, mSelectSatrt)<mClickPosX) {
									editable.insert(mSelectSatrt,"0");
									mSelectSatrt++;
								}
								return super.onTouchEvent(event);
						}
			break;

		case MotionEvent.ACTION_UP:
			 mSelectSatrt = getSelectionStart();
			 if ((mClickLine+1) == lineCount){
					int woqu =getOffsetForPosition(mClickPosX, mClickPosY);
					lineStart = getOffsetForPosition(0, mClickPosY);
					dstart = mSelectSatrt - lineStart;
					 setSelection(woqu, woqu);
					temp=0;
					 Log.e("wxp", "lineStart======"+lineStart+"       ,dstart====="+dstart);
					 mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							while (mPaint.measureText(editable.toString(), lineStart, lineStart+dstart+temp)<mClickPosX) {
								editable.append("0");
								temp++;							
						     }							
						}
					});
					return super.onTouchEvent(event);
				}
			 
			
			
/*			if ((mClickLine+1)>lineCount) {
				setSelection(getText().length(), getText().length());
			} else if ((mClickLine+1) == lineCount){
				setSelection(lineStart+mSelectSatrt, lineStart+mSelectSatrt);
			} else {
				setSelection(lineStart+mSelectSatrt, lineStart+mSelectSatrt);
			}*/
			mFirstDown = true;
			
			break;

		default:
			break;

		}

		// invalidate();

		
		return super.onTouchEvent(event);
	}
	
	public int getVOffset(){
		int height= 0;
		Class temp = TextView.class;
		try {
			Method method = temp.getMethod("getVerticalOffset", Boolean.class);
			height = (Integer)method.invoke(this, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return height;
	}
	
	private Handler myHandler =new Handler();

	public int dpToPx(int dp){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
