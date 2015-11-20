package com.tunebrains.recyclerviewfastscroll;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tunebrains.rcfastscroll.IndexLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private IndexLayoutManager mIndexLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ContactsAdapter(this));

        mIndexLayoutManager = (IndexLayoutManager) findViewById(R.id.index_layout);
        mIndexLayoutManager.attach(mRecyclerView);
    }

    private class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {
        private List<Contact> mContacts;
        private LinkedHashMap<String, Integer> mMapIndex;
        private ArrayList<String> mSectionList;
        private String[] mSections;

        public ContactsAdapter(Context pContext) {
            mContacts = ContactsProvider.load(pContext);
            fillSections();
        }

        private void fillSections() {
            mMapIndex = new LinkedHashMap<String, Integer>();

            for (int x = 0; x < mContacts.size(); x++) {
                String fruit = mContacts.get(x).getName();
                if (fruit.length() > 1) {
                    String ch = fruit.substring(0, 1);
                    ch = ch.toUpperCase();
                    if (!mMapIndex.containsKey(ch)) {
                        mMapIndex.put(ch, x);
                    }
                }
            }
            Set<String> sectionLetters = mMapIndex.keySet();
            // create a list from the set to sort
            mSectionList = new ArrayList<String>(sectionLetters);
            Collections.sort(mSectionList);

            mSections = new String[mSectionList.size()];
            mSectionList.toArray(mSections);
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.contact_item, null);
            return new ContactViewHolder(content);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            Contact lContact = getItem(position);
            String section = getSection(lContact);
            holder.bind(lContact, section, mMapIndex.get(section) == position);
        }

        private String getSection(Contact pContact) {
            return pContact.getName().substring(0, 1).toUpperCase();
        }

        private Contact getItem(int pPosition) {
            return mContacts.get(pPosition);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    private class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final TextView mSectionName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSectionName = (TextView) itemView.findViewById(R.id.section_title);

        }

        public void bind(Contact pItem, String pSection, boolean bShowSection) {
            mName.setText(pItem.getName());
            mSectionName.setText(pSection);
            mSectionName.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        }
    }
}
