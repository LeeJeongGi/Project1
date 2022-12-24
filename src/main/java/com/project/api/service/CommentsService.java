package com.project.api.service;

import com.project.api.dto.request.CommentsForm;

import java.util.List;

public interface CommentsService {

    List<Long> saveComments(CommentsForm commentsForm, String userId);

    Long deleteComments(Long commentId, String userId);
}
