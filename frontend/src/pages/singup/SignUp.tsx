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
import Box from '@mui/material/Box';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import styled from 'styled-components';
import { Agree } from '../../components/option/Agree';
import BasicStringSelect from '../../components/option/BasicStringSelect';
import RadioButtonsGroup from '../../components/option/ColorToggleButton';
import ColorToggleButton from '../../components/option/ColorToggleButton';

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
  // 이메일 인증체크
  const [checkEmail, setCheckEmail] = useState(0);
  // 패스워드 체크확인
  const [checkPassword, setCheckPassword] = useState(0);

  const [code, setCode] = useState(0);
  const [agree1, setAgree1] = useState(0);
  const [agree2, setAgree2] = useState(0);
  const steps = ['약관동의', '회원 정보', '취향 설문'];
  const ageList = ['10대', '20대', '30대', '40대', '50대', '60대', '70대이상'];

  const {
    register,
    formState: { errors, isSubmitting, isSubmitted },
    handleSubmit,
    control,
    getValues,
    watch,
  } = useForm<IForm>({
    mode: 'onSubmit',
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
  const sendEmail = getValues('email');

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
    console.log(sendEmail);

    if (errors.email) {
      alert('이메일을 다시 확인해 주십시오');
    } else {
      axios
        .post(
          `${process.env.REACT_APP_BASE_URL}/member/sendVerification`,
          sendEmail
        )
        .then((res) => {
          alert('인증번호를 전송했습니다.');

          console.log(res);
        })
        .catch((err) => {
          alert('이메일 전송오류 입니다. 이메일을 다시 확인해주세요');
          console.log('이메일 전송 오류:', err);
        });
    }
  };

  //이메일 인증확인
  const handleCheckEmail = () => {
    axios
      .post(
        `${process.env.REACT_APP_BASE_URL}/member/verification/email/code`,
        { sendEmail, code }
      )
      .then((res) => {
        alert('인증번호를 전송했습니다.');

        console.log(res);
      })
      .catch((err) => {
        alert('인증에 실패했습니다. 인증번호를 확인해주세요');
        console.log('이메일 인증 오류:', err);
      });
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
            <Box sx={{ width: '100%' }}>
              <Stepper activeStep={0} alternativeLabel>
                {steps.map((label) => (
                  <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
            <br />
            <div className={classes.labelContainer}>
              <label htmlFor="">Service 이용약관 동의</label>
              {agree1 ? (
                <BsFillCheckSquareFill
                  style={{ color: '#FE9D3A' }}
                  onClick={() => setAgree1(0)}
                />
              ) : (
                <BsSquare
                  onClick={() => {
                    setAgree1(1);
                  }}
                />
              )}
            </div>
            <Agree />
            <br />
            <br />
            <div className={classes.labelContainer}>
              <label htmlFor="">개인정보 수집 및 이용에 대한 안내</label>
              {agree2 ? (
                <BsFillCheckSquareFill
                  style={{ color: '#FE9D3A' }}
                  onClick={() => setAgree2(0)}
                />
              ) : (
                <BsSquare
                  onClick={() => {
                    setAgree2(1);
                  }}
                />
              )}
            </div>
            <Agree />
            <br />
            <br />
          </div>
        )}

        {progress === 1 && (
          <div className={classes.containerNoHeight}>
            <Box sx={{ width: '100%' }}>
              <Stepper activeStep={1} alternativeLabel>
                {steps.map((label) => (
                  <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
            <div className={classes.InputContainer}>
              {/* 이메일 입력창 */}
              <div className={classes.labelContainer}>
                <label htmlFor="email" className={classes.labelStyle}>
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
                      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                    message: '이메일 형식에 맞지 않습니다.',
                  },
                  required: '이메일은 필수 입력입니다.',
                  onChange(event) {
                    setCheckEmail(0);
                  },
                }}
                onClick={handleSendEmail}
              />

              <label htmlFor="emailcheck" className={classes.labelStyle}>
                이메일 인증번호
              </label>

              {/* 인증번호 확인 */}
              {checkEmail ? (
                <InputContainer>
                  <StyledInput placeholder="인증번호를 입력하세요" />
                  <StyledButtonProps
                    width="4rem"
                    height="1srem"
                    fontSize="0.62rem"
                    radius="15px"
                    background="#C6C5C5"
                    value={code}
                  >
                    인증확인
                  </StyledButtonProps>
                </InputContainer>
              ) : (
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
              )}

              {/* 비밀번호 입력창 */}
              <br />
              <div className={classes.InputContainer}>
                <div className={classes.labelContainer}>
                  <label htmlFor="password" className={classes.labelStyle}>
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
                type="password"
                name="password"
                placeholder="비밀번호를 입력하세요"
                control={control}
                rules={{
                  pattern: {
                    value: /^(?=.*[a-z])(?=.*\d)[a-zA-Z\d]{8,}$/,
                    message: '영어,숫자,특수문자가 포함되어야합니다..',
                  },
                  minLength: {
                    value: 8,
                    message: '8자리 이상 비밀번호를 사용하세요.',
                  },
                  required: '비밀번호는 필수 입력입니다.',

                  onChange(event) {
                    setCheckPassword(0);
                  },
                }}
                aria-invalid={
                  isSubmitted ? (errors.password ? 'true' : 'false') : undefined
                }
              />
            </div>
            {/* 비밀번호확인 */}
            <div className={classes.labelContainer}>
              <label htmlFor="passwordconfirm" className={classes.labelStyle}>
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
                type="password"
                {...register('passwordconfirm', {
                  required: true,
                  validate: {
                    check: (val) => {
                      const originalPassword = getValues('password');
                      if (originalPassword && originalPassword !== val) {
                        return '비밀번호가 일치하지 않습니다.';
                      } else {
                        setCheckPassword(1);
                      }
                    },
                  },
                })}
                placeholder="비밀번호를 재입력하세요"
              />
            </InputContainer>
            <br />
            <br />
            <div className={classes.inputContainer}>
              <StyledButton
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="#7D7B7B;"
                fontSize="1.25rem"
                background="#F9F9F9"
                radius="10px"
                onClick={() => {
                  if (progress > 0) {
                    setProgress((prevProgress) => prevProgress - 1);
                  }
                }}
              >
                이전
              </StyledButton>
              &nbsp;&nbsp;&nbsp;
              <StyledButton
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
                onClick={() => {
                  if (checkEmail === 1 && checkPassword === 1) {
                    if (progress < 5) {
                      setProgress((prevProgress) => prevProgress + 1);
                    }
                  } else {
                    alert('이메일인증과 비밀번호를 확인해주세요!');
                  }
                }}
              >
                다음
              </StyledButton>
            </div>
          </div>
        )}
        {progress === 2 && (
          <div className={classes.containerNoHeight}>
            <Box sx={{ width: '100%' }}>
              <Stepper activeStep={1} alternativeLabel>
                {steps.map((label) => (
                  <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
            <br />
            {/* 닉네임 적기 */}
            <div className={classes.labelContainer}>
              <label htmlFor="email" className={classes.labelStyle}>
                닉네임
              </label>
              {errors.nickname && (
                <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
                  {errors.nickname.message}
                </small>
              )}
            </div>

            <StyledBasicInput
              name="nickname"
              placeholder="닉네임을 입력하세요"
              control={control}
              rules={{
                minLength: {
                  value: 8,
                  message: '8자리이하 닉네임을 사용해주세요.',
                },
                required: '닉네임은 필수 입력입니다.',
              }}
            />

            <div className={classes.labelContainer}>
              <div>
                <label className={classes.labelStyle} htmlFor="nickname">
                  연령
                </label>
                <BasicStringSelect name="age" options={ageList} />
              </div>
              <div>
                <label className={classes.labelStyle} htmlFor="nickname">
                  성별
                </label>
                <div>
                  <ColorToggleButton />
                </div>
              </div>
            </div>
            <div className={classes.inputContainer}>
              <StyledButton
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="#7D7B7B;"
                fontSize="1.25rem"
                background="#F9F9F9"
                radius="10px"
                onClick={() => {
                  if (progress > 0) {
                    setProgress((prevProgress) => prevProgress - 1);
                  }
                }}
              >
                이전
              </StyledButton>
              &nbsp;&nbsp;&nbsp;
              <StyledButton
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
                onClick={() => {
                  if (checkEmail === 1 && checkPassword === 1) {
                    if (progress < 5) {
                      setProgress((prevProgress) => prevProgress + 1);
                    }
                  } else {
                    alert('이메일인증과 비밀번호를 확인해주세요!');
                  }
                }}
              >
                다음
              </StyledButton>
            </div>
          </div>
        )}
        {progress === 3 && <p>세번째페이지</p>}
        {progress === 4 && <p>네번째페이지</p>}
        {progress === 5 && <p>다섯번째페이지</p>}
        {progress === 6 && <p>여섯번째페이지</p>}
      </form>
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
            if (progress > 0) {
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
            if (progress < 5) {
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
