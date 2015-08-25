package com.example.cinetvbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NavigationDrawerActivity extends FragmentActivity {
	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	private ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	private String mTitle = "CineTV Box";
	
	String uid,uname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigationdraweractivity);

		mTitle = "CineTV Box";
		getActionBar().setTitle(mTitle);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
		Intent intent = getIntent();
		  if (null != intent) {
			uid= intent.getStringExtra("uid");
			uname= intent.getStringExtra("uname");	
		  }

		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.navigationdraweractivity_drawerlayout);

		mDrawerList = (ListView) findViewById(R.id.navigationdraweractivity_listview_drawerlist);
		
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);


		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("CineTV Box");
				invalidateOptionsMenu();
			}
		};

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(1);
		}

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		List<HashMap<String, String>> data = GetSampleData();
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.navigationdrawerlistitem, new String[] { "image",
						"list" }, new int[] {
						R.id.navigationdrawerlistitem_imageview_icon,
						R.id.navigationdrawerlistitem_textview_title });

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// display view for selected nav drawer item
				displayView(position);
			}

		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Bundle bundle = new Bundle();
    	bundle.putString("uid", uid);
		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = new Dashboard();
			fragment.setArguments(bundle);
			break;
			
		/*case 1:
			fragment = new TrekkingRoute();
			break;

		case 2:
			fragment = new Destination();
			break;*/

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.navigationdraweractivity_framelayout_contentframe, fragment).commit();

			String[] menuItems = getResources().getStringArray(R.array.menus);
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			mTitle = menuItems[position];
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("NavigationDrawerActivity", "Error in creating fragment");
		}
	}

	List<HashMap<String, String>> GetSampleData() {
		int[] pic = new int[] { R.drawable.profile,R.drawable.movie,R.drawable.tv,R.drawable.settings};
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("list", "Logged in: "+ uname);
		map.put("image", String.valueOf(pic[0]));
		list.add(map);
		
		map = new HashMap<String, String>();
		map.put("list", "Movies");
		map.put("image", String.valueOf(pic[1]));
		list.add(map);

		map = new HashMap<String, String>();
		map.put("image", String.valueOf(pic[2]));
		map.put("list", "TV Series");
		list.add(map);
		
		map = new HashMap<String, String>();
		map.put("image", String.valueOf(pic[3]));
		map.put("list", "Settings");
		list.add(map);

		

		return list;
	}

}
