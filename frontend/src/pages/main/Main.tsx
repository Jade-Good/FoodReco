import React from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { MemberFooterHome } from '../../components/MemberFooter/MemberFooterHome';
import { SignUp } from '../singup/SignUp';

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
      <SignUp />
      <MemberFooterHome />
    </>
  );
};
