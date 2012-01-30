package org.jaykid.linkatron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.ArrayAdapter;

public class LinkListActivity extends ListActivity{

	static final String START = "<pre>";
	static final String URL = "url";
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkslist);
        
        String[] links = null;
        String url = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString(URL)!= null)
        	url = bundle.getString(URL);
        
		try {
			links = getLinks(url);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, links);
		setListAdapter(adapter);
    }
	
    private void copyTextToClipboard(String text) {
    	ClipboardManager clipboard = 
    		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
    	clipboard.setText(text);
    }
    
    private String[] getLinks(String url) throws Exception, IOException {
    	
    	String htmlLine = getHtmlLineFromUrl(url);
    	
    	String[] links = htmlLine.split("<br/>");
    	
    	return links;
    }

	private String getHtmlLineFromUrl(String url) throws IOException, ClientProtocolException {
		boolean copyingState = false;
		String line = "";
    	String htmlLine = "";
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpGet request = new HttpGet(url);
    	HttpResponse response = client.execute(request);
    	
    	InputStream in = response.getEntity().getContent();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	while((line = reader.readLine()) != null){
    		if (copyingState) {
    			htmlLine = line;
    			break;
    		}
    		if (line.equals(START)) copyingState = true;
    	}
    	in.close();
    	return htmlLine;
	}
}
