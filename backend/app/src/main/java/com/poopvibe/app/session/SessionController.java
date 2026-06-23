package com.poopvibe.app.session;

import com.poopvibe.app.session.SessionDtos.AddCommentRequest;
import com.poopvibe.app.session.SessionDtos.AddMediaRequest;
import com.poopvibe.app.session.SessionDtos.AddReactionRequest;
import com.poopvibe.app.session.SessionDtos.CommentResponse;
import com.poopvibe.app.session.SessionDtos.MediaResponse;
import com.poopvibe.app.session.SessionDtos.ReactionResponse;
import com.poopvibe.app.session.SessionDtos.SessionResponse;
import com.poopvibe.app.session.SessionDtos.UpsertSessionRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for sessions and their social child resources.
 */
@RestController
@RequestMapping("/api/v1/poop-sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService service;

    /**
     * Creates or syncs a session.
     *
     * @param request session payload
     * @return created session
     */
    @PostMapping
    public ResponseEntity<SessionResponse> create(@Valid @RequestBody UpsertSessionRequest request) {
        SessionResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/poop-sessions/" + response.id())).body(response);
    }

    /**
     * Lists sessions owned by a user.
     *
     * @param userId owner user identifier
     * @return user's sessions
     */
    @GetMapping
    public List<SessionResponse> listForUser(@RequestParam Long userId) {
        return service.listForUser(userId);
    }

    /**
     * Returns a session.
     *
     * @param sessionId session identifier
     * @return matching session
     */
    @GetMapping("/{sessionId}")
    public SessionResponse get(@PathVariable Long sessionId) {
        return service.get(sessionId);
    }

    /**
     * Updates a session.
     *
     * @param sessionId session identifier
     * @param request session payload
     * @return updated session
     */
    @PatchMapping("/{sessionId}")
    public SessionResponse update(@PathVariable Long sessionId, @Valid @RequestBody UpsertSessionRequest request) {
        return service.update(sessionId, request);
    }

    /**
     * Deletes a session.
     *
     * @param sessionId session identifier
     * @return empty response
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> delete(@PathVariable Long sessionId) {
        service.delete(sessionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds or replaces a reaction on a session.
     *
     * @param sessionId session identifier
     * @param request reaction payload
     * @return saved reaction
     */
    @PostMapping("/{sessionId}/reactions")
    public ReactionResponse addReaction(
            @PathVariable Long sessionId,
            @Valid @RequestBody AddReactionRequest request
    ) {
        return service.addReaction(sessionId, request);
    }

    /**
     * Adds a comment to a session.
     *
     * @param sessionId session identifier
     * @param request comment payload
     * @return saved comment
     */
    @PostMapping("/{sessionId}/comments")
    public CommentResponse addComment(
            @PathVariable Long sessionId,
            @Valid @RequestBody AddCommentRequest request
    ) {
        return service.addComment(sessionId, request);
    }

    /**
     * Adds media metadata to a session.
     *
     * @param sessionId session identifier
     * @param request media payload
     * @return saved media metadata
     */
    @PostMapping("/{sessionId}/media")
    public MediaResponse addMedia(@PathVariable Long sessionId, @Valid @RequestBody AddMediaRequest request) {
        return service.addMedia(sessionId, request);
    }
}
