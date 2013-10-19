package fr.xgouchet.angel.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.xgouchet.angel.model.Repository;

public class GetReposRequest extends AbstractAsyncGetRequest {

	public GetReposRequest(String token) {
		super("/user/repos", token);
	}

	@Override
	protected void fillRequestParams(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("type", "all"));
	}

	@Override
	protected Object parseResponse(String response) throws Exception {

		JSONArray array = new JSONArray(response);
		int count = array.length();

		List<Repository> repos = new ArrayList<Repository>(count);

		for (int i = 0; i < count; ++i) {
			JSONObject object = array.optJSONObject(i);
			if (object == null) {
				continue;
			}

			repos.add(GithubAPIHelper.parseRepository(object));
		}

		return repos;
	}
}
