package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ContactList extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>
         {
    // Define a ListView object
    ListView contactsList;
    // Define variables for the contact the user selects

    // A custom adapter that binds the result Cursor to the ListView
    private ContactsAdapter cursorAdapter;
    public static final int CONTACT_LOADER_ID = 0;

    public ContactList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupCursorAdapter();
        getLoaderManager().initLoader(CONTACT_LOADER_ID,new Bundle(),this);
    }

    private void setupCursorAdapter() {

        // Gets a CursorAdapter
        cursorAdapter = new ContactsAdapter(getActivity());
        contactsList = getActivity().findViewById(R.id.contacts_list_view);
        // Sets the adapter for the ListView
        contactsList.setAdapter(cursorAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String [] projection = {ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};

        return new CursorLoader(this.getActivity(), ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private static class ContactsAdapter extends CursorAdapter
    {
        private int IndexLookupKey;
        private int IndexDisplayName;
        private int IndexPhotoUri;
        Context mContext;


        public ContactsAdapter(Context context)
        {
            super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mContext=context;
        }

        @Override
        public Cursor swapCursor(Cursor newCursor)
        {
            if(newCursor != null){
                IndexLookupKey   = newCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                IndexDisplayName = newCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                IndexPhotoUri    = newCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
            }
            return super.swapCursor(newCursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            return ViewHolder.create(parent,mContext);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            final String lookupKey   = cursor.getString(IndexLookupKey);
            final String displayName = cursor.getString(IndexDisplayName);
            final String photoUri    = cursor.getString(IndexPhotoUri);

            ViewHolder.get(view).bind(displayName, photoUri,
                    Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey));
        }

        private static class ViewHolder
        {
            private ViewGroup mLayout;
            private ImageView mContactPhoto;
            private TextView mContactName;
            private Uri       mLookupUri;
            static Context mContext;


            public static final ViewGroup create(ViewGroup parent, Context context)
            {
                mContext = context;
                return new ViewHolder(parent).mLayout;
            }

            public static final ViewHolder get(View parent)
            {
                return (ViewHolder)parent.getTag();
            }

            private ViewHolder(ViewGroup parent)
            {
                Context context = parent.getContext();
                mLayout = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.contacts_list_item, parent, false);
                mLayout.setTag(this);
                mContactPhoto = mLayout.findViewById(R.id.PhotoUri);
                mContactName  = mLayout.findViewById(R.id.DisplayName);
            }

            public void bind(String displayName, String photoUri, Uri lookupUri)
            {
                if(photoUri != null){
                    mContactPhoto.setImageURI(Uri.parse(photoUri));
                }else{
                    mContactPhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.contactimage));
                }
                mContactName.setText(displayName);
                mLookupUri = lookupUri;
            }


        }
    }
}
