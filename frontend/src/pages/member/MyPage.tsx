import React, { useEffect, useState } from "react";
import { FooterMypage } from "../../components/footer/FooterMypage";
import { MemberInfo } from "../../components/membercomponents/MemberInfo";
import HeaderLogo from "../../components/header/HeaderLogo";
import classes from "./MyPage.module.css";
import StyledButton from "../../styles/StyledButton";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export const MyPage = () => {
  const navigate = useNavigate();
  const [height, setHeight] = useState("");
  const [weight, setWeight] = useState("");
  const [nickname, setNickname] = useState("");
  const [activity, setActivicty] = useState(0);
  const [profileURL, setProfileURL] = useState("");

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_BASE_URL}/mypage/info`)
      .then((res) => {
        console.log(res);
        setHeight(res.data.height);
        setWeight(res.data.weight);
        setNickname(res.data.nickname);
        setActivicty(res.data.activity);
        setProfileURL(res.data.profileUrl);
      })
      .catch((err) => {
        console.log(err);
      });
  });
  return (
    <>
      <HeaderLogo />
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          flexDirection: "column",
          marginTop: "10rem",
        }}
      >
        <div
          style={{
            borderRadius: "50%",
            overflow: "hidden",
            width: "15rem",
            height: "15rem",
            marginBottom: "2rem",
          }}
        >
          <img
            src={profileURL}
            alt="sds"
            style={{ width: "15rem", height: "15rem" }}
          />
        </div>
        <StyledButton
          background="#FFF6EC"
          color="#FE9D3A"
          border="1px solid #FE9D3A"
          onClick={() => navigate("/mypage/edit/:memberId")}
        >
          프로필수정
        </StyledButton>
        <br />
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <MemberInfo leftValue="닉네임" rightValue={nickname} />
          <br />
          <MemberInfo leftValue="키" rightValue={height} unit="CM" />
          <br />
          <MemberInfo leftValue="몸무게" rightValue={weight} unit="kg" />
          <br />
          <MemberInfo leftValue="활동량" rightValue={activity} unit="걸음" />
        </div>
        <br />
        <StyledButton
          width="20.5vw"
          background="#FFF6EC"
          color="#FE9D3A"
          border="1px solid #FE9D3A"
          onClick={() =>
            (window.location.href =
              "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://j9b102.p.ssafy.io/test&prompt=consent&response_type=code&client_id=195561660115-6gse0lsa1ggdm3t9jplps3sodm7e735n.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/fitness.activity.read https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/fitness.body.read https://www.googleapis.com/auth/fitness.nutrition.read https://www.googleapis.com/auth/fitness.sleep.read")
          }
        >
          구글 연동하기 : Fitness
        </StyledButton>
        <FooterMypage />
      </div>
    </>
  );
};
