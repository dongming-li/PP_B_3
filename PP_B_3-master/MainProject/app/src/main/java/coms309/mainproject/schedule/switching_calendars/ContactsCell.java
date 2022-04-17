package coms309.mainproject.schedule.switching_calendars;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import coms309.mainproject.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactCellListener} interface
 * to handle interaction events.
 * Use the {@link ContactsCell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsCell extends Fragment {

    private int activeIndex = 1;

    private ContactCellListener mListener;

    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactsCell() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsCell.
     */
    public static ContactsCell newInstance() {
        ContactsCell fragment = new ContactsCell();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts_cell, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout list = (LinearLayout) getView().findViewById(R.id.contactsList);

        //Button back to regular calendar
        Button b2 = new Button(getContext());
        b2.setTransformationMethod(null);
        b2.setTextSize(16);
        String text = "--MY CALENDAR--";
        b2.setText(text);

        list.addView(b2);

        //Button listener
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activeIndex = getContext().getSharedPreferences("UserInfo", MODE_PRIVATE).getInt("userID", 1);

                mListener.switchCalendar(activeIndex);
            }
        });

        for (Contact c : contacts) {

            Button b = new Button(getContext());
            b.setTransformationMethod(null);
            b.setTextSize(16);
            b.setText(c.userName);

            list.addView(b);

            final int index = c.userID;

            //Button listener
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    activeIndex = index; //set correct user ID

                    mListener.switchCalendar(activeIndex);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactCellListener) {
            mListener = (ContactCellListener) context;

            mListener.loadContacts(contacts);

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ContactCellListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface to interact with parent activity
     */
    public interface ContactCellListener {
        void loadContacts(ArrayList<Contact> contacts);

        void switchCalendar(int id);
    }
}
