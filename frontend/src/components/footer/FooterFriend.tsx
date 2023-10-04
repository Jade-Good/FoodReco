import React from "react";
import { AiOutlineHome } from "react-icons/ai";
import { BiGroup } from "react-icons/bi";
import { BsPersonCircle } from "react-icons/bs";
import { AiOutlineStar } from "react-icons/ai";
import { BiWindows } from "react-icons/bi";
import { Navigate, useNavigate } from "react-router-dom";
import classes from "./Footer.module.css";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/atoms/userState";

export const FooterFriend = () => {
  const [user, setUser] = useRecoilState(userState);

  const navigate = useNavigate();
  let memberEmail = user.email;

  return (
    <div className={classes.footer}>
      <div className={classes.iconBtn} onClick={() => navigate("/")}>
        <AiOutlineHome className={classes.iconStyle} />
        <p className={classes.iconText}>홈</p>
      </div>
      <div
        className={`${classes.iconBtn} ${classes.iconBtnAtive}`}
        onClick={() => navigate(`/friend`)}
      >
        <BiGroup className={classes.iconStyle} />
        <p className={classes.iconText}>친구</p>
      </div>
      <div
        className={classes.iconBtn}
        onClick={() => navigate(`/recommendation`)}
      >
        <div className={classes.circleStyle}>
          <AiOutlineStar className={classes.starIconStyle} />
        </div>
        <p className={classes.iconText} style={{ marginTop: "10vmin" }}>
          추천
        </p>
      </div>
      <div className={classes.iconBtn} onClick={() => navigate(`/crew`)}>
        <BiWindows className={classes.iconStyle} />
        <p className={classes.iconText}>그룹</p>
      </div>
      <div className={classes.iconBtn} onClick={() => navigate(`/mypage`)}>
        <BsPersonCircle className={classes.iconStyle} />
        <p className={classes.iconText}>마이페이지</p>
      </div>
    </div>
  );
};

const iconStyle = {
  cursor: "pointer", // 아이콘에 커서 스타일 지정
  fontSize: "2em", // 아이콘 크기 조정
  color: "#C6C5C5",
};
// 원 모양의 아이콘을 감싸는 div 스타일
const circleStyle = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  width: "3em",
  height: "3em",
  borderRadius: "50%", // 원 모양을 만듦
  backgroundColor: "#C6C5C5", // 배경색 지정
  boxShadow: "0px -2px 4px rgba(0, 0, 0, 0.2)", // 위쪽에 그림자 효과를 줌
};

const starIconStyle = {
  fontSize: "2.5em", // 아이콘 크기 조정
  color: "white",
  cursor: "pointer",
};
