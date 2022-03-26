package com.detection.diseases.maize.ui.community.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueAnswerModel {
    String answerContent,answerBy, imageAvatarLink;
    long answerLikes;
    long answerDislikes;
    String uuid;
    String createdAt;
}
