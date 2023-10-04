import React from "react";
import { Navigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms/userState";

interface PrivateRouteProps {
  authenticated: number;
  component: JSX.Element;
}

// 로그인 햇는지 안했는지 체크
// authenticated false이면 즉 0
export const CheckLogin = ({
  authenticated,
  component: Component,
}: PrivateRouteProps) => {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken === null) {
    // window.alert("로그인이 필요한 서비스입니다.");
    return <Navigate to="/login" />;
  }

  return Component;
};
