package com.rustdesk.api.repository;

import com.rustdesk.api.entity.ServerCmd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ServerCmd repository interface
 */
@Repository
public interface ServerCmdRepository extends JpaRepository<ServerCmd, Long> {

    /**
     * Find server commands by target
     *
     * @param target target identifier
     * @return List of server commands
     */
    List<ServerCmd> findByTarget(Integer target);
}
