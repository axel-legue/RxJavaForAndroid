package com.tehmou.book.androidlivesearchexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import java.util.*

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

        val editText = findViewById<EditText>(R.id.edit_text)
        val editTextExercice = findViewById<EditText>(R.id.edit_text_exercise)
        textViewExercise = findViewById(R.id.text_view_exercise)


        val input1: Observable<CharSequence> = RxTextView.textChanges(editText)
        val input2: Observable<CharSequence> = RxTextView.textChanges(editTextExercice)
        val combineEditTextObservable: Observable<CharSequence> =
                Observable.combineLatest(
                        input1,
                        input2,
                        BiFunction { t1: CharSequence, t2: CharSequence ->
                            return@BiFunction "$t1 $t2"
                        }
                )

        combineEditTextObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayTextView)


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
        textViewExercise.text = text
    }
}