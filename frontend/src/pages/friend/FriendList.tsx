import React, { useState } from "react";
import { MemberFooterFriend } from "../../components/MemberFooter/MemberFooterFriend";
import HeaderFriendAdd from "../../components/header/HeaderFriendAdd";
import { Friend } from "../../components/friend/Friend";
import { FriendInviteModal } from "../../components/friend/FriendInviteModal";
import "../../components/friend/Friend.module.css";

export const FriendList = () => {
  let arr = [
    "김인아",
    "고인원",
    "이민정",
    "주시원",
    "남아영",
    "이승준",
    "김인아",
    "고인원",
    "이민정",
    "주시원",
    "남아영",
    "이승준",
    "김인아",
    "고인원",
    "이민정",
    "주시원",
    "남아영",
    "이승준",
    "김인아",
    "고인원",
    "이민정",
    "주시원",
    "남아영",
    "이승준",
  ];

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
        {arr.map((name, i) => {
          return <Friend name={name} key={i} />;
        })}
      </div>
      <MemberFooterFriend />
      {modal === true ? <FriendInviteModal exitModal={modalOff} /> : null}
    </>
  );
};
