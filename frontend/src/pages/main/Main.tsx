import React, { useEffect } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { FooterHome } from "../../components/footer/FooterHome";
import { SignUp } from "../singup/SignUp";
import { Login } from "../login/Login";
import api from "../../utils/axios";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/atoms/userState";
import { ToastContainer } from "react-toastify";
import HeaderLogo from "../../components/header/HeaderLogo";
import StyledButton from "../../styles/StyledButton";

interface IForm {
  email: string;
  password: string;
}

export const Main: React.FC = () => {
  const [user, setUser] = useRecoilState(userState);

  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/home`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.error("Error occurred:", err);
      });
  }, []);

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, isSubmitted, errors },
  } = useForm<IForm>();

  const onSubmit: SubmitHandler<IForm> = (data) => {
    console.log(data);
  };

  return (
    <>
      <HeaderLogo />
      <div style={{ marginTop: "17vh", display: "flex", flexWrap: "wrap" }}>
        <button
          style={{
            flex: "1 0 50%",
            marginBottom: "1rem",
            background: "linear-gradient(90deg, orange, yellow)",
            color: "white", // 글자 색상을 흰색으로 설정
            border: "none", // 테두리 제거
            padding: "10px 20px", // 내용과 버튼 크기 조절
            width: "5vh",
            height: "30vh",
            fontSize: "20px",
          }}
        >
          서비스 소개
        </button>
        <button
          style={{
            flex: "1 0 50%",
            marginBottom: "1rem",
            background: "linear-gradient(90deg, green, black)",
            color: "white", // 글자 색상을 흰색으로 설정
            border: "none", // 테두리 제거
            padding: "10px 20px", // 내용과 버튼 크기 조절
            opacity: 0.9, // 투명도 조절
            fontSize: "20px",

            fontWeight: "bold",
          }}
        >
          알고리즘 소개
        </button>
        <button
          style={{
            flex: "1 0 50%",
            marginBottom: "1rem",
            background: "linear-gradient(90deg, red, yellow)",
            color: "white", // 글자 색상을 흰색으로 설정
            border: "none", // 테두리 제거
            padding: "10px 20px", // 내용과 버튼 크기 조절
            opacity: 0.9, // 투명도 조절
            width: "5vh",
            height: "30vh",
            fontSize: "20px",
            fontWeight: "bold",
          }}
        >
          버튼 텍스트
        </button>

        <button
          style={{
            flex: "1 0 50%",
            marginBottom: "1rem",
            background: "linear-gradient(90deg, blue, skyblue)",
            color: "white", // 글자 색상을 흰색으로 설정
            border: "none", // 테두리 제거
            padding: "10px 20px", // 내용과 버튼 크기 조절
            opacity: 0.9, // 투명도 조절
            fontSize: "20px",

            fontWeight: "bold",
          }}
        >
          버튼 텍스트
        </button>
      </div>
      <FooterHome />

      <ToastContainer
        position="top-center"
        autoClose={1000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </>
  );
};
