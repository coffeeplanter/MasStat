package ru.coffeeplanter.masstat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.coffeeplanter.masstat.R;
import ru.coffeeplanter.masstat.entities.Site;

/**
 * Fragment for the list of sites.
 */

public class SiteListFragment extends Fragment {

    private FloatingActionButton mAddSiteFab;
    private TextView mEmptyListTextView;
    private RecyclerView mSiteRecyclerView;
    private SiteAdapter mSiteAdapter;
    private List<Site> mSiteList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site_list, container, false);

        mEmptyListTextView = (TextView) view.findViewById(R.id.empty_site_list_textview);

        mSiteRecyclerView = (RecyclerView) view.findViewById(R.id.site_list_recyclerview);
        mSiteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSiteList = new ArrayList<>();
        mSiteList.add(new Site("Лента.ру", "http://www.lenta.ru"));
        mSiteList.add(new Site("Ведомости", "http://www.vedomosti.ru"));
        mSiteList.add(new Site("РБК", "http://www.rbc.ru"));

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
                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT, null)
                        .show(getFragmentManager(), null);
            }
        });

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
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT, site)
                            .show(getFragmentManager(), null);
                    return true;
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_DELETE, null)
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
