package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
