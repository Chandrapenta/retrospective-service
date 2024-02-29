package com.assignment.retrospectiveservice.repository;

import com.assignment.retrospectiveservice.model.Retrospective;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Retrospective entities.
 */
@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {

    /**
     * Finds a retrospective by its name.
     * 
     * @param name the name of the retrospective
     * @return an Optional containing the retrospective, or empty if not found
     */
    Optional<Retrospective> findByName(String name);

    /**
     * Finds retrospectives by their date.
     * 
     * @param date     the date of the retrospectives
     * @param pageable pagination information
     * @return a page of retrospectives
     */
    Page<Retrospective> findByDate(LocalDate date, Pageable pageable);
}
