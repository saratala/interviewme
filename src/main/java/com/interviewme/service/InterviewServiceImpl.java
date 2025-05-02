package com.interviewme.service;

import com.interviewme.domain.InterviewSession;
import com.interviewme.domain.InterviewStatus;
import com.interviewme.domain.Participant;
import com.interviewme.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    private final InterviewSessionRepository sessionRepository;
    private final AvatarService avatarService;

    public InterviewServiceImpl(InterviewSessionRepository sessionRepository, AvatarService avatarService) {
        this.sessionRepository = sessionRepository;
        this.avatarService = avatarService;
    }

    @Override
    public InterviewSession createSession(String participantName, String participantEmail) {
        Participant participant = new Participant(participantName, participantEmail);
        InterviewSession session = new InterviewSession();
        session.setParticipant(participant);
        // If there's a bidirectional relationship
        return sessionRepository.save(session);
    }

    @Override
    public void startSession(String sessionId) {
        InterviewSession session = findSession(sessionId);
        session.setStatus(InterviewStatus.STARTED);
        avatarService.initializeAvatar(sessionId);
        sessionRepository.save(session);
    }

    @Override
    public void endSession(String sessionId) {
        InterviewSession session = findSession(sessionId);
        session.setStatus(InterviewStatus.COMPLETED);
        session.setEndTime(Instant.now());
        avatarService.closeAvatar(sessionId);
        sessionRepository.save(session);
    }

    @Override
    public void processAudioInput(String sessionId, byte[] audioData) {
        InterviewSession session = findSession(sessionId);
        if (session.getStatus() != InterviewStatus.STARTED) {
            throw new IllegalStateException("Interview session is not active");
        }
        // Process audio data and generate avatar response
        avatarService.generateResponse(sessionId, new String(audioData));
    }

    @Override
    public void processVideoFrame(String sessionId, byte[] frameData) {
        InterviewSession session = findSession(sessionId);
        if (session.getStatus() != InterviewStatus.STARTED) {
            throw new IllegalStateException("Interview session is not active");
        }
        // Process video frame for emotion detection
        // Update avatar expression based on detected emotion
        avatarService.updateAvatarExpression(sessionId, "neutral");
    }

    private InterviewSession findSession(String sessionId) {
        return sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }
}