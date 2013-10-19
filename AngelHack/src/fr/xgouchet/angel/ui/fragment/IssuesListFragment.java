package fr.xgouchet.angel.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.api.APIRequestListener;
import fr.xgouchet.angel.api.GetIssuesFromRepoRequest;
import fr.xgouchet.angel.model.Issue;
import fr.xgouchet.angel.model.Repository;
import fr.xgouchet.angel.oauth.GithubOauth;
import fr.xgouchet.angel.ui.Toaster;
import fr.xgouchet.angel.ui.activity.MainActivity;
import fr.xgouchet.angel.ui.adapter.IssueAdapter;
import fr.xgouchet.angel.ui.fragment.IssueDialogFragment.Listener;

public class IssuesListFragment extends ListFragment implements
		APIRequestListener, Listener {

	private Repository mRepository;
	private IssueAdapter mAdapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Issue issue = (Issue) getListAdapter().getItem(position);

		((MainActivity) getActivity()).displayIssueDetail(issue);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		if ((mRepository != null) && (!mRepository.isFork)) {
			inflater.inflate(R.menu.issues, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add_issue) {
			addIssue();
		}
		return super.onOptionsItemSelected(item);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void loadRepositoryIssues(Repository repo) {
		mRepository = repo;

		setListAdapter(null);

		if (repo.isFork) {
			Toaster.toastException(getActivity(), new IllegalArgumentException(
					"Issues are not available on fork repositories"));
			return;
		}

		String token = GithubOauth.getAccessToken(getActivity());
		GetIssuesFromRepoRequest request = new GetIssuesFromRepoRequest(token,
				repo.owner.login, repo.name);
		request.setListener(this);
		request.execute();

		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			getActivity().invalidateOptionsMenu();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestComplete(Object result) {
		mAdapter = new IssueAdapter(getActivity(),
				android.R.layout.simple_list_item_1, (List<Issue>) result);
		setListAdapter(mAdapter);
	}

	@Override
	public void onRequestException(Exception exception) {
		Toaster.toastException(getActivity(), exception);
	}

	@Override
	public void onIssueCreated(Issue result) {
		if (mAdapter == null) {
			mAdapter = new IssueAdapter(getActivity(),
					android.R.layout.simple_list_item_1, new ArrayList<Issue>());
			setListAdapter(mAdapter);
		}
		mAdapter.add(result);
	}

	private void addIssue() {
		IssueDialogFragment dialog = new IssueDialogFragment();
		Bundle arguments = new Bundle(1);
		arguments.putParcelable("repo", mRepository);
		dialog.setArguments(arguments);

		dialog.setListener(this);

		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();

		transaction.add(dialog, "");
		transaction.commit();
	}
}
