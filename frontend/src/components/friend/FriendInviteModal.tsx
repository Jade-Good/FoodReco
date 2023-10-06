import React, { useState } from "react";
import classes from "./FriendInviteModal.module.css";
import QRCode from "qrcode.react";
import axios from "axios";

interface ModalProps {
  exitModal: (newState: boolean) => void;
}

interface seqProps {
  memberSeq: string;
}

const addFriend = ({ memberSeq }: seqProps) => {
  axios.get(`${process.env.REACT_APP_BASE_URL}/member/friend/${memberSeq}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    },
  });
};

export const FriendInviteModal = ({ exitModal }: ModalProps) => {
  const memberSeq = localStorage.getItem("memberSeq");

  let url = `https://j9b102.p.ssafy.io/friend/add/${memberSeq}`;
  // let url = `https://j9b102.p.ssafy.io/`;

  const copyURL = async () => {
    try {
      await navigator.clipboard.writeText(url);
    } catch (err) {
      console.error("Failed to copy text: ", err);
    }
  };

  return (
    <>
      <div
        className={classes.modalBackground}
        onClick={() => exitModal(false)}
      ></div>
      <div className={classes.modal}>
        <h3>QR 초대장을 만들었습니다.</h3>
        <p>아래 QR을 보여주거나</p>
        <p>초대 URL을 공유해주세요.</p>
        <hr></hr>
        <QRCode value={url} size={140} className={classes.qrCode} />
        <hr></hr>
        {/* <h6>{msg}</h6> */}

        {/* <div className={classes.shareList}>
          <div>
            <img
              src={`/images/카카오톡 로고 엣지.png`}
              alt="kakaotalk"
              className={classes.circleImg}
            />
            <p>카카오톡</p>
          </div>
          <div>
            <div className={classes.circleURL} onClick={copyURL}>
              URL
            </div>
            <p>주소복사</p>
          </div>
        </div> */}
      </div>
    </>
  );
};
