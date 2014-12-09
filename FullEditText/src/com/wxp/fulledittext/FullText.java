package com.wxp.fulledittext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FullText extends EditText {

	Paint mPaint;
	boolean mFirstDown = true;
	float mClickPosX = 0;
	float mClickPosY = 0;
	int mSpaceCount = 0;
	float mSpaceWidth = 0;

	/*
	 * 点击的是第几行
	 */
	int mClickLine = 0;

	int mFullTextWidth = 0;
	int mFullTextHeight = 0;

	float mLineHeight = 0;

	int selection = 0;
	Editable editable;
	int mSelectSatrt = 0;

	int mMaxLines = 0;
	int tempStart = 0;
	int lineStart = 0;
	int lineCount = 0;
	int temp = 0;
	int dstart = 0;

	Toast mToast = null;
	Context mContext;

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

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				DBUG.e("受到消息");
				if (mToast == null) {
					mToast = Toast
							.makeText(mContext, "END", Toast.LENGTH_SHORT);
				} else {
					mToast.setText("END");
				}
				mToast.show();
			}
		};
	};

	public void init(Context context) {
		mContext = context;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setTextSize(getTextSize());
		mSpaceWidth = mPaint.measureText(" ");
		mLineHeight = getLineHeight();

		FontMetrics fontMetrics = mPaint.getFontMetrics();
		myHandler.post(new Runnable() {

			@Override
			public void run() {
				addTextChangedListener(new TextWatcher() {

					int startC = 0;
					int countC = 0;
					String stringB = "";

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						int lines = getLineCount();
						if (lines > mMaxLines) {
							myHandler.sendEmptyMessage(0);
							String str = s.toString();
							int cursorStart = getSelectionStart();
							int cursorEnd = getSelectionEnd();
							if (cursorStart == cursorEnd
									&& cursorStart < str.length()
									&& cursorStart >= 1) {
								getEditableText().delete(start, start + count);
							} else {
								str = str.substring(0, s.length() - 1);
								setText(str);
								setSelection(getText().length());
							}

						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

			}
		});

	}

	public void measureLine() {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		mFullTextWidth = widthSize;

		mFullTextHeight = heightSize;
		mMaxLines = (int) (mFullTextHeight / mLineHeight);
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
				mClickPosX = event.getX();
				mClickPosY = event.getY();
				mSpaceCount = (int) (mClickPosX / mSpaceWidth);
				mClickLine = (int) (mClickPosY / mLineHeight);
				if (mClickLine > mMaxLines) {
					mClickLine = mMaxLines;
				}
				mFirstDown = false;
			}

			if ((mClickLine + 1) > lineCount) {
				// 如果触摸区域所在行大于当前文本内容的行数
				setSelection(getText().length(), getText().length());
				for (int i = 0; i < (mClickLine + 1 - lineCount); i++) {
					// 就初始化之前的行（通过添加换行符）
					editable.append("\n");
				}
				// 接着还要初始化这一行之前的内容（通过添加空格）
				lineStart = getOffsetForPosition(0, mClickPosY);
				mSelectSatrt = getSelectionStart();
				while (mPaint.measureText(editable.toString(), lineStart,
						mSelectSatrt) < mClickPosX) {
					editable.append(" ");
					mSelectSatrt++;
				}
				return super.onTouchEvent(event);
			} else {
				// 如果触摸区域刚好处在当前内容内部
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				setSelection(woqu, woqu);
				// 接着在ACTION_UP中处理
				if ((mClickLine + 1) == lineCount) {
					dstart = mSelectSatrt - lineStart;
					setSelection(woqu, woqu);
					temp = 0;
					myHandler.post(new Runnable() {
						@Override
						public void run() {
							while (mPaint.measureText(editable.toString(),
									lineStart, lineStart + dstart + temp) < mClickPosX) {
								editable.append(" ");
								temp++;
							}
						}
					});
					return super.onTouchEvent(event);
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			mSelectSatrt = getSelectionStart();
			if ((mClickLine + 1) == lineCount) {
				DBUG.e("in th lineCount...");
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				lineStart = getOffsetForPosition(0, mClickPosY);
				if (mClickLine == 0) {
					dstart = mSelectSatrt - lineStart;
					setSelection(woqu, woqu);
					temp = 0;
					myHandler.post(new Runnable() {
						@Override
						public void run() {
							while (mPaint.measureText(editable.toString(),
									lineStart, lineStart + dstart + temp) < mClickPosX) {
								editable.append(" ");
								temp++;
							}
						}
					});

				} else {
					if (mSelectSatrt <= woqu) {
						DBUG.e("in th content...mSelectSatrt:"+mSelectSatrt+"  woqu :"+woqu);
					} 
				}

			} else if ((mClickLine + 1) < lineCount) {
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				lineStart = getOffsetForPosition(0, mClickPosY);
				char b = getText().charAt(woqu - 1);
				char c = getText().charAt(woqu);
				String a = "\n";
				if (String.valueOf(b).equals(a) || String.valueOf(c).equals(a)) {
					//如果前一个是换行符或者后一个是换行符就在之前的位置插入空格
					setSelection(woqu, woqu);

					lineStart = getOffsetForPosition(0, mClickPosY);
					mSelectSatrt = getSelectionStart();

					while (mPaint.measureText(editable.toString(), lineStart,
							mSelectSatrt) < mClickPosX) {
						editable.insert(mSelectSatrt, " ");
						mSelectSatrt++;
					}

				} else {
					//否则不做操作
					DBUG.e("kai");
				}

			}

			mFirstDown = true;

			break;

		default:
			break;

		}

		return super.onTouchEvent(event);
	}

}
