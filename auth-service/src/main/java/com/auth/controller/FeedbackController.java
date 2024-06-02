package com.auth.controller;

import com.auth.model.entity.FeedbackEntity;
import com.auth.model.request.FeedbackRequest;
import com.auth.security.services.UserDetailsImpl;
import com.auth.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage feedback", name = "Feedback Resource")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Operation(summary = "Submit feedback", description = "Submit feedback")
    @PostMapping(value = "/feedback")
    @ResponseBody
    public FeedbackEntity submitFeedback(@RequestBody FeedbackRequest feedbackRequest, @AuthenticationPrincipal UserDetailsImpl user) {
        return feedbackService.saveFeedback(user.getId(), feedbackRequest.getContent());
    }

    @Operation(summary = "Get all feedback", description = "Get all feedback (admin only)")
    @GetMapping(value = "/feedback")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<FeedbackEntity> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
}

