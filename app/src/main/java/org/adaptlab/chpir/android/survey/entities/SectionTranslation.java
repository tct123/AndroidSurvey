package org.adaptlab.chpir.android.survey.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.adaptlab.chpir.android.survey.daos.BaseDao;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@android.arch.persistence.room.Entity(tableName = "SectionTranslations",
        foreignKeys = @ForeignKey(entity = Section.class,
                parentColumns = "RemoteId", childColumns = "SectionRemoteId", onDelete = CASCADE))
public class SectionTranslation implements SurveyEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "RemoteId")
    private Long mRemoteId;
    @SerializedName("text")
    @ColumnInfo(name = "Text")
    private String mText;
    @SerializedName("language")
    @ColumnInfo(name = "Language")
    private String mLanguage;
    @SerializedName("section_id")
    @ColumnInfo(name = "SectionRemoteId", index = true)
    private Long mSectionRemoteId;

    @NonNull
    public Long getRemoteId() {
        return mRemoteId;
    }

    public void setRemoteId(@NonNull Long id) {
        this.mRemoteId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String title) {
        this.mText = title;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public Long getSectionRemoteId() {
        return mSectionRemoteId;
    }

    public void setSectionRemoteId(Long mSectionRemoteId) {
        this.mSectionRemoteId = mSectionRemoteId;
    }

    @Override
    public Type getType() {
        return new TypeToken<ArrayList<SectionTranslation>>() {
        }.getType();
    }

    @Override
    public List<? extends SurveyEntity> getTranslations() {
        return null;
    }

    @Override
    public void save(BaseDao dao, List list) {
        dao.updateAll(list);
        dao.insertAll(list);
    }

    @Override
    public void setDeleted(boolean deleted) {

    }
}
