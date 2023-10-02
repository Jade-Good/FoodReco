import React from "react";
import classes from "./foodButton.module.css";
import { ImBlocked } from "react-icons/im";
import { IoArrowRedoSharp } from "react-icons/io5";
import { MdFavorite } from "react-icons/md";
import { RiTimerFill } from "react-icons/ri";
import axios from "axios";

export const FoodButton = () => {
  const feedBack = (num: number) => {
    console.log(num);
    // axios
    //     .post(
    //       `${process.env.REACT_APP_BASE_URL}/recommend/feedback/{nextFoodSeq}`,

    //     )
    // .then((res) => {
    //   console.log(data);
    //   console.log("로그인중", res);
    //   const nickcname = res.data.nickname;
    //   const accessToken = res.headers.authorization;
    //   const email = res.data.email;
    //   const refreshToken = res.headers.authorizationRefresh;

    //   axios.defaults.headers.common[
    //     "Authorization"
    //   ] = `Bearer ${accessToken}`;
    //   // localStorage.setItem('accesstoken', accessToken);

    //   setUser((prevUser) => ({
    //     ...prevUser,
    //     refreshToken: refreshToken,
    //     accessToken: accessToken,
    //     nickname: nickcname,
    //     email: email,
    //   }));

    //   // accessToken 만료하기 1분 전에 로그인 연장
    //   setTimeout(handleSilentRefresh, JWT_EXPIRY_TIME - 60000);

    //   navigate("/");
    // })
    // .catch((err) => {
    //   handleSilentRefresh(data);
    //   console.log("이메일 전송 오류:", err);
    // });
  };

  return (
    <div className={classes.buttons}>
      <div
        className={classes.circleTall}
        onClick={() => {
          feedBack(1);
        }}
      >
        <ImBlocked style={{ color: "#7D7B7B", fontSize: "4vh" }} />
      </div>
      <div
        className={classes.circleLarge}
        onClick={() => {
          feedBack(2);
        }}
      >
        <IoArrowRedoSharp style={{ color: "#4D7EFF", fontSize: "7vh" }} />
      </div>
      <div
        className={classes.circleLarge}
        onClick={() => {
          feedBack(3);
        }}
      >
        <MdFavorite style={{ color: "#FF4747", fontSize: "7vh" }} />
      </div>
      <div
        className={classes.circleTall}
        onClick={() => {
          feedBack(4);
        }}
      >
        <RiTimerFill style={{ color: "#C6C5C5", fontSize: "4vh" }} />
      </div>
    </div>
  );
};
