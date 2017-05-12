package ru.coffeeplanter.masstat.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.plumillonforge.android.chipview.Chip;

import java.io.Serializable;
import java.util.List;

import ru.coffeeplanter.masstat.R;
import ru.coffeeplanter.masstat.entities.Keyword;
import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;

/**
 * Dialog fragment for data editing and delete confirmation.
 */

public class EditDataDialogFragment extends DialogFragment {

    private static final String ARG_TYPE = "dialog_type";
    private static final String ARG_ENTITY = "data_entity";

    static final int TYPE_SITE_EDIT = 10;
    static final int TYPE_PERSON_EDIT = 20;
    static final int TYPE_SITE_EDIT_CONFIRM = 30;
    static final int TYPE_PERSON_EDIT_CONFIRM = 40;
    static final int TYPE_SITE_DELETE = 50;
    static final int TYPE_PERSON_DELETE = 60;

    public static EditDataDialogFragment newInstance(int type, @Nullable Object entity) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        if (entity != null) {
            args.putSerializable(ARG_ENTITY, (Serializable) entity);
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

        if (getArguments().containsKey(ARG_TYPE)) {
            int type = getArguments().getInt(ARG_TYPE);
            switch (type) {
                case TYPE_SITE_EDIT:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_site_edit, null);
                    EditText siteName = (EditText) view.findViewById(R.id.site_name_edittext_dialog);
                    EditText baseUrl = (EditText) view.findViewById(R.id.base_url_edittext_dialog);
                    EditText openTag = (EditText) view.findViewById(R.id.open_tag_edittext_dialog);
                    EditText closeTag = (EditText) view.findViewById(R.id.close_tag_edittext_dialog);
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        Site site = (Site) getArguments().getSerializable(ARG_ENTITY);
                        if (site != null) {
                            siteName.setText(site.getName());
                            baseUrl.setText(site.getBaseUrl());
                            openTag.setText(site.getOpenTag());
                            closeTag.setText(site.getCloseTag());
                        }
                    }
                    builder.setView(view).setTitle("Edit site parameters");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Save site parameters actions.
                            EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_SITE_EDIT_CONFIRM, null)
                                    .show(getFragmentManager(), null);
                        }
                    };
                    break;
                case TYPE_PERSON_EDIT:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_person_edit, null);
                    EditText personName = (EditText) view.findViewById(R.id.person_name_edittext_dialog);
                    EditText personKeywords = (EditText) view.findViewById(R.id.keywords_list_edittext_dialog);
                    if (getArguments().containsKey(ARG_ENTITY)) {
                        Person person = (Person) getArguments().getSerializable(ARG_ENTITY);
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
                    }
                    builder.setView(view).setTitle("Edit person parameters");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Save person parameters actions.
                            EditDataDialogFragment.newInstance(EditDataDialogFragment.TYPE_PERSON_EDIT_CONFIRM, null)
                                    .show(getFragmentManager(), null);
                        }
                    };
                    break;
                case TYPE_SITE_EDIT_CONFIRM:
                    builder.setMessage("Save changed site parameters in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Send site update request.
                        }
                    };
                    break;
                case TYPE_PERSON_EDIT_CONFIRM:
                    builder.setMessage("Save changed person parameters in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Send site update request.
                        }
                    };
                    break;
                case TYPE_SITE_DELETE:
                    builder.setMessage("Delete this site and all related data in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Send site delete request.
                        }
                    };
                    break;
                case TYPE_PERSON_DELETE:
                    builder.setMessage("Delete this person and all related data in remote database?");
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: Send person delete request.
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
