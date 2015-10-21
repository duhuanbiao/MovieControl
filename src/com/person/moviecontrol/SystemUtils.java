package com.person.moviecontrol;

import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * SystemUtils
 * 
 * @author Trinea 2013-5-15
 */
public class SystemUtils {

    /** recommend default thread pool size according to system available processors, {@link #getDefaultThreadPoolSize()} **/
    public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

    /**
     * get recommend default thread pool size
     * 
     * @return if 2 * availableProcessors + 1 less than 8, return it, else return 8;
     * @see {@link #getDefaultThreadPoolSize(int)} max is 8
     */
    public static int getDefaultThreadPoolSize() {
        return getDefaultThreadPoolSize(8);
    }

    /**
     * get recommend default thread pool size
     * 
     * @param max
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }
    
	/**
	 * @param context: 涓婁笅鏂囧叧绯�
	 * @return
	 * 		鑾峰彇灞忓箷澶у皬
	 */
	public static Rect getScreenSize(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return new Rect(0, 0, display.getWidth(), display.getHeight());
	}
}
