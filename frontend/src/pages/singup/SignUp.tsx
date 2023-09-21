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

interface IForm {
  email: string;
  password: string;
  nickname: string;
  age: number;
  sex: string;
  height: number;
  weight: number;
  activity: number;
}

export const SignUp = () => {
  const [isAutoLogin, setIsAutoLogin] = useState(0);
  const [isRememberId, setIsRememberId] = useState(0);
  const [progress, setProgress] = useState(0);
  const [chekcEmail, setCheckEmail] = useState(0);

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
      nickname: '',
      age: 0,
      sex: '',
      height: 0,
      weight: 0,
      activity: 0,
    },
  });

  const handleSignUp: SubmitHandler<IForm> = (data) => {
    console.log(data);
    console.log(errors);
  };

  //인증확인 체크
  const handleCheckEmail = () => {
    // api요청해서 맞으면 setcheckemail
  };

  return (
    <div className={classes.container}>
      <br />
      <div>
        <img
          src="images/foodreco.png"
          alt="sdsd"
          width={'200rem'}
          height={'100rem'}
        />
      </div>
      <br />
      <br />
      <form onSubmit={handleSubmit(handleSignUp)}>
        <div className={classes.InputContainer}>
          {/* 이메일 입력창 */}
          <div className={classes.labelContainer}>
            <label
              htmlFor="email"
              style={{ color: '#525252', fontSize: '1rem', fontWeight: '600' }}
            >
              이메일
            </label>
            {errors.email && (
              <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
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
            }}
          />

          <label
            htmlFor="emailcheck"
            style={{ color: '#525252', fontSize: '1rem', fontWeight: '600' }}
          >
            이메일 인증번호
          </label>

          {/* 인증번호 확인 */}
          <InputContainer>
            <StyledInput placeholder="인증번호를 입력하세요" />
            <StyledButton
              width="4rem"
              height="1srem"
              fontSize="0.62rem"
              radius="15px"
              onClick={handleCheckEmail}
            >
              인증확인
            </StyledButton>
          </InputContainer>

          {/* 비밀번호 입력창 */}
          <br />
          <div className={classes.InputContainer}>
            <label
              htmlFor="password"
              style={{ color: '#525252', fontSize: '1rem', fontWeight: '600' }}
            >
              비밀번호
            </label>
            {errors.password && (
              <small role="alert" style={{ color: 'red', fontSize: '10px' }}>
                {errors.password.message}
              </small>
            )}
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
            }}
            aria-invalid={
              isSubmitted ? (errors.password ? 'true' : 'false') : undefined
            }
          />
        </div>
        {/* 비밀번호확인 */}
        <label
          style={{ color: '#525252', fontSize: '1rem', fontWeight: '600' }}
          htmlFor="passwordcheck"
        >
          비밀번호 확인
        </label>
        <InputContainer>
          <StyledInput placeholder="비밀번호를 재입력하세요" />
        </InputContainer>
        <br />
        <br />
      </form>
      <div className={classes.inputContainer}>
        <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="9.4rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
          color="#7D7B7B;"
          fontSize="1.25rem"
          background="#F9F9F9"
          radius="20px"
        >
          이전
        </StyledButton>
        <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="9.4rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
          color="white"
          fontSize="1.25rem"
          background="#FE9D3A"
          radius="20px"
        >
          다음
        </StyledButton>
      </div>
    </div>
  );
};
