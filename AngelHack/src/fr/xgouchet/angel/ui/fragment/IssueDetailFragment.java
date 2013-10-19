package fr.xgouchet.angel.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.model.Issue;

public class IssueDetailFragment extends Fragment {

	private Issue mIssue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIssue = getArguments().getParcelable("issue");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_issue_details,
				container, false);

		((TextView) root.findViewById(R.id.issueTitle)).setText(mIssue.title);
		((TextView) root.findViewById(R.id.issueBody)).setText(mIssue.body);

		return root;
	}
}
