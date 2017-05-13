package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by philipp on 04.04.17.
 */

public class Field implements Parcelable
{
    private int _color;

    public Field()
    {
        _color = -1;
    }

    int getColor()
    {
        return _color;
    }

    void setColor(int color)
    {
        _color = color;
    }

    /* everything below here is for implementing Parcelable */

    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_color);
    }

    // this is used to regenerate Field object.
    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    // constructor that takes a Parcel and gives an Field object
    private Field(Parcel in) {
        _color = in.readInt();
    }
}
