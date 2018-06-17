package com.murach.invoice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Currency;

public class InvoiceTotalActivity extends Activity
		implements TextView.OnEditorActionListener {
	private EditText subtotalEditText;
	private TextView discountPercentTextView;
	private TextView discountAmountTextView;
	private TextView totalTextView;
	private String subtotalString;
	private SharedPreferences savedValue;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice_total);

		// get references to the widgets
		subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
		discountPercentTextView = (TextView) findViewById(R.id.discountPercentTextView);
		discountAmountTextView = (TextView) findViewById(R.id.discountAmountTextView);
		totalTextView = (TextView) findViewById(R.id.totalTextView);

		subtotalEditText.setOnEditorActionListener(this);

		savedValue = getSharedPreferences("SavedVales", MODE_PRIVATE);
	}
	private void calculateAndDisplay() {
		// get subtotal
		subtotalString = subtotalEditText.getText().toString();
		float subtotal;
		if (subtotalString.equals("")) {
			subtotal = 0;
		}
		else {
			subtotal = Float.parseFloat(subtotalString);
		}

		//get discount percent
		float discountPercent = 0;
		if (subtotal >= 200) {
			discountPercent = .2f;
		}
		else if (subtotal >= 100) {
			discountPercent = .1f;
		}
		else {
			discountPercent = 0;
		}

		//calculate discount
		float discountAmount = subtotal * discountPercent;
		float total = subtotal - discountAmount;

		//display data on layout
		NumberFormat percent = NumberFormat.getPercentInstance();
		discountPercentTextView.setText(percent.format(discountPercent));

		NumberFormat currency = NumberFormat.getCurrencyInstance();
		discountAmountTextView.setText(currency.format(discountAmount));
		totalTextView.setText(currency.format(total));
	}
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		calculateAndDisplay();

		//hide soft keyboard
		return false;
	}

	@Override
	public void onResume () {
		super.onResume();
		subtotalString = savedValue.getString("subtotalString", "");
		subtotalEditText.setText(subtotalString);
		calculateAndDisplay();
	}

	@Override
	public void onPause (){
	SharedPreferences.Editor editor = savedValue.edit();
	editor.putString("subtotalString", subtotalString);
	editor.commit();

	super.onPause();
	}
}