/*
 * Copyright sakura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myexamplecursorloader;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.myexamplecursorloader.data.Place;
import com.example.myexamplecursorloader.data.PlaceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 元のテストデータ消す
        getContentResolver().delete(PlaceManager.Place.CONTENT_URI, null, null);
        // テストデータ生成(本来はDB処理は非同期にやるのだがサンプルだし手抜き)
        List<Place> places = new ArrayList<Place>();
        Place place = new Place("1", "北海道", "http://www.pref.hokkaido.lg.jp/");
        places.add(place);
        place = new Place("2", "群馬", "http://www.pref.gunma.jp/");
        places.add(place);
        place = new Place("3", "岡山", "http://www.pref.okayama.jp/");
        places.add(place);
        place = new Place("4", "広島", "http://www.pref.hiroshima.lg.jp/");
        places.add(place);
        place = new Place("5", "香川", "http://www.pref.kagawa.lg.jp/");
        places.add(place);

        ContentValues values = new ContentValues();
        for (Place p : places) {
            values.clear();
            values.put(PlaceManager.Place.KEY_PLACE_ID, p.getPlaceID());
            values.put(PlaceManager.Place.KEY_PLACE, p.getPlace());
            values.put(PlaceManager.Place.KEY_URL, p.getUrl());
            getContentResolver()
                    .insert(PlaceManager.Place.CONTENT_URI, values);
        }

        // リスト画面呼び出し
        Intent intent = new Intent(getApplicationContext(), PlaceListActivity.class);
        startActivity(intent);
    }
}
