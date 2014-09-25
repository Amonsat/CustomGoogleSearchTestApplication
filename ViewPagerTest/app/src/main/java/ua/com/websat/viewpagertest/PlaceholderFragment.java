package ua.com.websat.viewpagertest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sat on 24.09.2014.
 */
//public class PlaceholderFragment extends Fragment implements OnScrollListener{
public class PlaceholderFragment extends Fragment implements OnCheckedChangeListener {
    private ItemSearchAdapter itemSearchAdapter;
    private ArrayList<SearchItem> searchItems;
    private static final String ARG_ITEMS = "items";
    private View footer;
    private EditText editText;
    FetchSearchTask searchTask = new FetchSearchTask(getActivity());
    ListView listView;
    private FavoritesListener favoritesListener;

    public void setData(ArrayList<SearchItem> items) {
        this.searchItems = items;
    }

//    public static PlaceholderFragment newInstance(ArrayList<SearchItem> searchItems) {
      public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
//        Bundle args = new Bundle();
//        args.putExtra(ARG_ITEMS, searchItems);
//        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            favoritesListener = (FavoritesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " should implement " + FavoritesListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        searchItems = new ArrayList<SearchItem>();
        itemSearchAdapter = new ItemSearchAdapter(this.getActivity(), searchItems, this);

        listView = (ListView) rootView.findViewById(R.id.listview_search);
        footer = inflater.inflate(R.layout.listview_footer, null);
        listView.addFooterView(footer);
        listView.setAdapter(itemSearchAdapter);
        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

                if (loadMore && searchTask.getStatus() == AsyncTask.Status.FINISHED) {
                    searchTask = new FetchSearchTask(getActivity());
                    searchTask.execute(editText.getText().toString(), Integer.toString(totalItemCount + 1));
                }
            }
        });

        editText = (EditText) rootView.findViewById(R.id.editText);

        editText.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            itemSearchAdapter.clear();
                            searchTask = new FetchSearchTask(getActivity());
                            searchTask.execute(editText.getText().toString(), "1");
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SearchItem item = itemSearchAdapter.getItem((Integer) buttonView.getTag());
        item.setFavorite(isChecked);

        if (isChecked) {
            favoritesListener.add(item);
        } else {
            favoritesListener.remove(item);
        }
    }

    public void uncheckSearchItem(SearchItem item) {
        for (SearchItem i: searchItems)
            if(i.getTitle() == item.getTitle()) i.setFavorite(false);
        itemSearchAdapter.notifyDataSetChanged();
    }

//    public void update(ArrayList<SearchItem> searchItems) {
//        itemSearchAdapter.clear();
//        for (SearchItem item: searchItems) itemSearchAdapter.add(item);
//    }

    public class FetchSearchTask extends ua.com.websat.viewpagertest.FetchSearchTask {
        public FetchSearchTask(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchItem> result) {
            super.onPostExecute(result);
            if (result != null) {
//                itemSearchAdapter.clear();
//                for (SearchItem item: result) itemSearchAdapter.add(item);
                itemSearchAdapter.addAll(result);
                itemSearchAdapter.notifyDataSetChanged();

                ((MainActivity)getActivity()).setSearchData(result);   // ???

//                int index = listView.getFirstVisiblePosition();
//                int top = (listView.getChildAt(0) == null) ? 0 : listView.getChildAt(0).getTop();
//                listView.setSelectionFromTop(index, top);


//                ((MainActivity)getActivity()).setFavoriteData(itemSearchAdapter.getFavorites());
            }
//            searchItems

        }

    }
}

