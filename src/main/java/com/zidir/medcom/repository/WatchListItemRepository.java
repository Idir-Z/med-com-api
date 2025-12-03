package com.zidir.medcom.repository;

import com.zidir.medcom.domain.WatchListItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WatchListItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatchListItemRepository extends JpaRepository<WatchListItem, Long> {}
