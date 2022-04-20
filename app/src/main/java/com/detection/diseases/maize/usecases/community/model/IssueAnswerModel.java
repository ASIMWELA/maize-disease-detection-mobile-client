package com.detection.diseases.maize.usecases.community.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author Augustine
 *
 * A model that defines an answer that comes from a remote API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueAnswerModel {
    /**
     * answercontent: The actual answer of the issue
     *
     * answerby : The user who created the answer
     *
     * imageAvatarLink: The url where the answer image is hosted
     */
    String answerContent,answerBy, imageAvatarLink;

    /**
     * Total number of likes of this answer
     */
    long answerLikes;
    /**
     * Total number of dislikes of this answer
     */
    long answerDislikes;

    /**
     * A unique identifier of this answer
     */
    String uuid;

    /**
     *A stringified date when the answer was created
     *
     */
    String createdAt;
}
