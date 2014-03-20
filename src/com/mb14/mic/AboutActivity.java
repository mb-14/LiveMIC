package com.mb14.mic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;


public class AboutActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        Button rateButton = (Button)findViewById(R.id.rate_button);
        rateButton.setOnClickListener(this);
		
	}


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mb14.mic"));
        startActivity(intent);

    }
}
