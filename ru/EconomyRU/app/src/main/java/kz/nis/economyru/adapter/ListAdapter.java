package kz.nis.economyru.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import kz.nis.economyru.ActivityList;
import kz.nis.economyru.R;
import kz.nis.economyru.extra.FontFactory;

/**
 * Created by myrza on 7/21/15.
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater lInflater;
    private ArrayList<String> questions;
    View.OnFocusChangeListener changeListenerEdit1 = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = ((EditText) v).getText().toString();
                int tag = (int) v.getTag();
                questions.set(tag, text);
                int id = questions.size() - tag;
                ActivityList.dbAdapter.updateQuest(id, text);
            }
        }
    };
    private ArrayList<String> answers;
    View.OnFocusChangeListener changeListenerEdit2 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = ((EditText) v).getText().toString();
                int tag = (int) v.getTag();
                answers.set(tag, text);
                int id = questions.size() - tag;
                ActivityList.dbAdapter.updateAnswer(id, text);
            }
        }
    };
    private ArrayList<Boolean> actives;
    private Typeface typeface;

    public ListAdapter(Context context, ArrayList<String> questions, ArrayList<String> answers, ArrayList<Boolean> actives) {
        this.lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.questions = questions;
        this.answers = answers;
        this.actives = actives;
        this.typeface = FontFactory.getFont1(context);
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public ArrayList<Boolean> getActivies() {
        return actives;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_list, parent, false);
            ((TextView) view.findViewById(R.id.textItemNumber)).setTypeface(typeface);
            ((EditText) view.findViewById(R.id.editTextItemQuest)).setTypeface(typeface);
            ((EditText) view.findViewById(R.id.editTextItemAnswer)).setTypeface(typeface);

        }
        TextView textNumber = (TextView) view.findViewById(R.id.textItemNumber);
        textNumber.setText(getCount()-position+".");
        EditText editQuest = (EditText) view.findViewById(R.id.editTextItemQuest);
        editQuest.setText(questions.get(position));
        editQuest.setOnFocusChangeListener(changeListenerEdit1);
        editQuest.setTag(position);

        EditText editAnswer = (EditText) view.findViewById(R.id.editTextItemAnswer);
        editAnswer.setText(answers.get(position));
        editAnswer.setOnFocusChangeListener(changeListenerEdit2);
        editAnswer.setTag(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxItemActiv);
        checkBox.setTag(position);
        checkBox.setChecked(actives.get(position));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                actives.set(tag, !actives.get(tag));
                int id = questions.size() - tag;
                ActivityList.dbAdapter.updateActiv(id, actives.get(tag));
            }

        });

        return view;
    }
}
