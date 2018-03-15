package com.example.piromsurang.together;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.piromsurang.together.adapters.FriendListAdapter;
import com.example.piromsurang.together.adapters.InvitationAdapter;
import com.example.piromsurang.together.models.Friend;
import com.example.piromsurang.together.models.FriendRepository;
import com.example.piromsurang.together.models.Invitation;
import com.example.piromsurang.together.presenters.FriendPresenter;
import com.example.piromsurang.together.views.FriendView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvitationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitationFragment extends Fragment implements FriendView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView invitationRecycleView;
    private ProgressBar progressBar;
    private InvitationAdapter adapter;
    private List<Invitation> invitationList = new ArrayList<>();
    private EditText title_edit;
    private EditText location_edit;
    private EditText time_edit;
    private EditText timer_edit;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private FriendRepository friendRepository;
    private FriendPresenter friendPresenter;

    private RecyclerView showFriendsRecycleView;
    private RecyclerView addedFriendsRecycleView;

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
        loadFacebookFriends();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        friendRepository = FriendRepository.getInstance();
        friendPresenter = new FriendPresenter(friendRepository, this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);

        adapter = new InvitationAdapter(invitationList);


        initializeInvitationRecycleView(view);
        initilizeOtherComponentsInvitations(view);
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

    @Override
    public void displayeFriendList(int permission) {
        if(permission == FriendPresenter.ON_DISPLAY_ADDED_LIST) {
            Log.d("Test from Fragment", friendPresenter.getAddedList().size() + " added list");
            addedFriendsRecycleView.setAdapter(new FriendListAdapter(friendPresenter, FriendPresenter.ON_DISPLAY_ADDED_LIST));
        } else if(permission == FriendPresenter.ON_DISPLAY_SEARCHED_LIST) {
            showFriendsRecycleView.setAdapter(new FriendListAdapter(friendPresenter, FriendPresenter.ON_DISPLAY_SEARCHED_LIST));
        }
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

        initializeOtherComponentsDialog(dialog);
        initializeShowFriendRecycleView(dialog);
        initializeAddedFriendRecycleView(dialog);
        initializeSearchEditText(dialog);

        dialog.show();
    }

    public void createInvitation() {
        String facebookUserId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//      find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
            }
        }
        String title = title_edit.getText().toString();
        String location = location_edit.getText().toString();
        String time = time_edit.getText().toString();
        int countdown_time = Integer.parseInt(timer_edit.getText().toString());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        System.out.println(dateFormat.format(date));
        myRef.child(facebookUserId).child("invitations").setValue(new Invitation(title, location, time, countdown_time, dateFormat.format(date)));
        Log.d("Test", "create invitation");
    }

    public void initilizeOtherComponentsInvitations(View view) {
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addInvitationButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddInvitationDialog(v);
            }
        });
        progressBar = (ProgressBar) view.findViewById(R.id.countdown_progressbar);
    }

    public void initializeInvitationRecycleView(View view) {
        invitationRecycleView = (RecyclerView) view.findViewById(R.id.invitationRecycleView);
        invitationRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        invitationRecycleView.setItemAnimator(new DefaultItemAnimator());
        invitationRecycleView.setAdapter(adapter);
    }

    public void initializeSearchEditText(Dialog dialog) {
        EditText searchFriendEditText = (EditText) dialog.findViewById(R.id.search_friends_edittext);
        searchFriendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Friend> searchedList;
                if(s.toString().isEmpty()) {
                    friendRepository.clearSearchedList();
                    searchedList = friendRepository.getSearchedList();
                } else {
                    searchedList = friendRepository.searchByName(s.toString());
                }
                friendPresenter.displayList(FriendPresenter.ON_DISPLAY_SEARCHED_LIST);
            }
        });
    }

    public void initializeAddedFriendRecycleView(Dialog dialog) {
        addedFriendsRecycleView = (RecyclerView) dialog.findViewById(R.id.added_friend_recycleview);
        addedFriendsRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        addedFriendsRecycleView.setItemAnimator(new DefaultItemAnimator());
        addedFriendsRecycleView.addItemDecoration(new DividerItemDecoration(dialog.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void initializeOtherComponentsDialog(final Dialog dialog) {
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
    }

    public void initializeShowFriendRecycleView(Dialog dialog) {
        showFriendsRecycleView = (RecyclerView) dialog.findViewById(R.id.add_friend_recycleview);
        showFriendsRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        showFriendsRecycleView.setItemAnimator(new DefaultItemAnimator());
        showFriendsRecycleView.addItemDecoration(new DividerItemDecoration(dialog.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void loadFacebookFriends() {
        String facebookUserId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//      find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
            }
        }

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + facebookUserId + "/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        JSONObject friends = response.getJSONObject();
                        JSONArray data = null;
                        try {
                            data = friends.getJSONArray("data");
//                            Log.d("Test", response.toString());
                            for(int i = 0 ; i < data.length() ; i++) {
                                JSONObject object = data.getJSONObject(i);
                                friendPresenter.addFriend(new Friend(object.getString("id"), object.getString("name")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d("Test", friendRepository.getFriendList().size() + "");
                    }
                }
        ).executeAsync();

    }
}
