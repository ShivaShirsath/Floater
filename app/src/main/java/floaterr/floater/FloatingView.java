package floaterr.floater;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class FloatingView extends Service {

    WindowManager wm;
    LinearLayout ll;
	WindowManager.LayoutParams parameters;

    @Override public IBinder onBind(Intent intent) {
        return null;
    }
	private Drawable getDrawableWithRadius() {

		GradientDrawable gradientDrawable   =   new GradientDrawable();
		gradientDrawable.setCornerRadii(new float[]{
			25, // Top    Left  Start 
			25, // Top    Left  Top
			25, // Top    Right Top
			25, // Top    Right End
			25, // Bottom Right End
			25, // Bottom Right Bottom
			25, // Bottom Left  Bottom
			25  // Bottom Left  Start
		});
		gradientDrawable.setColor(Color.argb(128, 255, 128, 0));
		return gradientDrawable;
	}
	
    @Override public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        ll = new LinearLayout(this);

		ll.setGravity(Gravity.CENTER);

        parameters = new WindowManager.LayoutParams(
			100, //WindowManager.LayoutParams.WRAP_CONTENT, 
			100, //WindowManager.LayoutParams.WRAP_CONTENT, 
			WindowManager.LayoutParams.TYPE_PHONE, 
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, 
			PixelFormat.TRANSLUCENT
		);
        parameters.gravity = Gravity.CENTER;
		
        parameters.x = 0;
        parameters.y = 0;

		ll.setBackgroundColor(Color.parseColor("#ff8800"));
		ll.setPadding(25, 25, 25, 25);
		ll.setBackground(getDrawableWithRadius());
		//ll.addView(new View(this));

        wm.addView(ll, parameters);

        ll.setOnTouchListener(new View.OnTouchListener() {
				double x;
				double y;
				double pressedX;
				double pressedY;

				@Override public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							x = parameters.x; y = parameters.y;
							pressedX = event.getRawX(); pressedY = event.getRawY();
						break;

						case MotionEvent.ACTION_MOVE:
							parameters.x = (int) (x + (event.getRawX() - pressedX)); parameters.y = (int) (y + (event.getRawY() - pressedY));
							wm.updateViewLayout(ll, parameters);
						break;
					}
					return false;
				}
			});	
        ll.setOnLongClickListener(new View.OnLongClickListener() {
				@Override public boolean onLongClick(View v) {
					wm.removeView(ll);
					stopSelf();
					System.exit(0);
					return true;
				}
			});
    }
    @Override public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
