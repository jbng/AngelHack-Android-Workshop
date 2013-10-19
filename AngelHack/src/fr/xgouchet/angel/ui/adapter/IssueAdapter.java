package fr.xgouchet.angel.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import fr.xgouchet.angel.model.Issue;

public class IssueAdapter extends ArrayAdapter<Issue> {

	public IssueAdapter(Context context, int resource, List<Issue> items) {
		super(context, resource, items);
	}

}
