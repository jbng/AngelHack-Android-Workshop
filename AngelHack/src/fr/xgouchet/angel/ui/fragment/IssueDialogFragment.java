package fr.xgouchet.angel.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.api.APIRequestListener;
import fr.xgouchet.angel.api.CreateIssueRequest;
import fr.xgouchet.angel.model.Issue;
import fr.xgouchet.angel.model.Repository;
import fr.xgouchet.angel.oauth.GithubOauth;
import fr.xgouchet.angel.ui.Toaster;

public class IssueDialogFragment extends DialogFragment implements
		OnClickListener, APIRequestListener {

	public interface Listener {

		void onIssueCreated(Issue result);

	}

	private Listener mListener;

	private Repository mRepository;

	private EditText mTitle, mBody;
	private CheckBox mAssignToMe;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mRepository = getArguments().getParcelable("repo");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("New Issue");

		View content = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_issue, null);
		mTitle = (EditText) content.findViewById(R.id.editIssueTitle);
		mBody = (EditText) content.findViewById(R.id.editIssueBody);
		mAssignToMe = (CheckBox) content.findViewById(R.id.checkBoxAssignToMe);
		builder.setView(content);

		builder.setPositiveButton(R.string.ui_issue_create, this);
		builder.setNegativeButton(R.string.ui_issue_cancel, null);

		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		String token = GithubOauth.getAccessToken(getActivity());

		Issue newIssue = new Issue();
		newIssue.title = mTitle.getText().toString();
		newIssue.body = mBody.getText().toString();

		if (mAssignToMe.isChecked()) {
			newIssue.assignee = mRepository.owner;
		}

		CreateIssueRequest create = new CreateIssueRequest(token, newIssue,
				mRepository);

		create.setListener(this);

		create.execute();
	}

	@Override
	public void onRequestComplete(Object result) {
		if (mListener != null) {
			mListener.onIssueCreated((Issue) result);
		}
	}

	@Override
	public void onRequestException(Exception exception) {
		Toaster.toastException(getActivity(), exception);
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}
}
