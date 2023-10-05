import React, { useEffect } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { FooterHome } from "../../components/footer/FooterHome";
import { SignUp } from "../singup/SignUp";
import { Login } from "../login/Login";
import api from "../../utils/axios";

interface IForm {
  email: string;
  password: string;
}

export const Main: React.FC = () => {
  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/mypage/info`, {
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
      <FooterHome />
    </>
  );
};
