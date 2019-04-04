package ar.com.lrusso.andruinobluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Sketch2 extends Activity
	{
	private WebView webView;

	@Override protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sketch2);

		webView = (WebView) findViewById(R.id.webView1);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.getSettings().setJavaScriptEnabled(true);

		// LOADING THE HTML DOCUMENT
		String resultHTML = loadAssetTextAsString("Sketch2.htm");

		// LOADING THE WEBVIEW
		webView.loadDataWithBaseURL(null, resultHTML, null, "utf-8", null);

		webView.setWebViewClient(new myWebClient());
		webView.setWebChromeClient(new WebChromeClient(){});

		Button button = (Button) findViewById(R.id.copyCode);
		button.setOnClickListener(new OnClickListener()
			{
			@Override public void onClick(View v)
				{
				clickInCopySketch();
				}
			});
		}

	@Override public boolean onCreateOptionsMenu(Menu menu)
		{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
		}

	public boolean onOptionsItemSelected(MenuItem item)
		{
		switch (item.getItemId())
			{
			case R.id.action_settings:
			View menuItemView = findViewById(R.id.action_settings);
			PopupMenu popupMenu = new PopupMenu(this, menuItemView);
			popupMenu.inflate(R.menu.popup_menu2);

			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
				public boolean onMenuItemClick(MenuItem item)
					{
					if (item.getTitle().toString().contains(getResources().getString(R.string.textAbout)))
						{
						clickInAbout();
						}
					return true;
					}
				});
			popupMenu.show();
			return true;

			default:
			return super.onOptionsItemSelected(item);
			}
		}

	private String loadAssetTextAsString(String name)
		{
		BufferedReader in = null;
		try
			{
			StringBuilder buf = new StringBuilder();
			InputStream is = getAssets().open(name);
			in = new BufferedReader(new InputStreamReader(is));

			String str;
			boolean isFirst = true;
			while ((str = in.readLine()) != null)
				{
				if (isFirst)
					{
					isFirst = false;
					}
					else
					{
					buf.append("\n");
					}
				buf.append(str);
				}
			return buf.toString();
			}
			catch (IOException e)
			{
			}
			finally
			{
			if (in != null)
				{
				try
					{
					in.close();
					}
					catch (IOException e)
					{
					}
				}
			}
		return null;
		}

	private void clickInCopySketch()
		{
		try
			{
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("Data", getResources().getString(R.string.textSketch2Example));
			clipboard.setPrimaryClip(clip);
			Toast.makeText(this,getResources().getString(R.string.textCopyToClipobardOK),Toast.LENGTH_SHORT).show();
			}
			catch(Exception e)
			{
			}
		}

	private void clickInAbout()
		{
		String anos = "";
		String valor = getResources().getString(R.string.textAboutMessage);
		int lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;
		if (lastTwoDigits<=5)
			{
			anos = "2005";
			}
			else
			{
			anos ="2005 - 20" + String.valueOf(lastTwoDigits).trim();
			}

		valor = valor.replace("ANOS", anos);

		TextView msg = new TextView(this);
		msg.setText(Html.fromHtml(valor));
		msg.setPadding(10, 20, 10, 25);
		msg.setGravity(Gravity.CENTER);
		float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
		float size = new EditText(this).getTextSize() / scaledDensity;
		msg.setTextSize(size);

		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.textAbout)).setView(msg).setIcon(R.drawable.ic_launcher).setPositiveButton(getResources().getString(R.string.textOK),new DialogInterface.OnClickListener()
			{
			public void onClick(DialogInterface dialog,int which)
				{
				}
			}).show();
		}
	}