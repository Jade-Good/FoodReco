package com.ssafy.special.repository.etc;

import com.ssafy.special.domain.etc.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendListRepository extends JpaRepository<FriendList, FriendList.FriendListId>{

}
