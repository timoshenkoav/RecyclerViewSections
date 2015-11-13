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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ContactsAdapter(this));
    }

    private class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {
        private List<Contact> mContacts;

        public ContactsAdapter(Context pContext) {
            mContacts = ContactsProvider.load(pContext);
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(getApplicationContext()).inflate(R.layout.contact_item, null);
            return new ContactViewHolder(content);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            holder.bind(getItem(position));
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

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Contact pItem) {
            mName.setText(pItem.getName());
        }
    }
}
