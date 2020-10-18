package floaterr.floater;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
//import android.content.ClipboardManager;
import android.text.ClipboardManager;
import android.widget.*;

public class FloatingView extends Service{

    WindowManager wm;
    LinearLayout ll;
	TextView tv;
	Button paste, remove;
	String str="";

	ClipboardManager clipboard;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ll = new LinearLayout(this);
		
		paste = new Button(this);
		tv = new TextView(this);
		remove = new Button(this);
		
		ll.setGravity(Gravity.CENTER);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.CENTER;
        parameters.x = 0;
        parameters.y = 0;

		paste.setText("Paste");
		tv.setText("TEXT");
		remove.setText("Remove");

        ll.addView(paste);
		ll.addView(tv);
		ll.addView(remove);
		
        wm.addView(ll, parameters);

        remove.setOnTouchListener(new View.OnTouchListener() {
				WindowManager.LayoutParams updatedParameters = parameters;
				double x;
				double y;
				double pressedX;
				double pressedY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:

							x = updatedParameters.x;
							y = updatedParameters.y;

							pressedX = event.getRawX();
							pressedY = event.getRawY();

							break;

						case MotionEvent.ACTION_MOVE:
							updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
							updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

							wm.updateViewLayout(ll, updatedParameters);

							break;
					}

					return false;
				}
			});
			
		paste.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View p1){
				tv.setText(clipboard.getText().toString());
			}
		});
		
        remove.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					wm.removeView(ll);
					stopSelf();
					System.exit(0);

					return true;
				}
			});

		remove.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					clipboard.setText(tv.getText().toString().replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""));
				}
			});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

}
