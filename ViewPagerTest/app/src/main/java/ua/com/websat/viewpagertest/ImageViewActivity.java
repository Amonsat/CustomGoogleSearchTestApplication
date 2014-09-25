package ua.com.websat.viewpagertest;

import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;


public class ImageViewActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_view);
        Bundle extras = getIntent().getExtras();
        String image = extras.getString("image");
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(image);
    }
}
