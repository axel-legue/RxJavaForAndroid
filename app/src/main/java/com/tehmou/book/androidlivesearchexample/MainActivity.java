package com.tehmou.book.androidlivesearchexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.search_results);
        listView.setAdapter(arrayAdapter);

        /*
         * The EditText "publishes" or "emits" an update, and the listener functions are subscribed to it.
         * This makes the listener functions "subscribers"
         *
         * An EditText can be considered an emitter of string values over time.
         * A producer like this in Rx terminology is called an "Observable" - because
         * the values it emits can be observed
         */
        EditText editText = (EditText) findViewById(R.id.edit_text);

        // The subscriber doesn't know where the data comes from and when, or how man times.
        // We can manipulate data as it's on the way o the subscriber.
        // For instance, we can disqualify data items based on certain conditions,
        // such as whether the string is at least three characters long.

        RxTextView.textChanges(editText)
                .filter(text -> text.length() >= 3)
                .debounce(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateSearchResults);

        /* You’ve added a thread change in the middle, because you need to be
         * sure that any operations that manipulate the UI are executed on the
         * main thread. In this case, the debounce operator will automatically
         * switch the thread to a background one in order to not block the
         * execution while you wait for more input. Most operators don’t switch
         * the thread, but ones that are delayed do.
         */
    }

    private void clearSearchResults() {
        arrayAdapter.clear();
    }

    private void updateSearchResults(CharSequence text) {
        // Create some random results
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("" + text + Math.random());
        }

        arrayAdapter.clear();
        arrayAdapter.addAll(list);
    }
}
