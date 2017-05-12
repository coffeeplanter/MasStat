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

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;

import java.util.ArrayList;
import java.util.List;

import ru.coffeeplanter.masstat.R;
import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Keyword;

/**
 * Fragment for the list of persons.
 */

public class PersonListFragment extends Fragment {

    private FloatingActionButton mAddPersonFab;
    private TextView mEmptyListTextView;
    private RecyclerView mPersonRecyclerView;
    private PersonAdapter mPersonAdapter;
    private List<Person> mPersonList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        mEmptyListTextView = (TextView) view.findViewById(R.id.empty_person_list_textview);

        mPersonRecyclerView = (RecyclerView) view.findViewById(R.id.person_list_recyclerview);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Chip> chipList1 = new ArrayList<>();
        chipList1.add(new Keyword("Lorem"));
        chipList1.add(new Keyword("Ipsum dolor"));
        chipList1.add(new Keyword("Sit amet"));
        List<Chip> chipList2 = new ArrayList<>();
        chipList2.add(new Keyword("Consectetur"));
        chipList2.add(new Keyword("adipiscing elit"));
        chipList2.add(new Keyword("Consectetur"));
        chipList2.add(new Keyword("adipiscing elit"));
        chipList2.add(new Keyword("Consectetur"));
        chipList2.add(new Keyword("adipiscing elit"));
        chipList2.add(new Keyword("Consectetur"));
        chipList2.add(new Keyword("adipiscing elit"));

        mPersonList = new ArrayList<>();
        mPersonList.add(new Person("Путин В. В."));
        mPersonList.get(0).setKeywords(chipList1);
        mPersonList.add(new Person("Дмитрий Анатольевич Медведев"));
        mPersonList.get(1).setKeywords(chipList2);
        mPersonList.add(new Person("Навальный"));
        mPersonList.get(2).setKeywords(chipList1);

        if (mPersonAdapter == null) {
            mPersonAdapter = new PersonAdapter(mPersonList);
        }
        mPersonRecyclerView.setAdapter(mPersonAdapter);

        chooseViewToShow();

        mAddPersonFab = (FloatingActionButton) view.findViewById(R.id.add_person_fab);
        mAddPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_EDIT, null)
                        .show(getFragmentManager(), null);
            }
        });

        return view;

    }

    // Shows TextView when person list is empty.
    private void chooseViewToShow() {
        if (mPersonAdapter.getItemCount() == 0) {
            mPersonRecyclerView.setVisibility(View.GONE);
            mEmptyListTextView.setVisibility(View.VISIBLE);
        } else {
            mPersonRecyclerView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setVisibility(View.GONE);
        }
    }

    private class PersonHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mPersonIcon, mPersonName;
        private ChipView mChipView;
        private ImageButton mDeleteButton;

        public PersonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.person_list_item, parent, false));
            mCardView = (CardView) itemView.findViewById(R.id.person_list_cardview_recyclerview);
            mPersonIcon = (TextView) itemView.findViewById(R.id.person_icon_textview_recyclerview);
            mPersonName = (TextView) itemView.findViewById(R.id.person_name_textview_recyclerview);
            mChipView = (ChipView) itemView.findViewById(R.id.chipview_recyclerview);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_person_imagebutton_recyclerview);
        }

        void bindSite(final Person person) {
            String name = person.getName();
            if ((name != null) && (name.length() > 0)) {
                mPersonIcon.setText(name.substring(0, 1).toUpperCase());
            }
            mPersonName.setText(name);
            mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_EDIT, person)
                            .show(getFragmentManager(), null);
                    return true;
                }
            });

            mChipView.setChipList(person.getKeywords());
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_DELETE, null)
                            .show(getFragmentManager(), null);
                }
            });
        }

    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {

        private List<Person> mPersonList;

        public PersonAdapter(List<Person> persons) {
            mPersonList = persons;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PersonHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person person = mPersonList.get(position);
            holder.bindSite(person);

        }

        @Override
        public int getItemCount() {
            return mPersonList.size();
        }

    }

}
