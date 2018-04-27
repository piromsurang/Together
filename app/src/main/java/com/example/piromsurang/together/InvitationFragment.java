package com.example.piromsurang.together;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.piromsurang.together.adapters.FriendListAdapter;
import com.example.piromsurang.together.adapters.InvitationAdapter;
import com.example.piromsurang.together.models.Friend;
import com.example.piromsurang.together.models.FriendRepository;
import com.example.piromsurang.together.models.CreatedInvitation;
import com.example.piromsurang.together.models.InvitationRepository;
import com.example.piromsurang.together.models.ReceivedInvitation;
import com.example.piromsurang.together.presenters.FriendPresenter;
import com.example.piromsurang.together.presenters.InvitationPresenter;
import com.example.piromsurang.together.views.FriendView;
import com.example.piromsurang.together.views.InvitationView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvitationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitationFragment extends Fragment implements FriendView, InvitationView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView invitationRecycleView;
    private InvitationAdapter adapter;
    private EditText title_edit;
    private EditText location_edit;
    private EditText time_edit;
    private EditText timer_edit;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private FriendRepository friendRepository;
    private FriendPresenter friendPresenter;
    private InvitationRepository invitationRepository;
    private InvitationPresenter invitationPresenter;

    private RecyclerView showFriendsRecycleView;
    private RecyclerView addedFriendsRecycleView;
    private String facebookUserId;

    private TextView countDownTextView;
    private Dialog dialog;

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

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        friendRepository = FriendRepository.getInstance();
        friendPresenter = new FriendPresenter(friendRepository, this);
        invitationRepository = InvitationRepository.getInstance();
        invitationPresenter = new InvitationPresenter(invitationRepository, this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//      find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
            }
        }

//        loadFacebookFriends();
//        loadInvitations();

        new DownloadTasks().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);

        adapter = new InvitationAdapter(invitationPresenter, this);


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
//            Log.d("Test from Fragment", friendPresenter.getAddedList().size() + " added list");
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
        friendPresenter.clearAddedList();
        dialog = new Dialog(this.getContext());
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_invitation, null);
        dialog.setContentView(layout);

        initializeOtherComponentsDialog(dialog);
        initializeShowFriendRecycleView(dialog);
        initializeAddedFriendRecycleView(dialog);
        initializeSearchEditText(dialog);

        dialog.show();
    }

    @Override
    public void displayInvitationList() {
        invitationRecycleView.setAdapter(new InvitationAdapter(invitationPresenter, this));
    }

    @Override
    public void createInvitation() {

        final String title = title_edit.getText().toString();
        String location = location_edit.getText().toString();
        String time = time_edit.getText().toString();
        final int countdown_time = Integer.parseInt(timer_edit.getText().toString());
//        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        // need mid something in gradle module to be 26 only support API version 26 or later
//        LocalDateTime currentTime = LocalDateTime.now();

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        c.add(Calendar.MINUTE, countdown_time);
        Date timeout = c.getTime();
//        Log.d("Test time", dateFormat.format(date));

        final String uuid = invitationPresenter.generateUuid();
        CreatedInvitation createdInvitation = new CreatedInvitation(title, location, time, countdown_time+"", date, timeout, uuid);
        ReceivedInvitation receivedInvitation = new ReceivedInvitation(title, location, time, countdown_time +"", date, timeout, facebookUserId, uuid);
        createdInvitation.setInvitedFriends(friendPresenter.getAddedList());
        myRef.child(facebookUserId).child("invitations").child("created").child(createdInvitation.getUuid()).setValue(createdInvitation);
        for(int i = 0 ; i < friendPresenter.getAddedList().size() ; i++) {
//            myRef.child(facebookUserId).child("invitations").child("created").child(createdInvitation.getUuid()).child("friends").child(i+"").setValue(friendPresenter.getAddedList().get(i));
            receivedInvitation.setIndex(i);
            myRef.child(friendPresenter.getAddedList().get(i).getId()).child("invitations").child("received").child(receivedInvitation.getUuid().toString()).setValue(receivedInvitation);
        }
        friendPresenter.clearAddedList();

        final CreatedInvitation cc = createdInvitation;
        //30 sec
        new CountDownTimer(countdown_time * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                String newtime = hours + ":" + minutes + ":" + seconds;
                if (newtime.equals("0:0:0")) {
                    newtime = "00:00:00";
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    newtime = "0" + hours + ":0" + minutes + ":0" + seconds;
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                    newtime = "0" + hours + ":0" + minutes + ":" + seconds;
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    newtime = "0" + hours + ":" + minutes + ":0" + seconds;
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    newtime = hours + ":0" + minutes + ":0" + seconds;
                } else if (String.valueOf(hours).length() == 1) {
                    newtime = "0" + hours + ":" + minutes + ":" + seconds;
                } else if (String.valueOf(minutes).length() == 1) {
                    newtime = hours + ":0" + minutes + ":" + seconds;
                } else if (String.valueOf(seconds).length() == 1) {
                    newtime = hours + ":" + minutes + ":0" + seconds;
                } else {
                    newtime = hours + ":" + minutes + ":" + seconds;
                }

                myRef.child(facebookUserId).child("invitations").child("created").child(uuid).child("countDownMinute").setValue(newtime);

                for(int i = 0 ; i < cc.getInvitedFriends().size() ; i++) {

                    myRef.child(cc.getInvitedFriends().get(i).getId()).child("invitations").child("received").child(cc.getUuid()).child("countDownMinute").setValue(newtime);
                }
            }

            public void onFinish() {

            }
        }.start();

    }

    public void initilizeOtherComponentsInvitations(View view) {
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addInvitationButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddInvitationDialog(v);
            }
        });
        countDownTextView = (TextView) view.findViewById(R.id.countdowntime_textview);

    }

    public void initializeInvitationRecycleView(View view) {
        invitationRecycleView = (RecyclerView) view.findViewById(R.id.invitationRecycleView);
        invitationRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        invitationRecycleView.setItemAnimator(new DefaultItemAnimator());
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
//        addedFriendsRecycleView.setItemAnimator(new DefaultItemAnimator());
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
//        showFriendsRecycleView.setItemAnimator(new DefaultItemAnimator());
        showFriendsRecycleView.addItemDecoration(new DividerItemDecoration(dialog.getContext(), LinearLayoutManager.VERTICAL));
    }


    private class DownloadTasks extends AsyncTask<Void, Void, Void> {

        private void loadFacebookFriends() {
            friendPresenter.clearAddedList();

        /* make the API call */
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/friends",
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


        private void loadCreatedInvitations() {

            final DatabaseReference createdInvitationRef = database.getReference(facebookUserId).child("invitations").child("created");
            createdInvitationRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    GenericTypeIndicator<ArrayList<Friend>> t = new GenericTypeIndicator<ArrayList<Friend>>() {};

//                    Log.d("Testing retreiving data", dataSnapshot.child("friends").getValue().getClass().toString());
//                    ArrayList<Friend> friends = (ArrayList<Friend>) dataSnapshot.child("friends").getValue(t);
//                    Log.d("Testing retreiving data", friends.get(0).getName());

                    final CreatedInvitation createdInvitation = dataSnapshot.getValue(CreatedInvitation.class);
//                    createdInvitation.setInvitedFriends(friends);
                    if(!invitationPresenter.getCreatedInvitationList().contains(createdInvitation)){
                        invitationPresenter.addToCreatedInvitation(createdInvitation);
                    }
                    try {
                        if(createdInvitation.getTimeoutTime().getTime() - Calendar.getInstance().getTime().getTime() > 0) {
                            new CountDownTimer(createdInvitation.getTimeoutTime().getTime() - Calendar.getInstance().getTime().getTime(), 1000) {

                                public void onTick(long millisUntilFinished) {

                                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                    String newtime = hours + ":" + minutes + ":" + seconds;
                                    if (newtime.equals("0:0:0")) {
                                        newtime = "00:00:00";
                                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                        newtime = "0" + hours + ":0" + minutes + ":0" + seconds;
                                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                                        newtime = "0" + hours + ":0" + minutes + ":" + seconds;
                                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                        newtime = "0" + hours + ":" + minutes + ":0" + seconds;
                                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                        newtime = hours + ":0" + minutes + ":0" + seconds;
                                    } else if (String.valueOf(hours).length() == 1) {
                                        newtime = "0" + hours + ":" + minutes + ":" + seconds;
                                    } else if (String.valueOf(minutes).length() == 1) {
                                        newtime = hours + ":0" + minutes + ":" + seconds;
                                    } else if (String.valueOf(seconds).length() == 1) {
                                        newtime = hours + ":" + minutes + ":0" + seconds;
                                    } else {
                                        newtime = hours + ":" + minutes + ":" + seconds;
                                    }

                                    myRef.child(facebookUserId).child("invitations").child("created").child(createdInvitation.getUuid()).child("countDownMinute").setValue(newtime);

                                    for(int i = 0 ; i < createdInvitation.getInvitedFriends().size() ; i++) {

                                        myRef.child(createdInvitation.getInvitedFriends().get(i).getId()).child("invitations").child("received").child(createdInvitation.getUuid()).child("countDownMinute").setValue(newtime);
                                    }
                                }

                                public void onFinish() {

                                }
                            }.start();
                        }
                    } catch(NullPointerException e) {
                        e.printStackTrace();
                    }

                    invitationPresenter.displayRecycleView();
//                Log.d("Test retreiving db", createdInvitation.getInvitedFriends().size()+"");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    GenericTypeIndicator<ArrayList<Friend>> t = new GenericTypeIndicator<ArrayList<Friend>>() {};
//                    ArrayList<Friend> friends = (ArrayList<Friend>) dataSnapshot.child("friends").getValue(t);

                    CreatedInvitation createdInvitation = dataSnapshot.getValue(CreatedInvitation.class);
//                    createdInvitation.setInvitedFriends(friends);
                    invitationPresenter.updateCreatedInvitation(createdInvitation);
                    invitationPresenter.displayRecycleView();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void loadReceivedInvitations() {

            final DatabaseReference createdInvitationRef = database.getReference(facebookUserId).child("invitations").child("received");
            createdInvitationRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("receiving testing", "o");
                    final ReceivedInvitation receivedInvitation = dataSnapshot.getValue(ReceivedInvitation.class);
                    if((!invitationPresenter.getReceivedInvitationList().contains(receivedInvitation)) ){
                        invitationPresenter.addToReceivedInvitation(receivedInvitation);
                    }
                    if(receivedInvitation.getTimeoutTime().getTime() - Calendar.getInstance().getTime().getTime() > 0) {
                        new CountDownTimer(receivedInvitation.getTimeoutTime().getTime() - Calendar.getInstance().getTime().getTime(), 1000) {

                            public void onTick(long millisUntilFinished) {

                                int seconds = (int) (millisUntilFinished / 1000) % 60;
                                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                String newtime = hours + ":" + minutes + ":" + seconds;
                                if (newtime.equals("0:0:0")) {
                                    newtime = "00:00:00";
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    newtime = "0" + hours + ":0" + minutes + ":0" + seconds;
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                                    newtime = "0" + hours + ":0" + minutes + ":" + seconds;
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    newtime = "0" + hours + ":" + minutes + ":0" + seconds;
                                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    newtime = hours + ":0" + minutes + ":0" + seconds;
                                } else if (String.valueOf(hours).length() == 1) {
                                    newtime = "0" + hours + ":" + minutes + ":" + seconds;
                                } else if (String.valueOf(minutes).length() == 1) {
                                    newtime = hours + ":0" + minutes + ":" + seconds;
                                } else if (String.valueOf(seconds).length() == 1) {
                                    newtime = hours + ":" + minutes + ":0" + seconds;
                                } else {
                                    newtime = hours + ":" + minutes + ":" + seconds;
                                }

                                myRef.child(facebookUserId).child("invitations").child("received").child(receivedInvitation.getUuid()).child("countDownMinute").setValue(newtime);
                            }

                            public void onFinish() {

                            }
                        }.start();
                    }
                    invitationPresenter.displayRecycleView();
//                Log.d("Test retreiving db", invitationPresenter.getCreatedInvitationList().get(0).getTitle());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    ReceivedInvitation receivedInvitation = dataSnapshot.getValue(ReceivedInvitation.class);
                    invitationPresenter.updateReceivedInvitation(receivedInvitation);
                    invitationPresenter.displayRecycleView();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        protected Void doInBackground(Void... params) {
            loadFacebookFriends();
            loadCreatedInvitations();
            loadReceivedInvitations();

            return null;
        }
    }

}
