package fr.xgouchet.angel.ui.activity;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.api.APIRequestListener;
import fr.xgouchet.angel.api.GetReposRequest;
import fr.xgouchet.angel.model.Issue;
import fr.xgouchet.angel.model.Repository;
import fr.xgouchet.angel.oauth.GithubOauth;
import fr.xgouchet.angel.ui.Toaster;
import fr.xgouchet.angel.ui.adapter.RepoAdapter;
import fr.xgouchet.angel.ui.fragment.IssueDetailFragment;
import fr.xgouchet.angel.ui.fragment.IssuesListFragment;

public class MainActivity extends FragmentActivity implements
		APIRequestListener, OnItemClickListener {

	// Navigation Drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private ActionBarDrawerToggle mDrawerToggleListener;
	private DrawerListener mDrawerListener;
	private RepoAdapter mDrawerAdapter;
	private String mTitle;

	// Master / Detail fragments
	private IssuesListFragment mMasterFragment;

	// Smartphone / Tablet
	private boolean mIsTwoPaned = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content
		setContentView(R.layout.activity_main);

		// setup master details
		if (findViewById(R.id.detail_container) == null) {
			mIsTwoPaned = false;
			mMasterFragment = new IssuesListFragment();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.add(R.id.main_container, mMasterFragment);
			transaction.commit();
		} else {
			mIsTwoPaned = true;
			mMasterFragment = (IssuesListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.master_container);
		}

		mTitle = getString(R.string.app_name);

		// Setup the drawer
		setupNavigationDrawer();
		setupDrawerToggle();

		// setup nav drawer
		setupNavigationDrawer();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (mDrawerToggleListener != null) {
			mDrawerToggleListener.syncState();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Refresh the list of repositories for this user
		String token = GithubOauth.getAccessToken(this);
		GetReposRequest repos = new GetReposRequest(token);
		repos.setListener(this);
		repos.execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggleListener != null) {
			if (mDrawerToggleListener.onOptionsItemSelected(item)) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestComplete(Object result) {

		List<Repository> repos = (List<Repository>) result;
		mDrawerAdapter = new RepoAdapter(this, R.layout.item_drawer, repos);
		mDrawerListView.setAdapter(mDrawerAdapter);

		if (repos.size() > 0) {

			mDrawerListView.setItemChecked(0, true);
			onItemClick(mDrawerListView, null, 0, 0);

			mDrawerLayout.openDrawer(mDrawerListView);

		}
	}

	@Override
	public void onRequestException(Exception exception) {
		Toaster.toastException(this, exception);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Highlight the selected item
		mDrawerListView.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerListView);

		// get repository
		Repository repo = mDrawerAdapter.getItem(position);

		mTitle = repo.name;

		// start list fragment
		mMasterFragment.loadRepositoryIssues(repo);
	}

	private void setupNavigationDrawer() {
		mDrawerListView = (ListView) findViewById(R.id.drawer_panel);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);

		mDrawerListView.setOnItemClickListener(this);
		mDrawerListView.setItemChecked(0, true);

		mDrawerListener = new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int newState) {
				if (mDrawerToggleListener != null) {
					mDrawerToggleListener.onDrawerStateChanged(newState);
				}
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				if (mDrawerToggleListener != null) {
					mDrawerToggleListener
							.onDrawerSlide(drawerView, slideOffset);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if (mDrawerToggleListener != null) {
					mDrawerToggleListener.onDrawerOpened(drawerView);
				}

				setTitle(R.string.app_name);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				if (mDrawerToggleListener != null) {
					mDrawerToggleListener.onDrawerClosed(drawerView);
				}

				setTitle(mTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerListener);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setupDrawerToggle() {
		mDrawerToggleListener = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_open);

		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}
	}

	public void displayIssueDetail(Issue issue) {
		int containerId;
		final Fragment details = new IssueDetailFragment();
		Bundle arguments = new Bundle(1);
		arguments.putParcelable("issue", issue);
		details.setArguments(arguments);

		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if (mIsTwoPaned) {
			containerId = R.id.detail_container;
		} else {
			containerId = R.id.main_container;
			transaction.addToBackStack("detail");
		}

		transaction.replace(containerId, details).commit();
	}

}
