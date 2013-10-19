package fr.xgouchet.angel.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.xgouchet.angel.R;
import fr.xgouchet.angel.model.Repository;

public class RepoAdapter extends ArrayAdapter<Repository> {

	public RepoAdapter(Context context, int resource, List<Repository> items) {
		super(context, resource, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		TextView text = (TextView) view.findViewById(android.R.id.text1);

		Repository repo = getItem(position);

		int iconId;
		if (repo.isFork) {
			iconId = R.drawable.ic_fork;
		} else {
			iconId = R.drawable.ic_repo;
		}

		int color;
		if (repo.isPrivate) {
			color = getContext().getResources().getColor(R.color.repo_private);
		} else {
			color = getContext().getResources().getColor(R.color.repo_public);
		}

		text.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
		text.setTextColor(color);

		return view;
	}
}
