package fr.xgouchet.angel.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.xgouchet.angel.model.Issue;

public class GetIssuesFromRepoRequest extends AbstractAsyncGetRequest {

	public GetIssuesFromRepoRequest(String token, String owner, String repo) {
		super("/repos/" + owner + "/" + repo + "/issues", token);
	}

	@Override
	protected void fillRequestParams(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("filter", "all"));
	}

	@Override
	protected Object parseResponse(String response) throws Exception {

		JSONArray array = new JSONArray(response);
		int count = array.length();

		List<Issue> issues = new ArrayList<Issue>(count);

		for (int i = 0; i < count; ++i) {
			JSONObject object = array.optJSONObject(i);
			if (object == null) {
				continue;
			}

			issues.add(GithubAPIHelper.parseIssue(object));
		}

		return issues;
	}
}
