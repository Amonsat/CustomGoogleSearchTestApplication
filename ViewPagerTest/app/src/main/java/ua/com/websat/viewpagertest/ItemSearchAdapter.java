package ua.com.websat.viewpagertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sat on 23.09.2014.
 */
public class ItemSearchAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SearchItem> searchItems;
    ImageManager imageManager = new ImageManager();


    ItemSearchAdapter(Context context, ArrayList<SearchItem> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return searchItems.size();
    }

    @Override
    public SearchItem getItem(int position) {
        return searchItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = layoutInflater.inflate(R.layout.list_item_search, parent, false);

        SearchItem searchItem = getItem(position);

        ((TextView) view.findViewById(R.id.list_item_search_textview)).setText(searchItem.getTitle());

        if (searchItem.getThumbnail() != null) {
            ((ImageView) view.findViewById(R.id.list_item_search_imageview)).setOnClickListener(new OnImageClickListener(position));
            imageManager.fetchImage(this.context, 3600, searchItem.getThumbnail(), ((ImageView) view.findViewById(R.id.list_item_search_imageview)));
        }
//        new DownloadImageTask((ImageView) view.findViewById(R.id.list_item_search_imageview)).execute(searchItem.getThumbnail());


        CheckBox checkBox = (CheckBox) view.findViewById(R.id.list_item_search_checkbox);
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        checkBox.setTag(position);
        checkBox.setChecked(searchItem.getFavorite());

        return view;
    }

    SearchItem getSearchItem(int position) {
        return ((SearchItem) getItem(position));
    }

    ArrayList<SearchItem> getFavorites() {
        ArrayList<SearchItem> favorites = new ArrayList<SearchItem>();
        for (SearchItem item: searchItems) {
            if (item.getFavorite()) favorites.add(item);
        }
        return favorites;
    }

    void clear() {
        searchItems.clear();
    }

    void add(SearchItem searchItem) {
        searchItems.add(searchItem);
    }

    void addAll(ArrayList<SearchItem> items) {
//        this.clear();
        for (SearchItem item: items) this.add(item);
    }

    OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItem((Integer) buttonView.getTag()).setFavorite(isChecked);
//            Activity activity = (Activity)context;
//            ((MainActivity) activity).
        }
    };

    class OnImageClickListener implements OnClickListener {
        int position;

        public OnImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final Activity activity = (Activity) context;
            String image = getSearchItem(this.position).getImage();
            ImageViewFragment fragment = (ImageViewFragment)activity.getFragmentManager().findFragmentById(R.id.fragment_image_view);
            if (fragment != null && fragment.isInLayout()) {
                fragment.showImage(image);
            } else {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("image", image);
                context.startActivity(intent);
            }
        }
    }

}
