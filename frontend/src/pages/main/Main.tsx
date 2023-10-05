import React, { useEffect } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { FooterHome } from "../../components/footer/FooterHome";
import { SignUp } from "../singup/SignUp";
import { Login } from "../login/Login";
import api from "../../utils/axios";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/atoms/userState";
import { ToastContainer } from "react-toastify";

interface IForm {
  email: string;
  password: string;
}

export const Main: React.FC = () => {
  const [user, setUser] = useRecoilState(userState);

  // useEffect(() => {
  //   api
  //     .get(`${process.env.REACT_APP_BASE_URL}/mypage/info`, {
  //       headers: {
  //         Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
  //       },
  //     })
  //     .then((res) => {
  //       console.log(res);
  //     })
  //     .catch((err) => {
  //       console.error("Error occurred:", err);
  //     });
  // }, []);
  console.log(user);
  console.log(user.memberSeq);
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
