import React, { useEffect, useState } from "react";
import StyledIdInputIcon from "../../components/inputs/StyledIdInputIcon";
import StyledPwInputIcon from "../../components/inputs/StyledPwInputIcon";
import StyledButton from "../../styles/StyledButton";
import classes from "./Login.module.css";
import { SubmitHandler, useForm } from "react-hook-form";
import { BsSquare } from "react-icons/bs";
import { BsFillCheckSquareFill } from "react-icons/bs";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/atoms/userState";

interface IForm {
  email: string;
  password: string;
}

export const Login = () => {
  const [isAutoLogin, setIsAutoLogin] = useState(0);
  const [isRememberId, setIsRememberId] = useState(0);
  const navigate = useNavigate();
  // const dispatch = useDispatch();
  const [user, setUser] = useRecoilState(userState);
  const JWT_EXPIRY_TIME = 3600 * 1000; // 만료 시간 (24시간 밀리 초로 표현)

  const {
    register,
    formState: { errors, isSubmitting, isSubmitted },
    handleSubmit,
    control,
  } = useForm<IForm>({
    mode: "onSubmit",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const handleLogin: SubmitHandler<IForm> = (data) => {
    const { email, password } = data;
    if (errors.email) {
      console.log(errors.email);
      alert("이메일을 다시 확인해 주십시오");
    } else {
      axios
        .post(
          `${process.env.REACT_APP_BASE_URL}/member/login`,

          { email: email, password: password }
          // {
          //   headers: { 'Content-Type': `application/json` },
          // }
        )
        .then((res) => {
          console.log(data);
          console.log("로그인중", res);
          const nickcname = res.data.nickname;
          const accessToken = res.headers.authorization;
          const email = res.data.email;
          const refreshToken = res.headers.authorizationRefresh;

          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${accessToken}`;
          // localStorage.setItem('accesstoken', accessToken);

          setUser((prevUser) => ({
            ...prevUser,
            refreshToken: refreshToken,
            accessToken: accessToken,
            nickname: nickcname,
            email: email,
          }));

          // accessToken 만료하기 1분 전에 로그인 연장
          setTimeout(handleSilentRefresh, JWT_EXPIRY_TIME - 60000);

          navigate("/");
        })
        .catch((err) => {
          handleSilentRefresh(data);
          console.log("이메일 전송 오류:", err);
        });
    }
  };
  const handleSilentRefresh: SubmitHandler<IForm> = (data) => {
    console.log("refreshtoken axois", data);
    axios
      .post("/silent-refresh", data)
      .then((res) => {
        const nickcname = res.data.nickname;
        const accessToken = res.headers.authorization;
        const email = res.data.email;
        const refreshToken = res.headers.authorizationRefresh;

        axios.defaults.headers.common[
          "Authorization"
        ] = `Bearer ${accessToken}`;
        // localStorage.setItem('accesstoken', accessToken);

        setUser((prevUser) => ({
          ...prevUser,
          refreshToken: refreshToken,
          accessToken: accessToken,
          nickname: nickcname,
          email: email,
        }));

        // accessToken 만료하기 1분 전에 로그인 연장
        setTimeout(handleSilentRefresh, JWT_EXPIRY_TIME - 60000);

        navigate("/");
      })
      .catch((error) => {
        console.log(error);
        alert("이메일과 비밀번호를 확인해주세요");
      });
  };
  const notify = () => toast("This is a toast notification !");

  return (
    <div className={classes.container}>
      <br />
      <div>
        <img
          src="images/foodreco.png"
          alt="sdsd"
          width={"200rem"}
          height={"100rem"}
        />
      </div>
      <br />
      <br />

      <button onClick={notify}>Notify !</button>
      <ToastContainer />
      <form onSubmit={handleSubmit(handleLogin)}>
        <div className={classes.inputContainer}>
          {errors.email && (
            <small role="alert" style={{ color: "red", fontSize: "10px" }}>
              {errors.email.message}
            </small>
          )}
          {/* 이메일 입력창 */}
          <StyledIdInputIcon
            name="email" // 필드의 이름
            placeholder="이메일"
            control={control} // useForm에서 가져온 control
            rules={{
              pattern: {
                value:
                  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i,
                message: "이메일 형식에 맞지 않습니다.",
              },
            }}
          />

          {errors.password && (
            <small role="alert" style={{ color: "red", fontSize: "10px" }}>
              {errors.password.message}
            </small>
          )}

          {/* 비밀번호 입력창 */}
          <br />
          <StyledPwInputIcon
            name="password"
            placeholder="비밀번호"
            control={control}
            rules={{
              minLength: {
                value: 8,
                message: "8자리 이상 비밀번호를 사용하세요.",
              },
            }}
            aria-invalid={
              isSubmitted ? (errors.password ? "true" : "false") : undefined
            }
          />
        </div>

        <div className={classes.autoLoginContainer}>
          {isAutoLogin ? (
            <BsFillCheckSquareFill
              style={{
                height: "17px",
                width: "17px",
                marginLeft: "8px",
                color: "#FE9D3A",
              }}
              onClick={() => {
                setIsAutoLogin(0);
              }}
            />
          ) : (
            <BsSquare
              color="#C6C5C5"
              style={{ height: "17px", width: "17px", marginLeft: "8px" }}
              onClick={() => {
                setIsAutoLogin(1);
              }}
            />
          )}
          &nbsp;
          <span style={{ fontSize: "13px", color: "#C6C5C5" }}>
            자동 로그인
          </span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          {isRememberId ? (
            <BsFillCheckSquareFill
              style={{
                height: "17px",
                width: "17px",
                marginLeft: "5px",
                color: "#FE9D3A",
              }}
              onClick={() => {
                setIsRememberId(0);
              }}
            />
          ) : (
            <BsSquare
              color="#C6C5C5"
              style={{ height: "17px", width: "17px", marginLeft: "5px" }}
              onClick={() => {
                setIsRememberId(1);
              }}
            />
          )}
          &nbsp;
          <span style={{ fontSize: "13px", color: "#C6C5C5" }}>
            아이디 저장
          </span>
        </div>
        <br />
        <br />
        <StyledButton disabled={isSubmitting} type="submit" width="18.8125rem">
          로그인
        </StyledButton>
      </form>
      <div>
        <p
          style={{ color: "#918C8C", fontWeight: 700 }}
          onClick={() => navigate("/signup")}
        >
          회원가입
        </p>
      </div>
    </div>
  );
};
