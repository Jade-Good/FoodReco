import React, { useState } from "react";
import { MemberFooterFriend } from "../../components/MemberFooter/MemberFooterFriend";
import HeaderFriendAdd from "../../components/header/HeaderFriendAdd";
import { Friend } from "../../components/friend/Friend";
import { FriendInviteModal } from "../../components/friend/FriendInviteModal";

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

  const updateParentState = (newState: boolean) => {
    setModal(newState);
  };

  return (
    <>
      <HeaderFriendAdd exitModal={setModal} />
      <div style={{ marginTop: "5.5rem", overflow: "hidden" }}>
        {arr.map((name, i) => {
          return <Friend name={name} key={i} />;
        })}
      </div>
      <MemberFooterFriend />
      {modal === true ? (
        <FriendInviteModal exitModal={updateParentState} />
      ) : null}
    </>
  );
};
