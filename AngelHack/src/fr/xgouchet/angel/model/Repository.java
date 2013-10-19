package fr.xgouchet.angel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Repository implements Parcelable {

	public long id;
	public String name;
	public String desc;
	public User owner;

	public boolean isPrivate;
	public boolean isFork; 

	public Repository() {
	}

	private Repository(Parcel in) {
		id = in.readLong();
		name = in.readString();
		desc = in.readString();
		isPrivate = (in.readByte() != 0);
		isFork = (in.readByte() != 0);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(desc);
		dest.writeByte((byte) (isPrivate ? 1 : 0));
		dest.writeByte((byte) (isFork ? 1 : 0));
	}

	public static final Parcelable.Creator<Repository> CREATOR = new Parcelable.Creator<Repository>() {
		public Repository createFromParcel(Parcel in) {
			return new Repository(in);
		}

		public Repository[] newArray(int size) {
			return new Repository[size];
		}
	};
}
