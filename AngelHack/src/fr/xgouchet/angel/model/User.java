package fr.xgouchet.angel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	public long id;
	public String login;
	public String avatar;

	public User() {
	}

	private User(Parcel in) {
		id = in.readLong();
		login = in.readString();
		avatar = in.readString();
	}

	@Override
	public String toString() {
		return login;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(login);
		dest.writeString(avatar);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

}
