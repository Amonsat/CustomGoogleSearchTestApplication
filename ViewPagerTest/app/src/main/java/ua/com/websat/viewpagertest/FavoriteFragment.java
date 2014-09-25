package ua.com.websat.viewpagertest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sat on 24.09.2014.
 */
public class FavoriteFragment extends Fragment {

    private ItemSearchAdapter itemFavoriteAdapter;
    private ArrayList<SearchItem> favoriteItems;

    public void setData(ArrayList<SearchItem> items) {
        this.favoriteItems = items;
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

//        ArrayList<SearchItem> favoriteItems = new ItemSearchAdapter(this.getActivity(), searchItems).getFavorites();
        itemFavoriteAdapter = new ItemSearchAdapter(this.getActivity(), favoriteItems);


        ListView listView = (ListView) rootView.findViewById(R.id.listview_favorite);
        listView.setAdapter(itemFavoriteAdapter);

        return rootView;
    }
}
