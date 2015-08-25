package com.example.cinetvbox;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;




public class Dashboard extends Fragment{
	
	
	private static final String[] CONTENT = new String[] { "Home", "Upcoming Movies", "Next Releases","Now Showing" };
	String uid;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    
    	uid = getArguments().getString("uid");
    	//Instantiating the adapter
    	FragmentPagerAdapter mAdapter = new GoogleMusicAdapter(getFragmentManager());
 
        //instantiate the Views
        ViewPager mPager = (ViewPager)getView().findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
 
        TabPageIndicator mIndicator = (TabPageIndicator)getView().findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

    }
    
    
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBunle ){		
	   final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.StyledIndicators);
	 // clone the inflater using the ContextThemeWrapper
	   LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);	    
	    View view = localInflater.inflate(R.layout.viewpager_main, container, false);
		return view;
		
	}
	
	class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	Bundle bundle = new Bundle();
        	bundle.putString("uid", uid);
        	bundle.putString("cat", "All");
        	switch(position){

    		case 0:
    			Home fragmenttab1 = new Home();
    			fragmenttab1.setArguments(bundle);
    			return fragmenttab1;
    			
    		case 1:
    			Upcomming fragmenttab2 = new Upcomming();
    			fragmenttab2.setArguments(bundle);
    			return fragmenttab2;
    			
    		case 2:
    			NextReleases fragmenttab3 = new NextReleases();
    			fragmenttab3.setArguments(bundle);
    			return fragmenttab3;
    		
			case 3:
				NowShowing fragmenttab4 = new NowShowing();
				fragmenttab4.setArguments(bundle);
				return fragmenttab4;
			}
    		return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
          return CONTENT.length;
        }
	}





}
