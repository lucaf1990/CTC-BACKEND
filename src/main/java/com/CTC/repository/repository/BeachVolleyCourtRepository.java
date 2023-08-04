package com.CTC.repository.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("beachVolleyCourtRepository")
public interface BeachVolleyCourtRepository extends CourtRepository {

}
