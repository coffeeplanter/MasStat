package ru.coffeeplanter.masstat.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.coffeeplanter.masstat.MasStatApp;
import ru.coffeeplanter.masstat.R;
import ru.coffeeplanter.masstat.dao.AdminDao;
import ru.coffeeplanter.masstat.dao.AdminDaoImpl;
import ru.coffeeplanter.masstat.dao.LocalStorage;
import ru.coffeeplanter.masstat.dao.LocalStorageImpl;
import ru.coffeeplanter.masstat.entities.Site;
import ru.coffeeplanter.masstat.net.NetCallback;

/**
 * Fragment for the list of sites.
 */

public class SiteListFragment extends Fragment {

    private final String TAG = "SiteListFragment";

    private FloatingActionButton mAddSiteFab;
    private TextView mEmptyListTextView;
    private RecyclerView mSiteRecyclerView;
    private SiteAdapter mSiteAdapter;
    private List<Site> mSiteList;
    private AdminDao mDao;
    private NetCallback mOnSitesGet;
    private LocalStorage mLocalStorage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSiteList = new ArrayList<>();
        mLocalStorage = new LocalStorageImpl(getActivity().getApplicationContext());
        setOnSitesGetListener();
        mDao = new AdminDaoImpl(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site_list, container, false);

        mEmptyListTextView = (TextView) view.findViewById(R.id.empty_site_list_textview);

        mSiteRecyclerView = (RecyclerView) view.findViewById(R.id.site_list_recyclerview);
        mSiteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mSiteAdapter == null) {
            mSiteAdapter = new SiteAdapter(mSiteList);
        }
        mSiteRecyclerView.setAdapter(mSiteAdapter);

        chooseViewToShow();


        mAddSiteFab = (FloatingActionButton) view.findViewById(R.id.add_site_fab);
        mAddSiteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT, null, mOnSitesGet)
                        .show(getFragmentManager(), null);
            }
        });

        mDao.getSites(mOnSitesGet); // TODO: Try to pass it to onAttach.

        return view;

    }

    private void chooseViewToShow() {
        if (mSiteAdapter.getItemCount() == 0) {
            mSiteRecyclerView.setVisibility(View.GONE);
            mEmptyListTextView.setVisibility(View.VISIBLE);
        } else {
            mSiteRecyclerView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
        }
    }

    private void setOnSitesGetListener() {
        mOnSitesGet = new NetCallback() {
            @Override
            public void success() {
                List<Site> sites = mLocalStorage.readSites();
                mSiteList.clear();
                if (sites != null) {
                    mSiteList.addAll(sites);
                }
                mSiteAdapter.notifyDataSetChanged();
                chooseViewToShow();
                Log.d(TAG, "Just got from server: " + mSiteList.toString());
                for (Site site : mSiteList) {
                    Log.d(TAG, "Just got from server: " + new Gson().toJson(site));
                }
                if (getView() != null) {
//                    Snackbar.make(getView(), "Site list received from server", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Toast.makeText(getActivity(), "Site list received from server", Toast.LENGTH_SHORT).show();
                    // TODO: Add one callback type for all actions.
                }
            }

            @Override
            public void failure() {
                List<Site> sites = mLocalStorage.readSites();
                mSiteList.clear();
                if (sites != null) {
                    mSiteList.addAll(sites);
                }
                mSiteAdapter.notifyDataSetChanged();
                chooseViewToShow();
                if (getView() != null) {
                    if (sites != null) {
//                        Snackbar.make(getView(), "Can't connect to server. Site list read from local storage", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        Toast.makeText(getActivity(), "Can't connect to server. Site list read from local storage", Toast.LENGTH_SHORT).show();
                    } else {
//                        Snackbar.make(getView(), "Can't connect to server, and local storage is empty", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        Toast.makeText(getActivity(), "Can't connect to server, and local storage is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }
        };
    }

    private class SiteHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mSiteIcon, mSiteName, mSiteUrl;
        private ImageButton mDeleteButton;

        public SiteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.site_list_item, parent, false));
            mCardView = (CardView) itemView.findViewById(R.id.site_list_cardview_recyclerview);
            mSiteIcon = (TextView) itemView.findViewById(R.id.site_icon_textview_recyclerview);
            mSiteName = (TextView) itemView.findViewById(R.id.site_name_textview_recyclerview);
            mSiteUrl = (TextView) itemView.findViewById(R.id.site_url_textview_recyclerview);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_site_imagebutton_recyclerview);
        }

        void bindSite(final Site site) {
            String name = site.getName();
            if ((name != null) && (name.length() > 0)) {
                mSiteIcon.setText(name.substring(0, 1).toUpperCase());
            }
            mSiteName.setText(name);
            mSiteUrl.setText(site.getBaseUrl());
            mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT, site, mOnSitesGet)
                            .show(getFragmentManager(), null);
                    return true;
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_DELETE, site, mOnSitesGet)
                            .show(getFragmentManager(), null);
                }
            });
        }

    }

    private class SiteAdapter extends RecyclerView.Adapter<SiteHolder> {

        private List<Site> mSiteList;

        public SiteAdapter(List<Site> sites) {
            mSiteList = sites;
        }

        @Override
        public SiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SiteHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(SiteHolder holder, int position) {
            Site site = mSiteList.get(position);
            holder.bindSite(site);

        }

        @Override
        public int getItemCount() {
            return mSiteList.size();
        }

    }

}
