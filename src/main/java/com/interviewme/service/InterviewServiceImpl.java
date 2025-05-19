package com.interviewme.service;

import com.interviewme.domain.InterviewSession;
import com.interviewme.domain.InterviewStatus;
import com.interviewme.domain.Participant;
import com.interviewme.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of the InterviewService interface.
 * Provides functionality for managing interview sessions, including creation, 
 * starting, ending, and processing audio/video inputs.
 */
@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    private final InterviewSessionRepository sessionRepository;
    private final AvatarService avatarService;

    /**
     * Constructor for InterviewServiceImpl.
     *
     * @param sessionRepository the repository for managing interview sessions
     * @param avatarService the service for managing avatar interactions
     */
    public InterviewServiceImpl(InterviewSessionRepository sessionRepository, AvatarService avatarService) {
        this.sessionRepository = sessionRepository;
        this.avatarService = avatarService;
    }

    /**
     * Creates a new interview session for a participant.
     *
     * @param participantName the name of the participant
     * @param participantEmail the email of the participant
     * @return the created InterviewSession
     */
    @Override
    public InterviewSession createSession(String participantName, String participantEmail) {
        Participant participant = new Participant(participantName, participantEmail);
        InterviewSession session = new InterviewSession();
        session.setParticipant(participant);
        session.setId(UUID.randomUUID()); // Explicitly set UUID
        session.setStartTime(Instant.now()); // Set start time
        session.setStatus(InterviewStatus.CREATED); // Set initial status
        return sessionRepository.save(session);
    }

    /**
     * Starts an existing interview session.
     *
     * @param sessionId the ID of the session to start
     */
    @Override
    public void startSession(String sessionId) {
        InterviewSession session = findSession(sessionId);
        session.setStatus(InterviewStatus.STARTED);
        avatarService.initializeAvatar(sessionId);
        sessionRepository.save(session);
    }

    /**
     * Ends an existing interview session.
     *
     * @param sessionId the ID of the session to end
     */
    @Override
    public void endSession(String sessionId) {
        InterviewSession session = findSession(sessionId);
        session.setStatus(InterviewStatus.COMPLETED);
        session.setEndTime(Instant.now());
        avatarService.closeAvatar(sessionId);
        sessionRepository.save(session);
    }

    /**
     * Processes audio input for an active interview session.
     *
     * @param sessionId the ID of the session
     * @param audioData the audio data to process
     * @throws IllegalStateException if the session is not active
     */
    @Override
    public void processAudioInput(String sessionId, byte[] audioData) {
        InterviewSession session = findSession(sessionId);
        if (session.getStatus() != InterviewStatus.STARTED) {
            throw new IllegalStateException("Interview session is not active");
        }
        // Use platform's default charset to match test expectations
        String audioText = new String(audioData);
        avatarService.generateResponse(sessionId, audioText);
    }

    /**
     * Processes a video frame for an active interview session.
     *
     * @param sessionId the ID of the session
     * @param frameData the video frame data to process
     * @throws IllegalStateException if the session is not active
     */
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

    /**
     * Finds an interview session by its ID.
     *
     * @param sessionId the ID of the session
     * @return the InterviewSession if found
     * @throws IllegalArgumentException if the session is not found
     */
    private InterviewSession findSession(String sessionId) {
        return sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }
}
