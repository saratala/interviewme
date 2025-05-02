package com.interviewme.service;

public interface AvatarService {
    void initializeAvatar(String sessionId);
    void generateResponse(String sessionId, String userInput);
    void updateAvatarExpression(String sessionId, String emotion);
    void closeAvatar(String sessionId);
}