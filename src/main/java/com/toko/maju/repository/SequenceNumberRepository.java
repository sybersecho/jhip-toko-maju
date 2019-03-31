package com.toko.maju.repository;

import com.toko.maju.domain.SequenceNumber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the SequenceNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SequenceNumberRepository extends JpaRepository<SequenceNumber, Long> {
	Integer findNextValueByType(String type);

	SequenceNumber findByType(String type);
}
