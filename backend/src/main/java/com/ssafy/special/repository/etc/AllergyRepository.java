package com.ssafy.special.repository.etc;

import com.ssafy.special.domain.etc.Alarm;
import com.ssafy.special.domain.etc.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Integer> {

}
