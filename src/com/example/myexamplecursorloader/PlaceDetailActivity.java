
package com.example.myexamplecursorloader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.TextView;

import com.example.myexamplecursorloader.data.Place;
import com.example.myexamplecursorloader.data.PlaceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * CursorLoaderを使ってテーブルの詳細画面を表示します
 */
public class PlaceDetailActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private List<Place> mPlace;
    private String mPlaceID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_detail);
        Intent intent = getIntent();
        mPlaceID = intent.getStringExtra("place_id");

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // 検索条件の指定
        Uri uri = Uri.parse(PlaceManager.Place.CONTENT_URI + "/" + mPlaceID);
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.getCount() == 0) {
            mPlace = null;
            return;
        }
        mPlace = new ArrayList<Place>();
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setID(cursor.getInt(0));
                place.setPlaceID(cursor.getString(1));
                place.setPlace(cursor.getString(2));
                place.setUrl(cursor.getString(3));
                // Adding to list
                mPlace.add(place);
            } while (cursor.moveToNext());
        }

        viewSet();
    }

    private void viewSet() {
        Place place = mPlace.get(0);

        TextView view = (TextView) findViewById(R.id.id);
        view.setText("データ詳細画面\n\nid=" + place.getID());
        TextView view2 = (TextView) findViewById(R.id.place_id);
        view2.setText("place_id=" + place.getPlaceID());
        TextView view3 = (TextView) findViewById(R.id.place);
        view3.setText("place=" + place.getPlace());
        TextView view4 = (TextView) findViewById(R.id.url);
        view4.setText("url=" + place.getUrl());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
