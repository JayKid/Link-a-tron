package org.jaykid.linkatron;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LinkATronActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
    
    public void onShowLinksClick(View button) {

        Intent newIntent = new Intent(this, LinkListActivity.class);
        startActivity(newIntent);
    }
    

}

