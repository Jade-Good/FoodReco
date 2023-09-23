package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.FriendList;
import com.ssafy.special.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendListRepository extends JpaRepository<FriendList, Long>{
    List<FriendList> findByOneMemberSeqOrOtherMemberSeq(Long oneMemberSeq, Long otherMemberSeq);
}
