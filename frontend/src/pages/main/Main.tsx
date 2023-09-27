import React from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { FooterHome } from "../../components/footer/FooterHome";
import { SignUp } from "../singup/SignUp";
import { Login } from "../login/Login";

interface IForm {
  email: string;
  password: string;
}

export const Main: React.FC = () => {
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
      <Login />
      <FooterHome />
    </>
  );
};
