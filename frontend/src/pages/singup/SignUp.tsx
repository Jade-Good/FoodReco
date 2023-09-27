import React, { useState } from "react";
import StyledIdInputIcon from "../../components/inputs/StyledIdInputIcon";
import StyledPwInputIcon from "../../components/inputs/StyledPwInputIcon";
import StyledButton from "../../styles/StyledButton";
import classes from "./SignUp.module.css";
import { SubmitHandler, useFieldArray, useForm } from "react-hook-form";
import { BsSquare } from "react-icons/bs";
import { BsFillCheckSquareFill } from "react-icons/bs";
import StyledBasicInput from "../../components/inputs/StyledBasicInput";

import StyledEmailInput from "../../components/inputs/StyledEmailInput";
import {
  InputContainer,
  StyledInput,
} from "./../../components/inputs/StyledInputs";
import axios from "axios";
import StyledButtonProps from "../../styles/StyledButtonProps";
import HeaderLogo from "../../components/header/HeaderLogo";
import Box from "@mui/material/Box";
import Stepper from "@mui/material/Stepper";
import Step from "@mui/material/Step";
import StepLabel from "@mui/material/StepLabel";
import styled from "styled-components";
import { Agree } from "../../components/option/Agree";
import BasicSelect from "../../components/option/BasicSelect";
import RadioButtonsGroup from "../../components/option/ColorToggleButton";
import ColorToggleButton from "../../components/option/ColorToggleButton";
import { useNavigate } from "react-router-dom";
import StyledBasicInputUnit from "../../components/inputs/StyledBasicInputUnit";
import { ChooseLikeFood } from "./ChooseLikeFood";
import ToggleButton from "@mui/material/ToggleButton";

interface IForm {
  email: string;
  emailValidation: number;
  password: string;
  nickname: string;
  age: string;
  sex: string;
  height: number;
  weight: number;
  activity: number;
  passwordconfirm: string;
  time: number;
}
interface ExerciseRates {
  [exercise: string]: {
    [time: number]: number;
  };
}
interface EmailCheck {
  email: string;
}

const ToggleButtonContainer = styled.div`
  display: flex;
  flex-wrap: wrap; /* Allow ToggleButtons to wrap to the next line */
`;

const CustomToggleButton = styled(ToggleButton)`
  && {
    border-radius: 1rem; /* Increase border-radius for a more rounded appearance */
    margin: 0.2rem; /* Add margin to create spacing between ToggleButtons */
    // border: 1px solid orange;
    // color: orange;
  }
`;

export const SignUp = () => {
  const navigate = useNavigate();
  const [isAutoLogin, setIsAutoLogin] = useState(0);
  const [isRememberId, setIsRememberId] = useState(0);
  // 회원가입 페이지 체크
  const [progress, setProgress] = useState(0);
  // 이메일 인증체크
  const [checkEmail, setCheckEmail] = useState(0);
  // 패스워드 체크확인
  const [checkPassword, setCheckPassword] = useState(0);

  const [code, setCode] = useState("0");
  //개인정보 동의 1
  const [agree1, setAgree1] = useState(0);
  // 개인정보 동의 2
  const [agree2, setAgree2] = useState(0);

  const steps = ["약관동의", "회원 정보", "취향 설문"];
  const ageList = ["10대", "20대", "30대", "40대", "50대", "60대", "70대이상"];
  const sexList = ["남자", "여자"];
  const activityList = ["운동안함", "걷기", "헬스", "수영", "자전거"];
  const timeList = [0.5, 1, 1.5, 2, 2.5, 3];
  // 걷기 1시간 6750 , 헬스 1시간 14700, 수영: 12850, 자전거:11050
  const [likefood, setLikeFood] = useState<string[]>([]);
  const [unlikeFood, setUnlikeFood] = useState<string[]>([]);
  const foodList = [
    "시과",
    "자장면",
    "소고기",
    "돼지고기",
    "피자",
    "파스타",
    "된찌",
    "김찌",
  ];
  const exerciseRates: ExerciseRates = {
    걷기: {
      0.5: 6750,
      1: 13500,
      1.5: 20250,
      2: 27000,
      2.5: 33750,
      3: 40500,
    },
    헬스: {
      0.5: 14700,
      1: 29400,
      1.5: 44100,
      2: 58800,
      2.5: 73500,
      3: 88200,
    },
    수영: {
      0.5: 12850,
      1: 25700,
      1.5: 38550,
      2: 51400,
      2.5: 64250,
      3: 77100,
    },
    자전거: {
      0.5: 11050,
      1: 22100,
      1.5: 33150,
      2: 44200,
      2.5: 55250,
      3: 66300,
    },
  };

  //좋아하는 음식 선택
  const toggleLikeFood = (food: string) => {
    if (likefood.includes(food)) {
      // 음식이 이미 좋아요 목록에 있는 경우, 제거합니다.
      setLikeFood(likefood.filter((item) => item !== food));
    } else {
      // 음식이 좋아요 목록에 없는 경우, 추가합니다.
      setLikeFood([...likefood, food]);
    }
  };

  //싫어하는 음식선택
  const toggleUnlikeFood = (food: string) => {
    if (unlikeFood.includes(food)) {
      // 음식이 이미 좋아요 목록에 있는 경우, 제거합니다.
      setUnlikeFood(unlikeFood.filter((item) => item !== food));
    } else {
      // 음식이 좋아요 목록에 없는 경우, 추가합니다.
      setUnlikeFood([...unlikeFood, food]);
    }
  };

  const {
    register,
    formState: { errors, isSubmitting, isSubmitted },
    handleSubmit,
    control,
    getValues,
    watch,
  } = useForm<IForm>({
    mode: "onSubmit",
    defaultValues: {
      email: "",
      emailValidation: undefined,
      password: "",
      nickname: "",
      age: "",
      sex: "",
      height: undefined,
      weight: undefined,
      activity: undefined,
      passwordconfirm: "",
      time: undefined,
    },
  });
  // watch 함수를 사용하여 email 값을 실시간으로 관찰합니다.
  const watchedEmail = watch("email");
  const sendEmail = getValues("email");

  // 회원가입 로직
  const handleSignUp: SubmitHandler<IForm> = (data) => {
    console.log(data);

    const {
      email,
      emailValidation,
      password,
      nickname,
      age,
      sex,
      height,
      weight,
      activity,
      passwordconfirm,
      time,
    } = data;
    const ages = parseInt(age.slice(0, 2));
    const walkingRate = exerciseRates[activity][time];
    const datas = {
      email: email,
      password: password,
      nickname: nickname,
      sex: sex,
      activity: walkingRate,
      age: ages,
      weight: weight,
      height: height,
    };
    console.log(datas);
    if (errors.email || errors.nickname || errors.password) {
      console.log(errors);
      alert("정보를 다시 확인해주세요!");
    } else {
      axios
        .post(`${process.env.REACT_APP_BASE_URL}/member/regist`, {
          email: email,
          password: password,
          nickname: nickname,
          sex: sex,
          activity: walkingRate,
          age: ages,
          weight: weight,
          height: height,
        })
        .then((res) => {
          console.log(res);
          navigate("/signup/complete");
          // alert("회원가입이 완료되었습니다!")
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  //이메일 인증 요청
  const handleSendEmail = () => {
    setCheckEmail(0);
    console.log(sendEmail);

    // const formData = new FormData();
    // formData.append("email", sendEmail);

    if (errors.email) {
      console.log(errors.email);
      alert("이메일을 다시 확인해 주십시오");
    } else {
      console.log(
        `${process.env.REACT_APP_BASE_URL}/member/verification/email`
      );

      axios
        .post(
          `${process.env.REACT_APP_BASE_URL}/member/verification/email`,
          { email: sendEmail }
          // {
          //   headers: { 'Content-Type': `application/json` },
          // }
        )
        .then((res) => {
          alert("인증번호를 전송했습니다.");

          console.log(res);
        })
        .catch((err) => {
          alert("이메일 전송오류 입니다. 이메일을 다시 확인해주세요");
          console.log("이메일 전송 오류:", err);
        });
    }
  };

  //이메일 인증확인
  const handleCheckEmail = () => {
    // const formData = new FormData();
    // formData.append("email", sendEmail);
    // formData.append("code", code);

    axios
      .post(
        `${process.env.REACT_APP_BASE_URL}/member/verification/email/code`,
        {
          email: sendEmail,
          // code: String(code),
          code: code,
        }
        // {
        //   headers: { 'Content-Type': `application/json` },
        // }
      )
      .then((res) => {
        console.log(res);
        if (res.status === 202) {
          console.log("인증확인");
          setCheckEmail(1);
        } else {
          alert("인증번호가 틀립니다.");
        }
      })
      .catch((err) => {
        console.log(code);
        alert("인증에 실패했습니다. 인증번호를 확인해주세요");
        console.log("이메일 인증 오류:", err);
      });
  };

  return (
    <div className={classes.container}>
      <br />
      {progress !== 5 && <HeaderLogo />}
      <br />
      <br />
      <br />
      {progress !== 5 && (
        <p style={{ color: "#525252", fontSize: "1.5rem", fontWeight: "bold" }}>
          회원가입
        </p>
      )}
      <br />

      <form onSubmit={handleSubmit(handleSignUp)}>
        {/* 회원가입 첫번째 페이지 */}
        {progress === 0 && (
          <div className={classes.containerNoHeight}>
            <Box sx={{ width: "100%" }}>
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
                  style={{ color: "#FE9D3A" }}
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
                  style={{ color: "#FE9D3A" }}
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
            <div className={classes.inputContainer}>
              <StyledButton
                type="button"
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="#7D7B7B;"
                fontSize="1.25rem"
                background="#F9F9F9"
                radius="10px"
                onClick={() => {
                  navigate("/");
                }}
              >
                취소
              </StyledButton>
              &nbsp;&nbsp;&nbsp;
              <StyledButton
                type="button"
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
                onClick={() => {
                  if (agree1 === 1 && agree2 === 1) {
                    if (progress < 5) {
                      setProgress((prevProgress) => prevProgress + 1);
                    }
                  } else {
                    alert("개인정보 동의를 모두 동의해주셔야 이용가능합니다.");
                  }
                }}
              >
                다음
              </StyledButton>
            </div>
          </div>
        )}
        {/* 회원가입 두번째페이지 */}
        {progress === 1 && (
          <div className={classes.containerNoHeight}>
            <Box sx={{ width: "100%" }}>
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
                    style={{ color: "red", fontSize: "10px" }}
                  >
                    {errors.email.message}
                  </small>
                )}
              </div>
              <StyledEmailInput
                type="button"
                children="인증요청"
                name="email" // 필드의 이름
                placeholder="이메일을 입력하세요"
                control={control} // useForm에서 가져온 control
                rules={{
                  pattern: {
                    value:
                      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                    message: "이메일 형식에 맞지 않습니다.",
                  },
                  required: "이메일은 필수 입력입니다.",
                  onChange() {
                    setCheckEmail(0);
                  },
                }}
                onClick={handleSendEmail}
              />

              {/* 인증번호 확인 */}
              <label htmlFor="emailcheck" className={classes.labelStyle}>
                이메일 인증번호
              </label>

              {checkEmail ? (
                <StyledEmailInput
                  children="인증확인"
                  name="emailValidation"
                  placeholder="인증번호를 입력하세요"
                  control={control}
                  rules={{
                    onChange(e: any) {
                      setCode(e.target.value);
                      console.log("codeChange 적용");
                    },
                  }}
                  onClick={handleCheckEmail}
                  background="gray"
                />
              ) : (
                <StyledEmailInput
                  children="인증확인"
                  name="emailValidation"
                  placeholder="인증번호를 입력하세요"
                  control={control}
                  rules={{
                    onChange(e: {
                      target: { value: React.SetStateAction<string> };
                    }) {
                      setCode(e.target.value);
                      console.log("codeChange 적용");
                    },
                  }}
                  onClick={handleCheckEmail}
                />
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
                      style={{ color: "red", fontSize: "10px" }}
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
                    value:
                      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/,
                    message: "영어,숫자,특수문자가 포함되어야합니다..",
                  },
                  minLength: {
                    value: 8,
                    message: "8자리 이상 비밀번호를 사용하세요.",
                  },
                  required: "비밀번호는 필수 입력입니다.",

                  onChange() {
                    setCheckPassword(0);
                  },
                }}
              />
            </div>
            {/* 비밀번호확인 */}
            <div className={classes.labelContainer}>
              <label htmlFor="passwordconfirm" className={classes.labelStyle}>
                비밀번호 확인
              </label>
              {errors.passwordconfirm && (
                <small role="alert" style={{ color: "red", fontSize: "10px" }}>
                  {errors.passwordconfirm.message}
                </small>
              )}
            </div>
            <InputContainer>
              <StyledInput
                id="passwordconfirm"
                type="password"
                {...register("passwordconfirm", {
                  required: true,
                  validate: {
                    check: (val: string) => {
                      const originalPassword = getValues("password");
                      if (originalPassword && originalPassword !== val) {
                        return "비밀번호가 일치하지 않습니다.";
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
                type="button"
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
                type="button"
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
                    alert("이메일인증과 비밀번호를 확인해주세요!");
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
            <Box sx={{ width: "100%" }}>
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
            <div className={classes.mB}>
              <div className={classes.labelContainer}>
                <label htmlFor="email" className={classes.labelStyle}>
                  닉네임
                </label>
                {errors.nickname && (
                  <small
                    role="alert"
                    style={{ color: "red", fontSize: "10px" }}
                  >
                    {errors.nickname.message}
                  </small>
                )}
              </div>

              <StyledBasicInput
                maxLength={8}
                name="nickname"
                placeholder="닉네임을 입력하세요"
                control={control}
                rules={{
                  maxLength: {
                    value: 8,
                    message: "8자리 이하 닉네임을 사용해주세요.",
                  },
                  minLength: {
                    value: 2,
                    message: "2자리 이상 닉네임을 사용해주세요",
                  },
                  required: "닉네임은 필수 입력입니다.",
                }}
              />
            </div>

            <div className={classes.mB}>
              <div className={classes.labelContainer}>
                <div>
                  <label className={classes.labelStyle} htmlFor="age">
                    연령
                  </label>

                  <BasicSelect
                    control={control}
                    {...register("age")}
                    name="age"
                    label="age"
                    options={ageList}
                  />
                </div>
                <div>
                  <label className={classes.labelStyle} htmlFor="gender">
                    성별
                  </label>

                  <BasicSelect
                    control={control}
                    {...register("sex")}
                    name="sex"
                    label="sex"
                    options={sexList}
                  />
                </div>
              </div>
              <div className={classes.labelContainer}>
                <div>
                  <label className={classes.labelStyle} htmlFor="height">
                    신장
                  </label>

                  <StyledBasicInputUnit
                    type="height"
                    unit="CM"
                    name="height"
                    control={control}
                    // styleContainer={{ borderColor: '#c3c7c9' }}
                  />
                </div>
                <div>
                  <label className={classes.labelStyle} htmlFor="weight">
                    체중
                  </label>

                  <StyledBasicInputUnit
                    type="weight"
                    unit="kg"
                    name="weight"
                    control={control}
                    // styleContainer={{ borderColor: '#c3c7c9' }}
                  />
                </div>
              </div>
              <div className={classes.labelContainer}>
                <div>
                  <label className={classes.labelStyle} htmlFor="activity">
                    운동종류
                  </label>

                  <BasicSelect
                    control={control}
                    {...register("activity")}
                    name="activity"
                    label="activity"
                    options={activityList}
                  />
                </div>
                <div>
                  <label className={classes.labelStyle} htmlFor="time">
                    운동시간
                  </label>

                  <BasicSelect
                    control={control}
                    {...register("time")}
                    name="time"
                    label="time"
                    options={timeList}
                  />
                </div>
              </div>
            </div>
            <div className={classes.inputContainer}>
              <StyledButton
                type="button"
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
                type="button"
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
                onClick={() => {
                  if (errors.nickname) {
                    alert("닉네임을 확인해주세요");
                  } else {
                    if (progress < 5) {
                      setProgress((prevProgress) => prevProgress + 1);
                    }
                  }
                }}
              >
                다음
              </StyledButton>
            </div>
          </div>
        )}
        {/* 좋아하는 음식 선택하기 */}
        {progress === 3 && (
          <div>
            <Box sx={{ width: "100%" }}>
              <Stepper activeStep={1} alternativeLabel>
                {steps.map((label) => (
                  <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
            <br />
            <label>좋아하는 음식을 선택해 주세요!</label>
            <ToggleButtonContainer>
              {foodList.map((food, index) => (
                <CustomToggleButton
                  value="check"
                  selected={likefood.includes(food)} // 버튼 선택 상태는 좋아요 목록에 음식이 있는지 여부에 따라 결정됩니다.
                  onClick={() => toggleLikeFood(food)} // 버튼을 클릭할 때 toggleFood 함수를 호출하여 음식을 추가하거나 제거합니다.
                  key={index}
                >
                  # {food}
                </CustomToggleButton>
              ))}
            </ToggleButtonContainer>
            <div className={classes.inputContainer}>
              <StyledButton
                type="button"
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
                type="button"
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)" /* 그림자 스타일 지정 */
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
                onClick={() => {
                  if (progress > 0 && likefood.length >= 5) {
                    setProgress((prevProgress) => prevProgress + 1);
                  } else {
                    alert("최소 5개 이상 좋아하는 음식을 선택해주세요");
                  }
                }}
              >
                다음
              </StyledButton>
            </div>
          </div>
        )}
        {/* 싫어하는 음식 선택하기 */}
        {progress === 4 && (
          <div>
            <Box sx={{ width: "100%" }}>
              <Stepper activeStep={1} alternativeLabel>
                {steps.map((label) => (
                  <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
            <br />
            <label>싫어하는 음식을 선택해 주세요!</label>
            <ToggleButtonContainer>
              {foodList.map((food, index) => (
                <CustomToggleButton
                  value="check"
                  selected={unlikeFood.includes(food)} // 버튼 선택 상태는 좋아요 목록에 음식이 있는지 여부에 따라 결정됩니다.
                  onClick={() => toggleUnlikeFood(food)} // 버튼을 클릭할 때 toggleFood 함수를 호출하여 음식을 추가하거나 제거합니다.
                  key={index}
                >
                  # {food}
                </CustomToggleButton>
              ))}
            </ToggleButtonContainer>
            <div className={classes.inputContainer}>
              <StyledButton
                type="button"
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
                type="submit"
                disabled={isSubmitting}
                width="9.0rem"
                boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
                color="white"
                fontSize="1.25rem"
                background="#FE9D3A"
                radius="10px"
              >
                완료
              </StyledButton>
            </div>
          </div>
        )}
        <br />
        {/* 나중에 삭제해야함 */}
        {/* <StyledButton
          type="submit"
          disabled={isSubmitting}
          width="9.0rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
          color="white"
          fontSize="1.25rem"
          background="#FE9D3A"
          radius="10px"
        >
          제출
        </StyledButton> */}
      </form>

      {/* <div className={classes.inputContainer}> */}
      {/* <StyledButton
          disabled={isSubmitting}
          type="submit"
          width="9.0rem"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
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
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
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
        </StyledButton> */}
      {/* </div> */}
    </div>
  );
};
