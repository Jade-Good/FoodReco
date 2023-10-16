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
import { useNavigate } from "react-router-dom";
import Chart from "react-apexcharts";
import Donut from "./Donut";
import WordCloudCustom from "./WordCloudCustom";

interface IForm {
  email: string;
  password: string;
}

export const Main: React.FC = () => {
  const [user, setUser] = useRecoilState(userState);
  const navigate = useNavigate();

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
      <div
        style={{
          marginTop: "13vh",
          display: "flex",
          flexWrap: "wrap",
          overflow: "scroll",
          marginBottom: "10vh",
          height: "100vh",
          justifyContent: "center",
        }}
      >
        <div>
          <p style={{ fontSize: "20px" }}>추천 받은 음식</p>
          <WordCloudCustom />
          <p style={{ fontSize: "20px" }}>음식 비율</p>
          <Donut />
        </div>
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
