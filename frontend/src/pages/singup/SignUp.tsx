import React, { useState } from 'react';
import StyledIdInputIcon from '../../components/inputs/StyledIdInputIcon';
import StyledPwInputIcon from '../../components/inputs/StyledPwInputIcon';
import StyledButton from '../../styles/StyledButton';
import classes from './SignUp.module.css';
import { SubmitHandler, useForm } from 'react-hook-form';
import { BsSquare } from 'react-icons/bs';
import { BsFillCheckSquareFill } from 'react-icons/bs';
import StyledBasicInput from '../../components/inputs/StyledBasicInput';

import StyledEmailInput from '../../components/inputs/StyledEmailInput';
import {
  InputContainer,
  StyledInput,
} from './../../components/inputs/StyledInputs';
import axios from 'axios';
import StyledButtonProps from '../../styles/StyledButtonProps';
import HeaderLogo from '../../components/header/HeaderLogo';

interface IForm {
  email: string;
  password: string;
  nickname: string;
  age: number;
  sex: string;
  height: number;
  weight: number;
  activity: number;
  passwordconfirm: string;
}

interface EmailCheck {
  email: string;
}

export const SignUp = () => {
  const [isAutoLogin, setIsAutoLogin] = useState(0);
  const [isRememberId, setIsRememberId] = useState(0);
  const [progress, setProgress] = useState(0);
  const [chekcEmail, setCheckEmail] = useState(0);
  const [email, setEmail] = useState('');
  const [checkPassword, setCheckPassword] = useState(0);
  // useForm에서 watch 함수를 가져옵니다.

  const {
    register,
    formState: { errors, isSubmitting, isSubmitted },
    handleSubmit,
    control,
    getValues,
    watch,
  } = useForm<IForm>({
    mode: 'onChange',
    defaultValues: {
      email: '',
      password: '',
      nickname: '',
      age: 0,
      sex: '',
      height: 0,
      weight: 0,
      activity: 0,
      passwordconfirm: '',
    },
  });
  // watch 함수를 사용하여 email 값을 실시간으로 관찰합니다.
  const watchedEmail = watch('email');

  // 회원가입 로직
  const handleSignUp: SubmitHandler<IForm> = (data) => {
    const { email, password, nickname, age, sex, height, weight, activity } =
      data;
    // axios
    //   .post(`${process.env.REACT_APP_BASE_URL}signup/email`, {
    //     email,
    //     password,
    //   })
    //   .catch((error) => {
    //     console.dir(error);
    //   });
  };

  //이메일 인증 요청
  const handleSendEmail = () => {
    console.log('sadfasdf');
    console.log(watchedEmail);

    // if (errors.email) {
    //   alert('이메일을 다시 확인해 주십시오');
    // } else {
    //   axios
    //     .post(`${process.env.REACT_APP_BASE_URL}signup/email`, {
    //       email,
    //     })
    //     .then((res) => {
    //       alert('인증번호를 전송했습니다.');
    //       console.log(res);
    //     })
    //     .catch((err) => {
    //       alert('이메일을 다시 확인해 주십시오');
    //       console.log('이메일 전송 오류:', err);
    //     });
    // }
  };

  //이메일 인증확인
  const handleCheckEmail = () => {
    // api요청해서 맞으면 setcheckemail
  };

  return (
    <div className={classes.container}>
      <br />
      <HeaderLogo />
      <br />
      <br />
      <br />
      <p style={{ color: '#525252', fontSize: '1.5rem', fontWeight: 'bold' }}>
        회원가입
      </p>
      <br />
      <form onSubmit={handleSubmit(handleSignUp)}>
        {/* 회원가입 첫번째 페이지 */}
        {progress === 0 && (
          <div className={classes.containerNoHeight}>
            <div className={classes.InputContainer}>
              {/* 이메일 입력창 */}
              <div className={classes.labelContainer}>
                <label
                  htmlFor="email"
                  style={{
                    color: '#525252',
                    fontSize: '1rem',
                    fontWeight: '600',
                  }}
                >
                  이메일 입력
                </label>
                {errors.email && (
                  <small
                    role="alert"
                    style={{ color: 'red', fontSize: '10px' }}
                  >
                    {errors.email.message}
                  </small>
                )}
              </div>
              <StyledEmailInput
                children="인증요청"
                name="email" // 필드의 이름
                placeholder="이메일을 입력하세요"
                control={control} // useForm에서 가져온 control
                rules={{
                  pattern: {
                    value:
                      /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i,
                    message: '이메일 형식에 맞지 않습니다.',
                  },
                  required: '이메일은 필수 입력입니다.',
                }}
                onClick={handleSendEmail}
              />

              <label
                htmlFor="emailcheck"
                style={{
                  color: '#525252',
                  fontSize: '1rem',
                  fontWeight: '600',
                }}
              >
                이메일 인증번호
              </label>

              {/* 인증번호 확인 */}
              <InputContainer>
                <StyledInput placeholder="인증번호를 입력하세요" />
                <StyledButtonProps
                  width="4rem"
                  height="1srem"
                  fontSize="0.62rem"
                  radius="15px"
                  onClick={handleCheckEmail}
                >
                  인증확인
                </StyledButtonProps>
              </InputContainer>

              {/* 비밀번호 입력창 */}
              <br />
              <div className={classes.InputContainer}>
                <div className={classes.labelContainer}>
                  <label
                    htmlFor="password"
                    style={{
                      color: '#525252',
                      fontSize: '1rem',
                      fontWeight: '600',
                    }}
                  >
                    비밀번호 입력
                  </label>
                  {errors.password && (
                    <small
                      role="alert"
                      style={{ color: 'red', fontSize: '10px' }}
                    >
                      {errors.password.message}
                    </small>
                  )}
                </div>
              </div>

              <StyledBasicInput
                name="password"
                placeholder="비밀번호를 입력하세요"
                control={control}
                rules={{
                  minLength: {
                    value: 8,
                    message: '8자리 이상 비밀번호를 사용하세요.',
                  },
                  required: '비밀번호는 필수 입력입니다.',
                }}
                aria-invalid={
                  isSubmitted ? (errors.password ? 'true' : 'false') : undefined
                }
              />
            </div>
            {/* 비밀번호확인 */}

            <div className={classes.labelContainer}>
              <label
                htmlFor="passwordconfirm"
                style={{
                  color: '#525252',
                  fontSize: '1rem',
                  fontWeight: '600',
                }}
              >
                비밀번호 확인
              </label>
              {errors.passwordconfirm && (
                <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
                  {errors.passwordconfirm.message}
                </small>
              )}
            </div>

            <InputContainer>
              <StyledInput
                id="passwordconfirm"
                type="passwordconfirm"
                {...register('passwordconfirm', {
                  required: true,
                  validate: {
                    check: (val) => {
                      const originalPassword = getValues('password');
                      if (originalPassword && originalPassword !== val) {
                        return '비밀번호가 일치하지 않습니다.';
                      } else {
                        const checkPasswords = () => {
                          setCheckPassword(1);
                        };
                      }
                    },
                  },
                })}
                placeholder="비밀번호를 재입력하세요"
              />
            </InputContainer>
          </div>
        )}
      </form>
      <br />
      <br />
      <div className={classes.inputContainer}>
        <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="9.0rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
          color="#7D7B7B;"
          fontSize="1.25rem"
          background="#F9F9F9"
          radius="10px"
          onClick={() => {
            if (progress !== 0) {
              setProgress((prevProgress) => prevProgress - 1);
            }
          }}
        >
          이전
        </StyledButton>
        &nbsp;&nbsp;&nbsp;
        <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="9.0rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
          color="white"
          fontSize="1.25rem"
          background="#FE9D3A"
          radius="10px"
          onClick={() => {
            if (progress !== 6) {
              setProgress((prevProgress) => prevProgress + 1);
            }
          }}
        >
          다음
        </StyledButton>
      </div>
    </div>
  );
};
