
package com.example.myexamplecursorloader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.myexamplecursorloader.data.Place;
import com.example.myexamplecursorloader.data.PlaceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * CursorLoaderを使ってテーブルのデータをListViewに表示します
 * 
 * @author sakura
 */
public class PlaceListActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> { // ←これを実装する必要がある

    private SimpleCursorAdapter mAdapter;
    List<Place> mPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        // Loaderの初期化
        getSupportLoaderManager().initLoader(0, null, this);

        // ListViewにセットしたいテーブルのフィールドを配列にセット
        String[] from = new String[] {
                PlaceManager.Place.KEY_PLACE,
                PlaceManager.Place.KEY_URL
        };
        // ListViewの各ビューを配列にセット
        int[] to = new int[] {
                R.id.place,
                R.id.url
        };
        // アダプターに一行のレイアウトをセット
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null,
                from, to,
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // アダプターをListViewにセット
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                // 詳細画面の呼び出し
                Intent intent = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                Place p = mPlaces.get(position);
                intent.putExtra("place_id", p.getPlaceID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Loaderの廃棄
        getSupportLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // CursorLoader生成（検索条件の指定）
        return new CursorLoader(this, PlaceManager.Place.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Cursor old = mAdapter.swapCursor(cursor);
        if (old != null) {
            old.close();
        }
        if (cursor.getCount() == 0) {
            mPlaces = null;
            return;
        }
        mPlaces = new ArrayList<Place>();
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setID(cursor.getInt(0));
                place.setPlaceID(cursor.getString(1));
                place.setPlace(cursor.getString(2));
                place.setUrl(cursor.getString(3));
                // Adding to list
                mPlaces.add(place);
            } while (cursor.moveToNext());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Cursor old = mAdapter.swapCursor(null);
        if (old != null) {
            old.close();
        }
    }

}
