package coms309.mainproject.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.MainMenu;
import coms309.mainproject.R;

/**
 * Activity class for displaying user's list of contacts in alphabetical order of last names.
 *
 * @author christine
 */
public class ContactsActivity extends FragmentActivity {
    /**
     * A collection of the current user's contacts.
     */
    public ArrayList<Contact> contacts = new ArrayList<>();

    /**
     * The userID of the current user.
     */
    private static int userID;


    /**
     * The URL to get all contacts of the current user.
     */
    private static final String getAllContactsURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/get_all_contacts.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        userID = (getSharedPreferences(
                "UserInfo", MODE_PRIVATE)).getInt("userID",0);

        loadContacts();
    }

    /**
     * Makes a request to the server to get all contacts relevant to the current user.
     */
    public void loadContacts() {
        final RequestQueue controller = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, getAllContactsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0 && response != null) {
                            spliceContacts(response);
                            if (contacts.size() > 0) {
                                makeContactsListView(contactsToHashMap());
                            }
                            Log.d("UserID", "UserID: " + userID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        // error
                        Log.d("Error.Response:", error == null ? "Null" : "NotNull");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put("userID", "" + userID);

                return params;
            }
        };
        controller.add(request);
    }

    /**
     * Splits up contacts information by row and then by column.
     * Creates new contact instances.
     * @param res the String returned by the server's response
     */
    private void spliceContacts(String res)
    {
        // Split by row where each row is one contact.
        String[] contactsInfo = res.split("\n");

        for (String individualContactInfo : contactsInfo) {
            // Split by column where columns are the contact's first name, last name, phone number,
            // and email.
            String[] cols = individualContactInfo.split("\\s");

            Contact contact = new Contact(cols[0], cols[1], cols[2], cols[3]);
            contacts.add(contact);
        }
        Collections.sort(contacts);
    }

    /**
     * Helper method to organize the list of contacts into a hashmap with the keys being the first
     *  letter of all of the last names existing in the list of contacts and the values being the
     *  every last name that belongs to that letter.
     */
    private Map<String, ArrayList<Contact>> contactsToHashMap()
    {
        ArrayList<String> lastNameLetters = new ArrayList<>();
        for (Contact contact: contacts) {
            String lastNameLetter = contact.lastName.substring(0,1);
            lastNameLetters.add(lastNameLetter);
        }
        removeDuplicates(lastNameLetters);
        Map<String, ArrayList<Contact>> contactsByLastName =
                new HashMap<String, ArrayList<Contact>>();
        for (String letter: lastNameLetters) {
            ArrayList<Contact> contactsForLastNameLetter = new ArrayList<>();
            for (Contact contact: contacts) {
                String lastNameLetter = contact.lastName.substring(0,1);
                if (lastNameLetter.equals(letter)) {
                    contactsForLastNameLetter.add(contact);
                }
            }
            contactsByLastName.put(letter, contactsForLastNameLetter);
        }

        return contactsByLastName;

    }

    /**
     * Helper method to make dynamically create the list of contacts view in order of last name.
     */
    private void makeContactsListView(Map<String, ArrayList<Contact>> contactsByLastName)
    {
        final LinearLayout listOfContacts = (LinearLayout) findViewById(R.id.list_of_contacts);
        LinearLayout.LayoutParams params;

        // Keys of HashMap, the distinct first-letters of last names, are not exactly in order so
        // sort them.
        Object[] contactsByLastNameKeys = contactsByLastName.keySet().toArray();
        Arrays.sort(contactsByLastNameKeys);

        for (Object key: contactsByLastNameKeys) {
            ArrayList<Contact> contactsForSpecificLetter = contactsByLastName.get(key);

            // Overarching layout for contacts for a specific last name letter.
            // Includes title for letter and all contacts that start with that letter.
            LinearLayout specificLetterLayout = new LinearLayout(this);
            specificLetterLayout.setOrientation(LinearLayout.VERTICAL);
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            specificLetterLayout.setLayoutParams(params);

            TextView specificLetterTitle = new TextView(this);
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,0,0,0);
            specificLetterTitle.setPadding(0,0,0,10);
            specificLetterTitle.setText(key.toString());
            specificLetterTitle.setTextSize(24);

            specificLetterLayout.addView(specificLetterTitle);

            // The layout for all of the contacts that start with the corresponding letter.
            LinearLayout contactsForSpecificLetterLayout = new LinearLayout(this);
            contactsForSpecificLetterLayout.setOrientation(LinearLayout.VERTICAL);
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactsForSpecificLetterLayout.setLayoutParams(params);
            contactsForSpecificLetterLayout.setPadding(0,0,0,20);

            for (Contact contact: contactsForSpecificLetter) {
                final Contact currentContact = contact;
                // LinearLayout for one contact.
                LinearLayout individualContactLayout = new LinearLayout(this);
                params = new LinearLayout.LayoutParams(800, LinearLayout.LayoutParams.WRAP_CONTENT);
                individualContactLayout.setLayoutParams(params);
                individualContactLayout.setOrientation(LinearLayout.HORIZONTAL);
                individualContactLayout.setClickable(true);
                individualContactLayout.setBackgroundResource(R.drawable.customborder);
                individualContactLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String contactInfo = currentContact.toString();
                        showIndividualContact(v, contactInfo);
                    }
                });

                // Image for one contact.
                ImageView contactIcon = new ImageView(this);
                contactIcon.setImageResource(R.mipmap.ic_person);

                // TextView for one contact's first and last name.
                TextView contactNameTextView = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                // setMargins: left top right bottom
                params.setMargins(10, 10, 0, 10);
                params.gravity = Gravity.CENTER;
                contactNameTextView.setLayoutParams(params);
                contactNameTextView.setTextSize(14);
                String contactName = contact.firstName + " " + contact.lastName;
                contactNameTextView.setText(contactName);

                individualContactLayout.addView(contactIcon);
                individualContactLayout.addView(contactNameTextView);

                contactsForSpecificLetterLayout.addView(individualContactLayout);
            }

            specificLetterLayout.addView(contactsForSpecificLetterLayout);

            listOfContacts.addView(specificLetterLayout);
        }
    }

    /**
     * Helper method to get distinct letters of last names.
     * @param lastNameLetters a list of all first-letters of last names present in the list of
     *  contacts
     */
    private static void removeDuplicates(ArrayList<String> lastNameLetters) {
        ArrayList<String> lastNameLettersNoDuplicates = new ArrayList<>();
        for (String lastNameLetter: lastNameLetters) {
            if (!lastNameLettersNoDuplicates.contains(lastNameLetter)) {
                lastNameLettersNoDuplicates.add(lastNameLetter);
            }
        }
        lastNameLetters.clear();
        lastNameLetters.addAll(lastNameLettersNoDuplicates);
    }

    /**
     * Starts MainMenu Activity to go back to main menu screen.
     * @param view the current view
     */
    public void goBackToMainMenu(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /**
     * Starts IndividualContact Activity to go to individual contact screen.
     * @param view the current view
     * @param contactInfo a String of the chosen contact's information
     */
    public void showIndividualContact(View view, String contactInfo) {
        Intent intent = new Intent(this, IndividualContact.class);

        intent.putExtra("contactInfo", contactInfo);
        startActivity(intent);
    }

    /**
     * Starts AddContact Activity to go to add contact screen.
     * @param view the current view
     */
    public void addContact(View view) {
        Intent intent = new Intent(this, AddContact.class);
        startActivity(intent);
    }
}
