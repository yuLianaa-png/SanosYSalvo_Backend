package com.babygoat.match_service.repository;

import com.babygoat.match_service.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface matchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUserId(Long userId);
}
