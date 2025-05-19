package com.interviewme.service;

import com.interviewme.domain.InterviewSession;
import com.interviewme.domain.InterviewStatus;
import com.interviewme.domain.Participant;
import com.interviewme.repository.InterviewSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewServiceImplTest {

    private InterviewSessionRepository sessionRepository;
    private AvatarService avatarService;
    private InterviewServiceImpl interviewService;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(InterviewSessionRepository.class);
        avatarService = mock(AvatarService.class);
        interviewService = new InterviewServiceImpl(sessionRepository, avatarService);
    }

    @Test
    void testCreateSession() {
        String name = "John Doe";
        String email = "john.doe@example.com";

        ArgumentCaptor<InterviewSession> captor = ArgumentCaptor.forClass(InterviewSession.class);
        when(sessionRepository.save(any(InterviewSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InterviewSession session = interviewService.createSession(name, email);

        verify(sessionRepository).save(captor.capture());
        InterviewSession savedSession = captor.getValue();

        assertNotNull(savedSession.getId());
        assertEquals(name, savedSession.getParticipant().getName());
        assertEquals(email, savedSession.getParticipant().getEmail());
        assertEquals(InterviewStatus.CREATED, savedSession.getStatus());
        assertNotNull(savedSession.getStartTime());
    }

    @Test
    void testStartSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.CREATED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        interviewService.startSession(sessionId.toString());

        assertEquals(InterviewStatus.STARTED, session.getStatus());
        verify(avatarService).initializeAvatar(sessionId.toString());
        verify(sessionRepository).save(session);
    }

    @Test
    void testEndSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.STARTED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        interviewService.endSession(sessionId.toString());

        assertEquals(InterviewStatus.COMPLETED, session.getStatus());
        assertNotNull(session.getEndTime());
        verify(avatarService).closeAvatar(sessionId.toString());
        verify(sessionRepository).save(session);
    }

    @Test
    void testProcessAudioInput_ActiveSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.STARTED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Create test data using platform's default charset
        String testAudio = "test audio";
        byte[] audioData = testAudio.getBytes();

        interviewService.processAudioInput(sessionId.toString(), audioData);

        verify(avatarService).generateResponse(eq(sessionId.toString()), eq(testAudio));
    }

    @Test
    void testProcessAudioInput_InactiveSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.CREATED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        byte[] audioData = "test audio".getBytes();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                interviewService.processAudioInput(sessionId.toString(), audioData));

        assertEquals("Interview session is not active", exception.getMessage());
        verify(avatarService, never()).generateResponse(anyString(), anyString());
    }

    @Test
    void testProcessVideoFrame_ActiveSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.STARTED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        byte[] frameData = new byte[]{1, 2, 3};
        interviewService.processVideoFrame(sessionId.toString(), frameData);

        verify(avatarService).updateAvatarExpression(sessionId.toString(), "neutral");
    }

    @Test
    void testProcessVideoFrame_InactiveSession() {
        UUID sessionId = UUID.randomUUID();
        InterviewSession session = new InterviewSession();
        session.setId(sessionId);
        session.setStatus(InterviewStatus.CREATED);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        byte[] frameData = new byte[]{1, 2, 3};

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                interviewService.processVideoFrame(sessionId.toString(), frameData));

        assertEquals("Interview session is not active", exception.getMessage());
        verify(avatarService, never()).updateAvatarExpression(anyString(), anyString());
    }
}
