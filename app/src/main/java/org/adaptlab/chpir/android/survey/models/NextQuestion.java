package org.adaptlab.chpir.android.survey.models;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "NextQuestions")
public class NextQuestion extends ReceiveModel {
    private static final String TAG = "NextQuestion";

    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "QuestionIdentifier")
    private String mQuestionIdentifier;
    @Column(name = "OptionIdentifier")
    private String mOptionIdentifier;
    @Column(name = "NextQuestionIdentifier")
    private String mNextQuestionIdentifier;
    @Column(name = "RemoteQuestionId")
    private Long mRemoteQuestionId;
    @Column(name = "RemoteInstrumentId")
    private Long mRemoteInstrumentId;

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            NextQuestion nextQuestion = NextQuestion.findByRemoteId(remoteId);
            if (nextQuestion == null) {
                nextQuestion = this;
            }
            nextQuestion.setRemoteId(remoteId);
            nextQuestion.setQuestionIdentifier(jsonObject.getString("question_identifier"));
            nextQuestion.setOptionIdentifier(jsonObject.getString("option_identifier"));
            nextQuestion.setNextQuestionIdentifier(jsonObject.getString("next_question_identifier"));
            nextQuestion.setRemoteQuestionId(jsonObject.getLong("question_id"));
            nextQuestion.setRemoteInstrumentId(jsonObject.getLong("instrument_id"));
            nextQuestion.save();
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }
    }

    public static NextQuestion findByRemoteId(Long id) {
        return new Select().from(NextQuestion.class).where("RemoteId = ?", id).executeSingle();
    }

    public String getNextQuestionIdentifier() {
        return mNextQuestionIdentifier;
    }

    private void setRemoteId(Long id) {
        mRemoteId = id;
    }

    private void setQuestionIdentifier(String id) {
        mQuestionIdentifier = id;
    }

    private void setOptionIdentifier(String id) {
        mOptionIdentifier = id;
    }

    private void setNextQuestionIdentifier(String id) {
        mNextQuestionIdentifier = id;
    }

    private void setRemoteQuestionId(Long id) {
        mRemoteQuestionId = id;
    }

    private void setRemoteInstrumentId(Long id) {
        mRemoteInstrumentId = id;
    }

}
