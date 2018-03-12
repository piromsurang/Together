package com.example.piromsurang.together;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piromsurang.together.adapters.InvitationAdapter;
import com.example.piromsurang.together.models.Invitation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvitationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private InvitationAdapter adapter;
    private List<Invitation> invitationList = new ArrayList<>();
    private EditText title_edit;
    private EditText location_edit;
    private EditText time_edit;
    private EditText timer_edit;

    public InvitationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitationFragment newInstance(String param1, String param2) {
        InvitationFragment fragment = new InvitationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);

        adapter = new InvitationAdapter(invitationList);
        addButton = (FloatingActionButton) view.findViewById(R.id.addInvitationButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddInvitationDialog(v);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.invitationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.countdown_progressbar);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void openAddInvitationDialog(View view) {
        final Dialog dialog = new Dialog(this.getContext());
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_invitation, null);
        dialog.setContentView(layout);
        Button create_invitation_button = (Button) dialog.findViewById(R.id.create_add_button);
        Button cancel_dialog_button = (Button) dialog.findViewById(R.id.cancel_add_button);
        title_edit = (EditText) dialog.findViewById(R.id.title_edittext);
        location_edit = (EditText) dialog.findViewById(R.id.location_edittext);
        time_edit = (EditText) dialog.findViewById(R.id.time_edittext);
        timer_edit = (EditText) dialog.findViewById(R.id.timer_edittext);
        create_invitation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(title_edit.getText().toString().isEmpty() ||
                        location_edit.getText().toString().isEmpty() ||
                        time_edit.getText().toString().isEmpty() ||
                        timer_edit.getText().toString().isEmpty() )) {
                    createInvitation();
                    dialog.dismiss();
                }
            }
        });
        cancel_dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createInvitation() {
        Log.d("Test", "create invitation");
    }
}
