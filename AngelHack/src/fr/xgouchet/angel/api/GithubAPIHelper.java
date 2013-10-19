package fr.xgouchet.angel.api;

import org.json.JSONObject;

import fr.xgouchet.angel.model.Issue;
import fr.xgouchet.angel.model.Repository;
import fr.xgouchet.angel.model.User;

public class GithubAPIHelper {

	public static final String BASE_URL = "https://api.github.com";
	
	
	public static Repository parseRepository(JSONObject object) {
		if (object == null) {
			return null;
		}

		Repository repo = new Repository();

		repo.id = object.optLong("id");
		repo.name = object.optString("name");
		repo.desc = object.optString("description");
		repo.isPrivate = object.optBoolean("private");
		repo.isFork = object.optBoolean("fork");
		repo.owner = parseUser(object.optJSONObject("owner"));

		return repo;
	}

	public static User parseUser(JSONObject object) {
		if (object == null) {
			return null;
		}

		User user = new User();

		user.id = object.optLong("id");
		user.login = object.optString("login");
		user.avatar = object.optString("avatar");

		return user;
	}

	public static Issue parseIssue(JSONObject object) {
		if (object == null) {
			return null;
		}

		Issue issue = new Issue();

		issue.number = object.optLong("number");
		issue.title = object.optString("title");
		issue.state = object.optString("state");
		issue.body = object.optString("body");

		issue.user = parseUser(object.optJSONObject("user"));
		issue.assignee = parseUser(object.optJSONObject("assignee"));

		return issue;
	}
}
