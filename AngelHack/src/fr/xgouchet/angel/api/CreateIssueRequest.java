package fr.xgouchet.angel.api;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import fr.xgouchet.angel.model.Issue;
import fr.xgouchet.angel.model.Repository;

public class CreateIssueRequest extends AbstractAsyncPostRequest {

	private Issue mIssue;

	public CreateIssueRequest(String token, Issue issue, Repository repo) {
		super("/repos/" + repo.owner.login + "/" + repo.name + "/issues", token);
		mIssue = issue;
	}

	@Override
	protected int getExpectedStatus() {
		return HttpStatus.SC_CREATED;
	}

	@Override
	protected String getJsonContent() throws JSONException {
		JSONObject object = new JSONObject();

		object.put("title", mIssue.title);

		if (mIssue.body != null) {
			object.put("body", mIssue.body);
		}

		if (mIssue.assignee != null) {
			object.put("assignee", mIssue.assignee.login);
		}

		return object.toString();
	}

	@Override
	protected Object parseResponse(String response) throws Exception {
		return GithubAPIHelper.parseIssue(new JSONObject(response));
	}

}
