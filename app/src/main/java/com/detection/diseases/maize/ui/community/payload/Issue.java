package com.detection.diseases.maize.ui.community.payload;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Issue implements Parcelable {
    String question,
     uuid,
     createdBy,
     questionDescription,
     crop,
     issueStatus,
     imageAvatarUrl,
     createdAt,
     issueLikes,
     issueDislikes,
     issueAnswers;

    protected Issue(Parcel in) {
        question = in.readString();
        uuid = in.readString();
        createdBy = in.readString();
        questionDescription = in.readString();
        crop = in.readString();
        issueStatus = in.readString();
        imageAvatarUrl = in.readString();
        createdAt = in.readString();
        issueLikes = in.readString();
        issueDislikes = in.readString();
        issueAnswers = in.readString();
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(uuid);
        parcel.writeString(createdBy);
        parcel.writeString(questionDescription);
        parcel.writeString(crop);
        parcel.writeString(issueStatus);
        parcel.writeString(imageAvatarUrl);
        parcel.writeString(createdAt);
        parcel.writeString(issueLikes);
        parcel.writeString(issueDislikes);
        parcel.writeString(issueAnswers);
    }
}
