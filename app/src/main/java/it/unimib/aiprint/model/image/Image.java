package it.unimib.aiprint.model.image;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.PrimaryKey;

public class Image implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String output_url;

    protected Image(Parcel in) {
        setId(in.readLong());
        setOutput_url(in.readString());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getId());
        parcel.writeString(getOutput_url());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOutput_url() {
        return output_url;
    }

    public void setOutput_url(String output_url) {
        this.output_url = output_url;
    }
}
