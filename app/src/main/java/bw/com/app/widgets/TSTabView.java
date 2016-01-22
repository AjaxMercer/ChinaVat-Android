package bw.com.app.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bw.com.app.R;


public class TSTabView extends LinearLayout {

	private ImageView bar;
	private int barHeight;
	private int barWidth;
	private int startX;
	private OnTabClickListener listener;
	private ImageView tabActivity;
	private ImageView tabConnection;
	private ImageView tabFind;
	private ImageView tabProfile;
	
	public interface OnTabClickListener {
		void onTabClick(int index);
	}
	
	public TSTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public TSTabView(Context context) {
		super(context);
		initView();
	}
	
	public void setOnTabClickListener(OnTabClickListener listener) {
		this.listener = listener;
	}
	
	private void initView() {
		inflate(getContext(), R.layout.ts_tab_view, this);
		bar = (ImageView) findViewById(R.id.indicator_bar);
		bar.setBackgroundDrawable(barDrawable);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		barWidth = dm.widthPixels / 4;
		barHeight = bar.getMeasuredHeight();
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				barHeight = bar.getMeasuredHeight();
			}
		});
		initTabClick();
	}
	
	private void initTabClick() {
		tabActivity = (ImageView) findViewById(R.id.tab_activity);
		tabConnection = (ImageView) findViewById(R.id.tab_connections);
		tabFind = (ImageView) findViewById(R.id.tab_find);
		tabProfile = (ImageView) findViewById(R.id.tab_profile);
		

	}
	
	public void onPageSelected(int index) {
		resetTabBg();

	}
	
	private void resetTabBg() {
		Handler handler = new Handler(Looper.getMainLooper());
		tabActivity.setImageResource(R.drawable.navicon_activity_normal);
		tabConnection.setImageResource(R.drawable.navicon_connection_normal);
		tabFind.setImageResource(R.drawable.navicon_find_normal);
		tabProfile.setImageResource(R.drawable.navicon_profile_normal);
	}
	
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		startX = position * barWidth + (int)(positionOffset * barWidth);
		bar.invalidate();
	}
	
	private Drawable barDrawable = new Drawable() {
		
		private Paint paint;
		@Override
		public void setColorFilter(ColorFilter cf) {
		}
		
		@Override
		public void setAlpha(int alpha) {
		}
		
		@Override
		public int getOpacity() {
			return 0;
		}
		
		@Override
		public void draw(Canvas canvas) {
			if (paint == null) {
				paint = new Paint();
				paint.setColor(getResources().getColor(R.color.highlight_normal));
			}
			canvas.drawRect(new Rect(startX, 0, barWidth + startX, barHeight), paint);
		}
	};
}
