package de.dhbw.stuttgart.mastermind;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by philipp on 04.04.17.
 */

public class Row implements Parcelable
{
    private int _anzFields;

    public int RightPlace;
    public int RightColor;

    public Field[] Fields;

    public Row(int fields)
    {
        _anzFields = fields;

        Fields = new Field[_anzFields];
    }

    /* everything below here is for implementing Parcelable */

    // constructor that takes a Parcel and gives an Row object
    private Row(Parcel in) {
        _anzFields = in.readInt();
        RightPlace = in.readInt();
        RightColor = in.readInt();

        Fields = new Field[in.readInt()];
        in.readTypedArray(Fields, Field.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_anzFields);
        out.writeInt(RightPlace);
        out.writeInt(RightColor);

        out.writeInt(Fields.length);
        out.writeTypedArray(Fields, flags);
    }

    // this is used to regenerate the Row object.
    public static final Parcelable.Creator<Row> CREATOR = new Parcelable.Creator<Row>() {
        public Row createFromParcel(Parcel in) {
            return new Row(in);
        }

        public Row[] newArray(int size) {
            return new Row[size];
        }
    };


}
