import React from "react";
import { AiOutlineHome } from "react-icons/ai";
import { BiGroup } from "react-icons/bi";
import { BsPersonCircle } from "react-icons/bs";
import { AiOutlineStar } from "react-icons/ai";
import { BiWindows } from "react-icons/bi";
import { Navigate, useNavigate } from "react-router-dom";
import classes from "./Footer.module.css";

export const FooterMypage = () => {
  const navigate = useNavigate();
  let memberId = 1;

  return (
    <div className={classes.footer}>
      <div className={classes.iconBtn} onClick={() => navigate("/")}>
        <AiOutlineHome className={classes.iconStyle} />
        <p className={classes.iconText}>홈</p>
      </div>
      <div
        className={classes.iconBtn}
        onClick={() => navigate(`/friend/${memberId}`)}
      >
        <BiGroup className={classes.iconStyle} />
        <p className={classes.iconText}>친구</p>
      </div>
      <div
        className={classes.iconBtn}
        onClick={() => navigate(`/recommendation/${memberId}`)}
      >
        <div className={classes.circleStyle}>
          <AiOutlineStar className={classes.starIconStyle} />
        </div>
        <p className={classes.iconText} style={{ marginTop: "10vmin" }}>
          추천
        </p>
      </div>
      <div
        className={classes.iconBtn}
        onClick={() => navigate(`/crew/${memberId}`)}
      >
        <BiWindows className={classes.iconStyle} />
        <p className={classes.iconText}>그룹</p>
      </div>
      <div
        className={`${classes.iconBtn} ${classes.iconBtnAtive}`}
        onClick={() => navigate(`/mypage/${memberId}`)}
      >
        <BsPersonCircle className={classes.iconStyle} />
        <p className={classes.iconText}>마이페이지</p>
      </div>
    </div>
  );
};
