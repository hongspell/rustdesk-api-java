package com.rustdesk.api.repository;

import com.rustdesk.api.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Oauth repository interface
 */
@Repository
public interface OauthRepository extends JpaRepository<Oauth, Long> {

    /**
     * Find oauth by operation name
     *
     * @param op operation name
     * @return Optional Oauth
     */
    Optional<Oauth> findByOp(String op);

    /**
     * Find oauth configurations by type
     *
     * @param oauthType oauth type
     * @return List of oauth configurations
     */
    List<Oauth> findByOauthType(String oauthType);
}
