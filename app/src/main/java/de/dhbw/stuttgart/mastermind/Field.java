package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by philipp on 04.04.17.
 */

public class Field
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

    ImageView getPicture(Context context)
    {
        ImageView tmp = new ImageView(context);

        switch(_color)
        {
            case 0:
                tmp.setImageResource(R.mipmap.ic_blue);
                break;
            case 1:
                tmp.setImageResource(R.mipmap.ic_green);
                break;
            case 2:
                tmp.setImageResource(R.mipmap.ic_lightblue);
                break;
            case 3:
                tmp.setImageResource(R.mipmap.ic_pink);
                break;
            case 4:
                tmp.setImageResource(R.mipmap.ic_red);
                break;
            case 5:
                tmp.setImageResource(R.mipmap.ic_yellow);
                break;
            case 6:
                tmp.setImageResource(R.mipmap.ic_grey);
                break;
            case 7:
                tmp.setImageResource(R.mipmap.ic_purple);
                break;
            default:
                tmp.setImageResource(R.mipmap.ic_slot);
                break;
        }

        return tmp;
    }
}
