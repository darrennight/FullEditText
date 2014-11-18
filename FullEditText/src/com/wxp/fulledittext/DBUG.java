package com.wxp.fulledittext;

import android.util.Log;

public class DBUG {
    public static final boolean DBUG=true;
    public static void e(String wxp){
    	if(DBUG){
    		Log.e("wxp", wxp);
    	}
    }
}
