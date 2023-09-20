import React from 'react';
import StyledIdInputIcon from '../../components/inputs/StyledIdInputIcon';
import StyledPwInputIcon from '../../components/inputs/StyledPwInputIcon';
import StyledButton from '../../styles/StyledButton';
import classes from './Login.module.css';
import { SubmitHandler, useForm } from 'react-hook-form';

interface IForm {
  email: string;
  password: string;
}

export const Login = () => {
  const {
    register,
    formState: { errors, isSubmitting, isSubmitted },
    handleSubmit,
    control,
  } = useForm<IForm>({
    mode: 'onSubmit',
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const handleLogin: SubmitHandler<IForm> = (data) => {
    console.log(data);
    console.log(errors);
  };

  return (
    <div className={classes.container}>
      <div>
        <img
          src="images/foodreco.png"
          alt="sdsd"
          width={'200rem'}
          height={'150rem'}
        />
      </div>

      <form onSubmit={handleSubmit(handleLogin)}>
        <div className={classes.inputContainer}>
          {errors.email && (
            <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
              {errors.email.message}
            </small>
          )}

          <StyledIdInputIcon
            name="email" // 필드의 이름
            placeholder="이메일"
            control={control} // useForm에서 가져온 control
            rules={{
              pattern: {
                value:
                  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i,
                message: '이메일 형식에 맞지 않습니다.',
              },
            }}
          />

          <br />
          {errors.password && (
            <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
              {errors.password.message}
            </small>
          )}
          <StyledPwInputIcon
            name="password"
            placeholder="비밀번호"
            control={control}
            rules={{
              minLength: {
                value: 8,
                message: '8자리 이상 비밀번호를 사용하세요.',
              },
            }}
            aria-invalid={
              isSubmitted ? (errors.password ? 'true' : 'false') : undefined
            }
          />

          <div>
            <p style={{ color: '#918C8C' }}>회원가입</p>
          </div>
        </div>
        <br />
        <br />
        <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="18.8125rem"
          height="height: 5rem"
        >
          로그인
        </StyledButton>
      </form>
    </div>
  );
};
