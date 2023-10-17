import React, { useState, useEffect } from "react";
import { FooterFriend } from "../../components/footer/FooterFriend";
import HeaderFriendAdd from "../../components/header/HeaderFriendAdd";
import { Friend } from "../../components/friend/Friend";
import { FriendInviteModal } from "../../components/friend/FriendInviteModal";
import "../../components/friend/Friend.module.css";
import axios from "axios";
import api from "../../utils/axios";

export const FriendList = () => {
  const [friendList, setFriendList] = useState<
    { memberNickname: string; memberImg: string }[]
  >([]);
  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/friend/list`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        console.log(res);
        setFriendList(res.data.friendList);
      })
      .catch((err) => console.log(err));
  }, []);

  console.log(friendList);

  let [modal, setModal] = useState(false);

  const modalOn = (newState: boolean) => {
    setModal(newState);
    document.body.style.overflow = "hidden";
  };

  const modalOff = (newState: boolean) => {
    setModal(newState);
    document.body.style.overflow = "scroll";
  };

  return (
    <>
      <HeaderFriendAdd exitModal={modalOn} />
      <div style={{ margin: "5.5rem 0", overflow: "hidden" }}>
        {friendList.map((friend, i) => {
          return (
            <Friend
              name={friend.memberNickname}
              imgUrl={friend.memberImg}
              key={i}
            />
          );
        })}
      </div>
      <FooterFriend />
      {modal === true ? <FriendInviteModal exitModal={modalOff} /> : null}
    </>
  );
};
