package com.ssafy.special.repository.etc;

import com.ssafy.special.domain.etc.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

}
