package de.dhbw.stuttgart.mastermind;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.dhbw.stuttgart.mastermind.HighscoreFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HighscoreItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HighscoreRecyclerViewAdapter extends RecyclerView.Adapter<HighscoreRecyclerViewAdapter.ViewHolder> {

    private final List<HighscoreItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int _placeCount;

    public HighscoreRecyclerViewAdapter(List<HighscoreItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        _placeCount = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_highscore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText(String.valueOf(_placeCount) + ".");
        _placeCount = _placeCount + 1;

        holder.mNameView.setText(mValues.get(position).name);
        holder.mDurationView.setText(mValues.get(position).duration);
        holder.mTriesView.setText(String.valueOf(mValues.get(position).tries));
        holder.mColorsView.setText(String.valueOf(mValues.get(position).colors));
        holder.mFieldsView.setText(String.valueOf(mValues.get(position).fields));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    //TODO: implement deleting of a line? or delete whole list from settings?

                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.color.ListBackgroudOdd : R.color.ListBackgroudEven );
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mDurationView;
        public final TextView mTriesView;
        public final TextView mColorsView;
        public final TextView mFieldsView;

        public HighscoreItem mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.name);
            mDurationView = (TextView) view.findViewById(R.id.time);
            mTriesView = (TextView) view.findViewById(R.id.tries);
            mColorsView = (TextView) view.findViewById(R.id.colors);
            mFieldsView = (TextView) view.findViewById(R.id.fields);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
