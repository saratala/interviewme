package com.interviewme.service;

import org.springframework.stereotype.Service;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Override
    public void initializeAvatar(String sessionId) {
        // Implementation for initializing avatar
    }

    @Override
    public void closeAvatar(String sessionId) {
        // Implementation for closing avatar
    }

    @Override
    public void generateResponse(String sessionId, String audioData) {
        // Implementation for generating avatar response
    }

    @Override
    public void updateAvatarExpression(String sessionId, String expression) {
        // Implementation for updating avatar expression
    }
}