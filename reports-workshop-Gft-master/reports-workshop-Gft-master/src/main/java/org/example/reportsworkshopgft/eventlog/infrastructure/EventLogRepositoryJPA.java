package org.example.reportsworkshopgft.eventlog.infrastructure;

import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogIdJPA;
import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepositoryJPA extends JpaRepository<EventLogJPA, EventLogIdJPA> {
    Page<EventLogJPA> findAll(Pageable pageable);
}
