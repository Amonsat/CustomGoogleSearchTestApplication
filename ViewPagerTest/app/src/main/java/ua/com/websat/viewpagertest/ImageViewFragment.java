package ua.com.websat.viewpagertest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Sat on 24.09.2014.
 */
public class ImageViewFragment extends Fragment {
    private static final String ARG_POSITION = "section_number";
    ImageManager imageManager = new ImageManager();

    public ImageViewFragment() {
        // Required empty public constructor
    }

    public void showImage(String image) {
//        new DownloadImageTask((ImageView) getView().findViewById(R.id.imageView)).execute(image);
        imageManager.fetchImage(this.getActivity(), 3600, image, ((ImageView)getView().findViewById(R.id.imageView)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_view, null);
        return view;
    }

    public static ImageViewFragment newInstance(int position) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
}
