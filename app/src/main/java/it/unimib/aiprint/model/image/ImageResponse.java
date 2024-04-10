package it.unimib.aiprint.model.image;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageResponse implements Parcelable {
    private boolean isLoading;
    private Image image;
    private String id;
    private String output_url;

    protected ImageResponse(String id, String output_url) {
     this.id = id;
     this.output_url = output_url;
    }

    public String getOutput_url() {
        return output_url;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    protected ImageResponse(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.isLoading = in.readByte() != 0;
        //this.image = in.readStringArray(Image.CREATOR);
    }

    public static final Creator<ImageResponse> CREATOR = new Creator<ImageResponse>() {
        @Override
        public ImageResponse createFromParcel(Parcel in) {
            return new ImageResponse(in);
        }

        @Override
        public ImageResponse[] newArray(int size) {
            return new ImageResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dest.writeTypedObject(this.image, flags);
        }
    }
}
