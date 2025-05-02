package com.interviewme.service;

import com.interviewme.domain.InterviewSession;

public interface InterviewService {
    InterviewSession createSession(String participantName, String participantEmail);
    void startSession(String sessionId);
    void endSession(String sessionId);
    void processAudioInput(String sessionId, byte[] audioData);
    void processVideoFrame(String sessionId, byte[] frameData);
}