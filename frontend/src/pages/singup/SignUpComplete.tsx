import React from "react";
import StyledButton from "../../styles/StyledButton";
import { useNavigate } from "react-router-dom";

const containerStyle = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  height: "100vh",
};

export const SignUpComplete = () => {
  const navigate = useNavigate();

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
      }}
    >
      <img
        src="/images/foodreco.png"
        alt="dsf"
        style={{ width: "18.8125rem", height: "9.9375rem" }}
      />

      <div>
        <br />
        <br />
        <p>회원가입 완료</p>
        <br />
        <p>푸드레코에 오신것을 환영합니다.</p>
        <p>자신에게 맞는 메뉴를 추천받아보세요</p>
      </div>
      <br />
      <StyledButton
        width="18.8125rem"
        height="2.8125rem"
        onClick={() => navigate("/login")}
      >
        로그인
      </StyledButton>
    </div>
  );
};
