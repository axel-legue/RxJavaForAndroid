package com.tehmou.book.androidlivesearchexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var textViewExercise: TextView

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        val listView = findViewById<ListView>(R.id.search_results)
        listView.adapter = arrayAdapter

        /*
         * The EditText "publishes" or "emits" an update, and the listener functions are subscribed to it.
         * This makes the listener functions "subscribers"
         *
         * An EditText can be considered an emitter of string values over time.
         * A producer like this in Rx terminology is called an "Observable" - because
         * the values it emits can be observed
         */
        val editText = findViewById<EditText>(R.id.edit_text)

        // The subscriber doesn't know where the data comes from and when, or how man times.
        // We can manipulate data as it's on the way o the subscriber.
        // For instance, we can disqualify data items based on certain conditions,
        // such as whether the string is at least three characters long.
        RxTextView.textChanges(editText)
                .filter { text -> text.length >= 3 }
                .debounce(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text -> updateSearchResults(text) }

        /* You’ve added a thread change in the middle, because you need to be
         * sure that any operations that manipulate the UI are executed on the
         * main thread. In this case, the debounce operator will automatically
         * switch the thread to a background one in order to not block the
         * execution while you wait for more input. Most operators don’t switch
         * the thread, but ones that are delayed do.
         */
        /**
         * Exercise
         */
        val editTextExercice = findViewById<EditText>(R.id.edit_text_exercise)
        textViewExercise = findViewById(R.id.text_view_exercise)
        RxTextView.textChanges(editTextExercice)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text -> displayTextView(text) }
    }

    private fun clearSearchResults() {
        arrayAdapter.clear()
    }

    private fun updateSearchResults(text: CharSequence) {
        // Create some random results
        val list: MutableList<String> = ArrayList()
        for (i in 0..3) {
            list.add("" + text + Math.random())
        }
        arrayAdapter.clear()
        arrayAdapter.addAll(list)
    }

    private fun displayTextView(text: CharSequence) {
        textViewExercise.text = if (text.length > 7) "Text too long" else ""
    }
}