package de.dhbw.stuttgart.mastermind;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 23.04.17.
 */

public class SavegameFragment extends Fragment
{
    private SavegameFragment.OnListFragmentInteractionListener mListener;
    public static List<SavegameItem> savegameContent = new ArrayList<>();

    public static SavegameDataSource dataSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SavegameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new SavegameDataSource(this.getContext());
        dataSource.open();

        //dataSource.deleteAllItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savedgames_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            //sorted by fastest time
            savegameContent = dataSource.getAllSavegameItems();

            recyclerView.setAdapter(new SavegameRecyclerViewAdapter(savegameContent, mListener));
        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        dataSource.open();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HighscoreFragment.OnListFragmentInteractionListener) {
            mListener = (SavegameFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(SavegameItem item);
    }
}
