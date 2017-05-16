package ru.coffeeplanter.masstat.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.plumillonforge.android.chipview.Chip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.coffeeplanter.masstat.R;
import ru.coffeeplanter.masstat.dao.AdminDao;
import ru.coffeeplanter.masstat.dao.AdminDaoImpl;
import ru.coffeeplanter.masstat.entities.Keyword;
import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;
import ru.coffeeplanter.masstat.net.NetCallback;

/**
 * Dialog fragment for data editing and delete confirmation.
 */

public class EditDataDialogFragment extends DialogFragment {

    private static final String ARG_TYPE = "dialog_type";
    private static final String ARG_ENTITY = "data_entity";
    private static final String ARG_CALLBACK = "callback";

    static final int TYPE_SITE_EDIT = 10;
    static final int TYPE_SITE_EDIT_CONFIRM = 20;
    static final int TYPE_SITE_ADD_CONFIRM = 30;
    static final int TYPE_SITE_DELETE = 40;
    static final int TYPE_PERSON_EDIT = 50;
    static final int TYPE_PERSON_EDIT_CONFIRM = 60;
    static final int TYPE_PERSON_ADD_CONFIRM = 70;
    static final int TYPE_PERSON_DELETE = 80;

    private final String TAG = "EditDataDialogFragment";

    private AdminDao mDao;
    private NetCallback mCallback;

    public static EditDataDialogFragment newInstance(int type, @Nullable Object entity, @Nullable NetCallback callback) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        if (entity != null) {
            args.putSerializable(ARG_ENTITY, (Serializable) entity);
        }
        if (callback != null) {
            args.putParcelable(ARG_CALLBACK, callback);
        }
        EditDataDialogFragment fragment = new EditDataDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        DialogInterface.OnClickListener listener = null;

        if (getArguments().containsKey(ARG_CALLBACK)) {
            mCallback = getArguments().getParcelable(ARG_CALLBACK);
            mDao = new AdminDaoImpl(getActivity().getApplicationContext());
        }

        if (getArguments().containsKey(ARG_TYPE)) {
            int type = getArguments().getInt(ARG_TYPE);
            switch (type) {
                case TYPE_SITE_EDIT:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_site_edit, null);
                    final EditText siteName = (EditText) view.findViewById(R.id.site_name_edittext_dialog);
                    final EditText baseUrl = (EditText) view.findViewById(R.id.base_url_edittext_dialog);
                    final EditText openTag = (EditText) view.findViewById(R.id.open_tag_edittext_dialog);
                    final EditText closeTag = (EditText) view.findViewById(R.id.close_tag_edittext_dialog);
                    final Site site;
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        site = (Site) getArguments().getSerializable(ARG_ENTITY);
                        if (site != null) {
                            siteName.setText(site.getName());
                            baseUrl.setText(site.getBaseUrl());
                            openTag.setText(site.getOpenTag());
                            closeTag.setText(site.getCloseTag());
                        }
                    } else {
                        site = new Site();
                    }
                    builder.setView(view).setTitle("Edit site parameters");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Site site = new Site();
                            assert site != null;
                            site.setName(siteName.getText().toString());
                            site.setBaseUrl(baseUrl.getText().toString());
                            site.setOpenTag(openTag.getText().toString());
                            site.setCloseTag(closeTag.getText().toString());
                            if (getArguments().containsKey(ARG_ENTITY)) {
                                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT_CONFIRM, site, mCallback)
                                        .show(getFragmentManager(), null);
                            } else {
                                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_ADD_CONFIRM, site, mCallback)
                                        .show(getFragmentManager(), null);
                            }
                        }
                    };
                    break;
                case TYPE_SITE_EDIT_CONFIRM:
                    builder.setMessage("Save changed site parameters in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getArguments().containsKey(ARG_ENTITY)) {
                                Site site = (Site) getArguments().getSerializable(ARG_ENTITY);
                                mDao.saveSite(site, mCallback);
                            }
                        }
                    };
                    break;
                case TYPE_SITE_ADD_CONFIRM:
                    builder.setMessage("Add new site in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getArguments().containsKey(ARG_ENTITY)) {
                                Site site = (Site) getArguments().getSerializable(ARG_ENTITY);
                                mDao.addSite(site, mCallback);
                            }
                        }
                    };
                    break;
                case TYPE_SITE_DELETE:
                    final Site siteToDelete;
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        siteToDelete = (Site) getArguments().getSerializable(ARG_ENTITY);
                        assert siteToDelete != null;
                        builder.setMessage("Delete the site \"" + siteToDelete.getName() + "\" and all related data in remote database?");
                    } else {
                        siteToDelete = null;
                    }
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (siteToDelete != null) {
                                mDao.deleteSite(siteToDelete.getId(), mCallback);
                            }
                        }
                    };
                    break;
                case TYPE_PERSON_EDIT:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_person_edit, null);
                    final EditText personName = (EditText) view.findViewById(R.id.person_name_edittext_dialog);
                    final EditText personKeywords = (EditText) view.findViewById(R.id.keywords_list_edittext_dialog);
                    final Person person;
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        person = (Person) getArguments().getSerializable(ARG_ENTITY);
                        if (person != null) {
                            personName.setText(person.getName());
                            StringBuilder keywordsString = new StringBuilder();
                            List<Chip> keywords = person.getKeywords();
                            for (int i = 0; i < keywords.size(); i++) {
                                keywordsString.append((keywords.get(i)).getText());
                                if (i < keywords.size() - 1) {
                                    keywordsString.append(", ");
                                }
                            }
                            personKeywords.setText(keywordsString.toString());
                        }
                    } else {
                        person = new Person();
                    }
                    builder.setView(view).setTitle("Edit person parameters");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            assert person != null;
                            person.setName(personName.getText().toString());
                            String keywordWholeString = personKeywords.getText().toString();

                            List<Keyword> keywordListToAdd = new ArrayList<>();
                            if (!keywordWholeString.isEmpty()) {
                                String[] keywordArray = keywordWholeString.split(",");
                                for (int i = 0; i < keywordArray.length; i++) {
                                    keywordArray[i] = keywordArray[i].trim();
                                }
                                HashSet<String> keywordSet = new HashSet<>();
                                Collections.addAll(keywordSet, keywordArray);
                                for (String s : keywordSet) {
                                    Keyword k = new Keyword(s.trim());
                                    k.setPersonId(person.getId());
                                    keywordListToAdd.add(k);
                                }
                            }

                            if (getArguments().containsKey(ARG_ENTITY)) {

                                List<Keyword> keywordListToDelete = new ArrayList<>();
                                if (!person.getKeywords().isEmpty()) {
                                    for (Chip chip : person.getKeywords()) {
                                        keywordListToDelete.add((Keyword) chip);
                                    }
                                }

                                for (int i = keywordListToDelete.size() - 1; i >= 0; i--) {
                                    for (int j = keywordListToAdd.size() - 1; j >= 0; j--) {
                                        Log.d(TAG, "keywordListToAdd: " + keywordListToAdd.get(j).getName() + " keywordListToDelete: " + keywordListToDelete.get(i).getName());
                                        if (keywordListToAdd.get(j).getName().equals(keywordListToDelete.get(i).getName())) {
                                            keywordListToDelete.remove(i);
                                            keywordListToAdd.remove(j);
                                            break;
                                        }
                                    }
                                }

                                List<Chip> chipListToAdd = new ArrayList<>();
                                if (!keywordListToAdd.isEmpty()) {
                                    for (Keyword k : keywordListToAdd) {
                                        chipListToAdd.add(k);
                                    }
                                }

                                List<Chip> chipListToDelete = new ArrayList<>();
                                if (!keywordListToDelete.isEmpty()) {
                                    for (Keyword k : keywordListToDelete) {
                                        chipListToDelete.add(k);
                                    }
                                }

                                person.setKeywords(chipListToDelete);
                                person.setAdditionalKeywords(chipListToAdd);

                                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_EDIT_CONFIRM, person, mCallback)
                                        .show(getFragmentManager(), null);
                            } else {
                                List<Chip> chipListToAdd = new ArrayList<>();
                                if (!keywordListToAdd.isEmpty()) {
                                    for (Keyword k : keywordListToAdd) {
                                        chipListToAdd.add(k);
                                    }
                                }
                                person.setKeywords(chipListToAdd);
                                EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_ADD_CONFIRM, person, mCallback)
                                        .show(getFragmentManager(), null);
                            }
                        }
                    };
                    break;
                case TYPE_PERSON_EDIT_CONFIRM:
                    builder.setMessage("Save changed person parameters in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getArguments().containsKey(ARG_ENTITY)) {
                                Person person = (Person) getArguments().getSerializable(ARG_ENTITY);
                                mDao.savePerson(person, mCallback);
                            }
                        }
                    };
                    break;
                case TYPE_PERSON_ADD_CONFIRM:
                    builder.setMessage("Add new person in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getArguments().containsKey(ARG_ENTITY)) {
                                Person person = (Person) getArguments().getSerializable(ARG_ENTITY);
                                mDao.addPerson(person, mCallback);
                            }
                        }
                    };
                    break;
                case TYPE_PERSON_DELETE:
                    final Person personToDelete;
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        personToDelete = (Person) getArguments().getSerializable(ARG_ENTITY);
                        assert personToDelete != null;
                        builder.setMessage("Delete the person \"" + personToDelete.getName() + "\" and all related data in remote database?");
                    } else {
                        personToDelete = null;
                    }
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (personToDelete != null) {
                                mDao.deletePerson(personToDelete.getId(), mCallback);
                            }
                        }
                    };
                    break;
                default:
                    return super.onCreateDialog(savedInstanceState);
            }
        }

        builder
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Cancel action.
                    }
                });

        return builder.create();

    }

}
