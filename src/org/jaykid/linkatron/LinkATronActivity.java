package org.jaykid.linkatron;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LinkATronActivity extends Activity {
	
	static final String URL = "url";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onShowLinksClick(View button) {

        Intent newIntent = new Intent(this, LinkListActivity.class);
        newIntent.putExtra(URL, ((EditText)findViewById(R.id.pasteIdMain)).getText().toString());
        startActivity(newIntent);
    }
}

