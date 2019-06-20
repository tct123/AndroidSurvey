package org.adaptlab.chpir.android.survey.questionfragments;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.opencsv.CSVReader;

import org.adaptlab.chpir.android.survey.BuildConfig;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.SingleQuestionFragment;
import org.adaptlab.chpir.android.survey.models.Option;
import org.adaptlab.chpir.android.survey.models.Response;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class ListOfItemsQuestionFragment extends SingleQuestionFragment {
    private static final String TAG = "ListOfItemsViewHolder";
    public ArrayList<EditText> mResponses;

    protected abstract EditText createEditText();

    protected void createQuestionComponent(ViewGroup questionComponent) {
        mResponses = new ArrayList<>();
        for (Option option : getOptions()) {
            final TextView optionText = new TextView(getActivity());
            optionText.setText(getOptionText(option));
            questionComponent.addView(optionText);
            EditText editText = createEditText();
            editText.setHint(R.string.free_response_edittext);
            editText.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
            questionComponent.addView(editText);
            mResponses.add(editText);
            editText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setResponse(null);
                }

                // Required by interface
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    if (mSpecialResponses != null && s.length() > 0) {
                        mSpecialResponses.clearCheck();
                    }
                }
            });
        }
    }

    @Override
    protected String serialize() {
        String serialized = "";
        for (int i = 0; i < mResponses.size(); i++) {
            serialized += StringEscapeUtils.escapeCsv(mResponses.get(i).getText().toString());
            if (i < mResponses.size() - 1) serialized += Response.LIST_DELIMITER;
        }
        return serialized;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void deserialize(String responseText) {
        if (responseText.equals("")) return;
        InputStream input = new ByteArrayInputStream(responseText.getBytes(Charsets.UTF_8));
        InputStreamReader inputReader = new InputStreamReader(input);
        CSVReader reader = new CSVReader(inputReader);
        String[] listOfResponses;
        try {
            listOfResponses = reader.readNext();
            for (int i = 0; i < listOfResponses.length; i++) {
                if (mResponses.size() > i)
                    mResponses.get(i).setText(listOfResponses[i]);
            }
        } catch (IOException e) {
            if (BuildConfig.DEBUG) Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    @Override
    protected void unSetResponse() {
        for (EditText oneEditText : mResponses) {
            oneEditText.setText(Response.BLANK);
        }
        setResponseTextBlank();
    }

}
