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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class LinkListActivity extends ListActivity{

	static final String START = "<pre>";
	static final String URL = "url";
	private static final CharSequence[] items = { "Open in a Browser", "Copy to clipboard" , "Share" };
	private String url = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkslist);
        
        String[] links = null;
        String pastieId = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString(URL)!= null)
        	pastieId = bundle.getString(URL);
        
		try {
			links = getLinks(pastieId);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, links);
		setListAdapter(adapter);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
				
				url = getListView().getItemAtPosition(position).toString();
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
				dialogBuilder.setTitle("Select an option:");
				
				dialogBuilder.setItems(items, new OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
	                	
						fixUrlWithHttp();
						switch(item){
							case 0:
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								startActivity(browserIntent);
								break;
							case 1:
								copyTextToClipboard(url);
								break;
							case 2:
								Intent sharingIntent = new Intent(Intent.ACTION_SEND);
								sharingIntent.setType("text/plain");
								sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
								startActivity(Intent.createChooser(sharingIntent,"Share using"));
								break;
						}
						
					}
				});
				AlertDialog ad = dialogBuilder.create();
				ad.show();
			}
		});
		
    }
	
	private void fixUrlWithHttp() {
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			   url = "http://" + url;
	}
	
    private void copyTextToClipboard(String text) {
    	ClipboardManager clipboard = 
    		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
    	clipboard.setText(text);
    }
    
    private String[] getLinks(String pastieId) throws Exception, IOException {
    	
    	String htmlLine = getHtmlLineFromUrl("http://pastie.org/pastes/"+pastieId+"/text");
    	
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
