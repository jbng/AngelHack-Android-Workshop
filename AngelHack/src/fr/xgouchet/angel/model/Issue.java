package fr.xgouchet.angel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Issue implements Parcelable {

	public long number;
	public String state; // open / closed
	public String title;
	public String body;
	public User user, assignee;

	public Issue() {
	}

	public Issue(Parcel in) {
		number = in.readLong();

		state = in.readString();
		title = in.readString();
		body = in.readString();

		user = in.readParcelable(null);
		assignee = in.readParcelable(null);

	}

	@Override
	public String toString() {
		return title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(number);

		dest.writeString(state);
		dest.writeString(title);
		dest.writeString(body);
		dest.writeParcelable(user, flags);
		dest.writeParcelable(assignee, flags);
	}

	public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
		public Issue createFromParcel(Parcel in) {
			return new Issue(in);
		}

		public Issue[] newArray(int size) {
			return new Issue[size];
		}
	};

}
