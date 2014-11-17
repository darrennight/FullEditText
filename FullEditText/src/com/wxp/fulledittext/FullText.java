package com.wxp.fulledittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
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

	@Override
	protected void onDraw(Canvas canvas) {
		int lines = (int) (mFullTextHeight / mLineHeight);
		for (int i = 0; i < lines; i++) {
			canvas.drawLine(0, getTop() + mLineHeight + i * mLineHeight,
					mFullTextWidth, getTop() + mLineHeight + i * mLineHeight,
					mPaint);
		}
		super.onDraw(canvas);
	}

	public void init(Context context) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, getTextSize(), getResources()
						.getDisplayMetrics()));
		mSpaceWidth = mPaint.measureText(" ")/1.5f;
		mLineHeight = getLineHeight();
		FontMetrics fontMetrics = mPaint.getFontMetrics();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		mFullTextWidth = widthSize;

		mFullTextHeight = heightSize;

		setMeasuredDimension(mFullTextWidth, mFullTextHeight);
	}

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
	public void addSpaceBefore() {

	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {

		//mSpaceString = getText().toString();
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getText().delete(0, getText().length());
			if (mFirstDown) {
				mClickPosX = event.getX();
				mClickPosY = event.getY();
				mSpaceCount = (int) (mClickPosX / mSpaceWidth);
				mClickLine = (int) (mClickPosY / mLineHeight);
				if (mClickLine > mLastClickLine) {

					// 初始化当前位置之上的所有行
					for (int i = mLastClickLine; i < mClickLine; i++) {
						mSpaceString += "\n";
					}
					mLastClickLine = mClickLine;
				} else {

				}
				if (mClickLine >= mLastClickLine) {
					for (int i = 0; i < mSpaceCount; i++) {
						mSpaceString += " ";
					}
				}

				mFirstDown = false;
			}

			break;

		case MotionEvent.ACTION_UP:
			if (mClickLine > mLastClickLine) {
				getText().insert(0, mSpaceString);
			}
			
			mFirstDown = true;
			break;

		default:
			break;

		}

		// invalidate();

		return super.onTouchEvent(event);
	}

}
