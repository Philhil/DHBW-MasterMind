package de.dhbw.stuttgart.mastermind;

/**
 * Created by philipp on 04.04.17.
 */

public class Row
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
}
