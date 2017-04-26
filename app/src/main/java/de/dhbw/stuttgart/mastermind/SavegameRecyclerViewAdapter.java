package de.dhbw.stuttgart.mastermind;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by philipp on 23.04.17.
 */

public class SavegameRecyclerViewAdapter extends RecyclerView.Adapter<SavegameRecyclerViewAdapter.ViewHolder> {

    private final List<SavegameItem> mValues;
    private final SavegameFragment.OnListFragmentInteractionListener mListener;
    private int _placeCount;

    public SavegameRecyclerViewAdapter(List<SavegameItem> items, SavegameFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        _placeCount = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_savedgames, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText(String.valueOf(_placeCount) + ".");
        _placeCount = _placeCount + 1;

        holder.mNameView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

                    //TODO: implement deleting of a line?

                    Intent intent = new Intent(v.getContext(), GameActivity.class);
                    intent.putExtra("saveGame", mValues.get(position).saveGame);
                    intent.putExtra("id", Long.valueOf(mValues.get(position).id));
                    v.getContext().startActivity(intent);

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

        public SavegameItem mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}