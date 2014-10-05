package com.hackmit.badmouth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Mugshot extends Activity {

	protected static List<Target> targets;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private final String parse_applicationid = "s32blhuMK0fzjARmluinIoP7GMNCsWXPsREdsFhD";
	private final String parse_clientkey = "dBMVKhUnoFW8kHZzXbVXAc9E0qVrZVfjcfH5J5EQ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, parse_applicationid, parse_clientkey);
		ParseObject.registerSubclass(Target.class);
		ParseObject.registerSubclass(Badmouth.class);

		setContentView(R.layout.activity_mugshot);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(200);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (Mugshot.targets == null) {
			Mugshot.targets = new ArrayList<Target>();
			ParseQuery<Target> query = ParseQuery.getQuery(Target.class);
			query.findInBackground(new FindCallback<Target>() {
				@Override
				public void done(List<Target> objects, ParseException e) {
					if (e == null) {
						Mugshot.targets.addAll(objects);
					} else {
						Log.i("Mugshot", "Retrieving Targets failed.");
					}
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mugshot, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
		}
		return super.onKeyUp(keyCode, event);
	}

	public class SectionPagerAdapter extends FragmentStatePagerAdapter {
		private MugshotFragment current;

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			current = MugshotFragment.newInstance(position + 1);
			return current;
		}

		@Override
		public int getCount() {
			return 400;
		}

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class MugshotFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private Target mugshot;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static MugshotFragment newInstance(int sectionNumber) {
			MugshotFragment fragment = new MugshotFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public MugshotFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mugshot,
					container, false);
			final int n = (Integer) this.getArguments().get(ARG_SECTION_NUMBER);
			TextView tv = (TextView) rootView.findViewById(R.id.target_name);
			final EditText et = (EditText) rootView
					.findViewById(R.id.badmouthbox);
			if (Mugshot.targets.size() != 0) {
				this.mugshot = Mugshot.targets.get((n - 1)
						% Mugshot.targets.size());
				tv.setText(this.mugshot.getName());
				ParseRelation<Badmouth> badmouths = this.mugshot.getRelation(Target.badmouths);
				try {
					Badmouth example = badmouths.getQuery().orderByDescending("createdAt").getFirst();
					et.setHint("eg. " + example.getText());
				} catch (ParseException e) {
				}
			}else{
				tv.setText("Badmouth");
				et.setVisibility(android.view.View.INVISIBLE);
				et.setEnabled(false);
			}
			et.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// If the event is a key-down event on the "enter" button
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						// Perform action on key press
						Toast.makeText(getActivity(), "step 1",
								Toast.LENGTH_SHORT).show();
						String submittedText = et.getText().toString();
						et.setText("");
						final Badmouth newBadMouth = ParseObject.create(Badmouth.class);
						newBadMouth.put(Badmouth.text, submittedText);
						newBadMouth.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								Toast.makeText(getActivity(), "step 2",
										Toast.LENGTH_SHORT).show();
								mugshot.getBadmouths().add(newBadMouth);
								mugshot.saveInBackground();
							}
						});
						return true;
					}
					return false;
				}
			});
			return rootView;
		}
	}

}
