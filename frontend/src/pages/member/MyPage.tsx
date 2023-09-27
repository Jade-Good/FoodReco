import React from "react";
import { MemberFooterMypage } from "../../components/MemberFooter/MemberFooterMypage";
import { MemberInfo } from "../../components/membercomponents/MemberInfo";
import HeaderLogo from "../../components/header/HeaderLogo";
import classes from "./MyPage.module.css";
import StyledButton from "../../styles/StyledButton";
import { useNavigate } from "react-router-dom";

export const MyPage = () => {
  const navigate = useNavigate();
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
            src="/images/이민정.jpg"
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
          <MemberInfo leftValue="키" rightValue="190" unit="CM" />
          <br />
          <MemberInfo leftValue="키" rightValue="190" unit="CM" />
          <br />
          <MemberInfo leftValue="키" rightValue="190" unit="CM" />
          <br />
          <MemberInfo leftValue="키" rightValue="190" unit="CM" />
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
          구글인증
        </StyledButton>
        <MemberFooterMypage />
      </div>
    </>
  );
};
