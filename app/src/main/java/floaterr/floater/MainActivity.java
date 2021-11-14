package floaterr.floater;

public class MainActivity extends android.app.Activity {
    @Override protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		startService(new android.content.Intent(MainActivity.this, FloatingView.class));
		finish();
    }
}
