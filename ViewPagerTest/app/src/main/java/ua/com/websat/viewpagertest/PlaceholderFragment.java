package ua.com.websat.viewpagertest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sat on 24.09.2014.
 */
public class PlaceholderFragment extends Fragment{
    private ItemSearchAdapter itemSearchAdapter;
    private ArrayList<SearchItem> searchItems;
    private static final String ARG_ITEMS = "items";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        searchItems = new ArrayList<SearchItem>();
        itemSearchAdapter = new ItemSearchAdapter(this.getActivity(), searchItems);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_search);
        listView.setAdapter(itemSearchAdapter);

        final EditText editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            FetchSearchTask searchTask = new FetchSearchTask(getActivity());
                            searchTask.execute(editText.getText().toString());
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
                itemSearchAdapter.clear();
                for (SearchItem item: result) itemSearchAdapter.add(item);
                itemSearchAdapter.notifyDataSetChanged();
                ((MainActivity)getActivity()).setSearchData(result);
            }
//            searchItems

        }

    }
}

