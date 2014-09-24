package com.lambton.currencyconversion;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Ami_javaclass extends Activity
implements OnEditorActionListener, OnSeekBarChangeListener,
 OnItemSelectedListener, OnClickListener, OnKeyListener {

    // define variables for the widgets
    private TextView conversionTitleTextView;
    private EditText oneCurrencyEditText;
    private TextView rateTextView;
    private SeekBar rateSeekBar;
    private EditText secondCurrencyEditText;
    private Button convertButton;
    private Spinner conversionChoiceSpinner;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables
    private String oneCurrencyString = "";
    private String secondCurrencyString = "";
    private float rate = .15f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ami_layout);

        // get references to the widgets
        conversionTitleTextView = (TextView) findViewById(R.id.conversionTitleLabel);
        oneCurrencyEditText = (EditText) findViewById(R.id.oneCurrencyEditText);
        rateTextView = (TextView) findViewById(R.id.rateTextView);
        rateSeekBar = (SeekBar) findViewById(R.id.rateSeekBar);
        secondCurrencyEditText = (EditText) findViewById(R.id.secondCurrencyEditText);
        convertButton = (Button) findViewById(R.id.convertButton);
        conversionChoiceSpinner = (Spinner) findViewById(R.id.conversionChoiceSpinner);

        // set array adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.conversion_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        conversionChoiceSpinner.setAdapter(adapter);

        // set the listeners
        oneCurrencyEditText.setOnEditorActionListener(this);
        rateSeekBar.setOnSeekBarChangeListener(this);
        secondCurrencyEditText.setOnEditorActionListener(this);
        convertButton.setOnClickListener(this);
        rateSeekBar.setOnKeyListener(this);
        conversionChoiceSpinner.setOnItemSelectedListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {

        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("conversionTitle", conversionTitleTextView.getText().toString());
        editor.putString("oneCurrency", oneCurrencyEditText.getText().toString());
        editor.putString("rate", rateTextView.getText().toString());
        editor.putInt("rateSeekBar", rateSeekBar.getProgress());
        editor.putString("secondCurrency", secondCurrencyEditText.getText().toString());
        editor.putInt("conversionChoice", conversionChoiceSpinner.getSelectedItemPosition());
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        conversionTitleTextView.setText(savedValues.getString("conversionTitle", getResources().getString(R.string.conversion_title_label)));
        oneCurrencyEditText.setText(savedValues.getString("oneCurrency", getResources().getString(R.string.one_currency)));
        rateTextView.setText(savedValues.getString("rate", getResources().getString(R.string.rate)));
        rateSeekBar.setProgress(savedValues.getInt("rateSeekBar", getResources().getInteger(R.integer.rate_seekbar)));
        secondCurrencyEditText.setText(savedValues.getString("secondCurrency", getResources().getString(R.string.second_currency)));
        conversionChoiceSpinner.setSelection(savedValues.getInt("conversionChoice", 0));
    }

    public void calculateAndDisplay() {
        // get the currency amount
        oneCurrencyString = oneCurrencyEditText.getText().toString();
        float oneCurrency;
        if (oneCurrencyString.equals("")) {
            oneCurrency = 0;
        } else {
            oneCurrency = Float.parseFloat(oneCurrencyString);
        }
        secondCurrencyString = secondCurrencyEditText.getText().toString();
        float secondCurrency;
        if (secondCurrencyString.equals("")) {
            secondCurrency = 0;
        } else {
            secondCurrency = Float.parseFloat(secondCurrencyString);
        }
        // get rate
        rate = Float.valueOf(rateTextView.getText().toString());
        if (!oneCurrencyString.equals("") && oneCurrency != 0) {
            secondCurrency = oneCurrency * rate;
        } else if (!secondCurrencyString.equals("") && secondCurrency != 0) {
            oneCurrency = secondCurrency / rate;
        } else {
            Toast.makeText(getApplicationContext(), "Please input your currency!", Toast.LENGTH_SHORT).show();
        }
        secondCurrencyEditText.setText(String.format("%.2f",secondCurrency));
        oneCurrencyEditText.setText(String.format("%.2f",oneCurrency));
    }

    //*****************************************************
    // Event handler for the EditText
    //*****************************************************
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            switch(v.getId()) {
            case R.id.oneCurrencyEditText:
                secondCurrencyEditText.setText("");
                break;
            case R.id.secondCurrencyEditText:
                oneCurrencyEditText.setText("");
                break;
            }
            calculateAndDisplay();
        }
        return false;
    }

    //*****************************************************
    // Event handler for the SeekBar
    //*****************************************************
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        rateTextView.setText(String.valueOf(progress/100.00));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    //*****************************************************
    // Event handler for the Spinner
    //*****************************************************
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
            long id) {
        rateSeekBar.setVisibility(View.INVISIBLE);
        conversionTitleTextView.setText(conversionChoiceSpinner.getSelectedItem().toString());
        switch(position) {
        case 0:
            rateSeekBar.setVisibility(View.VISIBLE);
            conversionTitleTextView.setText(R.string.conversion_title_label);
            break;
        case 1:
            rateTextView.setText(R.string.rate_us2ca);
            calculateAndDisplay();
            break;
        case 2:
            rateTextView.setText(R.string.rate_ca2us);
            calculateAndDisplay();
            break;
        case 3:
            rateTextView.setText(R.string.rate_cn2us);
            calculateAndDisplay();
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    //*****************************************************
    // Event handler for the keyboard and DPad
    //*****************************************************
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:

                calculateAndDisplay();

                // hide the soft keyboard
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        oneCurrencyEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(
                        secondCurrencyEditText.getWindowToken(), 0);

                // consume the event
                return true;
        }
        // don't consume the event
        return false;
    }

    @Override
    public void onClick(View v) {
        calculateAndDisplay();
    }
}