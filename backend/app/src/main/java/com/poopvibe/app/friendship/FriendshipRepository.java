package com.poopvibe.app.friendship;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Persists friend requests and accepted relationships.
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /**
     * Finds a relationship regardless of which user initiated it.
     *
     * @param firstUserId first user identifier
     * @param secondUserId second user identifier
     * @return friendship row, or empty when no relationship exists
     */
    @Query("""
            select f from Friendship f
            where (f.requester.id = :firstUserId and f.addressee.id = :secondUserId)
               or (f.requester.id = :secondUserId and f.addressee.id = :firstUserId)
            """)
    Optional<Friendship> findBetween(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);

    /**
     * Lists all relationships touching a user.
     *
     * @param userId user identifier
     * @return friend requests and relationships involving the user
     */
    @Query("""
            select f from Friendship f
            where f.requester.id = :userId or f.addressee.id = :userId
            order by f.updatedAt desc
            """)
    List<Friendship> findForUser(@Param("userId") Long userId);

    /**
     * Lists relationships touching a user for a specific status.
     *
     * @param userId user identifier
     * @param status relationship status filter
     * @return friendships involving the user with the requested status
     */
    @Query("""
            select f from Friendship f
            where (f.requester.id = :userId or f.addressee.id = :userId)
              and f.status = :status
            """)
    List<Friendship> findByUserAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);
}
