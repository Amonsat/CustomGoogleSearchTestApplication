package ua.com.websat.viewpagertest;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sat on 23.09.2014.
 */
public class FetchSearchTask extends AsyncTask<String, Void, ArrayList<SearchItem>> {
    private final String LOG_TAG = PlaceholderFragment.FetchSearchTask.class.getSimpleName();
    private Context context;

    public FetchSearchTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private ArrayList<SearchItem> getSearchDataFromJson(String searchJsonStr)
            throws JSONException {

        final String GCS_ITEMS = "items";
        final String GCS_ITEM_TITLE = "title";
        final String GCS_ITEM_PAGEMAP = "pagemap";
        final String GCS_ITEM_PAGEMAP_CSE_IMAGE = "cse_image";
        final String GCS_ITEM_PAGEMAP_CSE_THUMBNAIL = "cse_thumbnail";
        final String SOURCE = "src";


        JSONObject searchJson = new JSONObject(searchJsonStr);
        JSONArray searchArray = searchJson.getJSONArray(GCS_ITEMS);

        ArrayList<SearchItem> searchResult = new ArrayList<SearchItem>();
        try {

            for (int i = 0; i < searchArray.length(); i++) {
                JSONObject item = searchArray.getJSONObject(i);
                String itemTitle = item.getString(GCS_ITEM_TITLE);

                JSONObject pagemap = item.getJSONObject(GCS_ITEM_PAGEMAP);


                JSONArray cseImageArray = pagemap.optJSONArray(GCS_ITEM_PAGEMAP_CSE_IMAGE);
                String itemImage = null;
                if (cseImageArray != null) {
                    JSONObject cseImage = cseImageArray.getJSONObject(0);
                    itemImage = cseImage.getString(SOURCE);
                }

                JSONArray cseThumbnailArray = pagemap.optJSONArray(GCS_ITEM_PAGEMAP_CSE_THUMBNAIL);
                String itemThumbnail = null;
                if (cseThumbnailArray != null) {
                    JSONObject cseThumbnail = cseThumbnailArray.getJSONObject(0);
                    itemThumbnail = cseThumbnail.getString(SOURCE);
                }


                searchResult.add(new SearchItem(itemTitle, itemImage, itemThumbnail, false));
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error 4: " + e);
        }

        return searchResult;
    }

    @Override
    protected ArrayList<SearchItem> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String searchJsonStr = null;
        String key =  "AIzaSyBI1Yweby6Z9-lkNQCLGxjz-FJxr4rpybc";
        String cx = "000789169284849313434:1wvdvpjwkko";


        try {
            final String SEARCH_BASE_URL = "https://www.googleapis.com/customsearch/v1?";
            final String QUERY_PARAM = "q";
            final String CX_PARAM = "cx";
            final String KEY_PARAM = "key";
            final String START_INDEX = "start";


            Uri builtUri = Uri.parse(SEARCH_BASE_URL).buildUpon()
                    .appendQueryParameter(CX_PARAM, cx)
                    .appendQueryParameter(KEY_PARAM, key)
                    .appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(START_INDEX, params[1])
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = url.openStream();
            StringBuffer buffer = new StringBuffer();
            if (in == null) {
                return null;
            }
            reader = new BufferedReader((new InputStreamReader(in)));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            searchJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error 1:", e);
            return null;
        } catch(Exception e) {
            Log.e(LOG_TAG, "Error 2:", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getSearchDataFromJson(searchJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<SearchItem> result) {
    }
}
