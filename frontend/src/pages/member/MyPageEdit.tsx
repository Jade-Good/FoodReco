import React, { useEffect, useState } from "react";

import StyledButton from "../../styles/StyledButton";
import classes from "./MyPageEdit.module.css";
import {
  SubmitHandler,
  useFieldArray,
  useForm,
  Controller,
} from "react-hook-form";

import StyledBasicInput from "../../components/inputs/StyledBasicInput";

import axios from "axios";
import HeaderLogo from "../../components/header/HeaderLogo";

import styled from "styled-components";
import BasicSelect from "../../components/option/BasicSelect";
import { useNavigate } from "react-router-dom";
import StyledBasicInputUnit from "../../components/inputs/StyledBasicInputUnit";
import { toast } from "react-toastify";
import { useRecoilState } from "recoil";
import { userState } from "../../recoil/atoms/userState";
import api from "../../utils/axios";

interface IForm {
  image: FileList | string | null;
  profileImg: string | File | null;
  nickname: string;
  height: string | number | undefined;
  weight: string | number | undefined;
  activity: string;
  time: number;
}

interface ExerciseRates {
  [exercise: string]: {
    [time: number]: number;
  };
}

export const MyPageEdit = () => {
  const navigate = useNavigate();

  const activityList = ["운동안함", "걷기", "헬스", "수영", "자전거"];
  const timeList = [0.5, 1, 1.5, 2, 2.5, 3];
  // 걷기 1시간 6750 , 헬스 1시간 14700, 수영: 12850, 자전거:11050
  const [imageURL, setImageURL] = useState<string | undefined>();

  const [height, setHeight] = useState("");
  const [weight, setWeight] = useState("");
  const [nickname, setNickname] = useState("");
  const [activity, setActivicty] = useState(0);
  const [profileURL, setProfileURL] = useState("");
  const [user, setUser] = useRecoilState(userState);

  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/mypage/info`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        // console.log(res);
        setHeight(res.data.memberDetailDto.height);
        setWeight(res.data.memberDetailDto.weight);
        setNickname(res.data.memberDetailDto.nickname);
        setActivicty(res.data.memberDetailDto.activity);
        // setProfileURL(res.data.memberDetailDto.profileUrl);
      })
      .catch((err) => {
        console.log(err);
        // console.log(err.response.status);
      });
  }, []);

  useEffect(() => {
    setValue("nickname", nickname);
    setValue("height", height);
    setValue("weight", weight);
    setValue("image", profileURL);
  }, [nickname, height, weight]);
  // let imageURL: string;

  // if (profileImageFile) {
  //   imageURL = URL.createObjectURL(profileImageFile);
  // } else {
  //   imageURL = '/image/foodreco.png';
  // }
  // const handleEditProfileImageClick = () => {
  //   console.log('Edit button clicked!');

  //   // Input element를 통해 사용자가 이미지 파일을 선택할 수 있게 합니다.
  //   fileInputRef.current?.click();
  // };

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
    운동안함: {
      0.5: 0,
      1: 0,
      1.5: 0,
      2: 0,
      2.5: 0,
      3: 0,
    },
  };

  const {
    register,
    formState: { errors, isSubmitting },
    handleSubmit,
    control,
    setValue,
    // getValues,
    watch,
  } = useForm<IForm>({
    mode: "onSubmit",
    defaultValues: {
      image: null,
      nickname: nickname,

      height: height,
      weight: weight,
      activity: "",
      time: undefined,
    },
  });

  const profileImage = watch("image");

  useEffect(() => {
    if (
      profileImage &&
      profileImage.length > 0 &&
      profileImage[0] instanceof File
    ) {
      const file = profileImage[0];
      setImageURL(URL.createObjectURL(file));
    } else {
      setImageURL("/image/foodreco.png"); // 기본 이미지 경로
    }
  }, [profileImage]);

  // const handleFileChange = () => {
  //   if (profileImage && profileImage[0]) {
  //     setProfileImageFile(profileImage[0]);
  //   }
  // };

  // 회원가입 로직
  const handleEdit: SubmitHandler<IForm> = (data) => {
    const formData = new FormData();

    const { image, nickname, height, weight, activity, time } = data;

    const walkingRate = exerciseRates[activity][time];
    let imgURL = null;
    let imgFile = null;
    if (image && image.length > 0 && image[0] instanceof File) {
      imgFile = image[0]; // FileList에서 첫 번째 파일을 가져옵니다.
      // formData.append("img", file); // 파일을 FormData에 추가합니다.
      imgURL = URL.createObjectURL(imgFile); // 이미지 URL을 생성합니다.
    }

    const datas = {
      img: imgFile,
      nickname: nickname,
      activity: walkingRate,
      weight: weight,
      height: height,
    };
    formData.append(
      "request",
      new Blob([JSON.stringify(datas)], { type: "application/json" })
    );
    console.log(datas);

    if (errors.nickname) {
      // console.log(errors);
      toast.error("정보를 다시 확인해주세요!", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
      });
    } else {
      // console.log("sdfasdf");
      api
        .patch(`${process.env.REACT_APP_BASE_URL}/mypage/info`, datas, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        })

        .then((res) => {
          console.log(res);
          navigate("/mypage");
          // alert("회원가입이 완료되었습니다!")
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  return (
    <div className={classes.container}>
      <br />
      <HeaderLogo />
      <br />
      <br />
      <br />

      <p style={{ color: "#525252", fontSize: "1.5rem", fontWeight: "bold" }}>
        프로필 수정하기
      </p>

      <br />

      <form onSubmit={handleSubmit(handleEdit)}>
        <div className={classes.containerNoHeight}>
          <div
            style={{
              width: "10vw",
              height: "10vh",
              borderRadius: "50%",
              // overflow: 'hidden',
            }}
          >
            {imageURL && (
              <img
                src={imageURL}
                alt="Profile Preview"
                style={{ width: "10vw", height: "10vh" }}
              />
            )}
          </div>

          <input
            id="picture"
            type="file"
            className="hidden"
            accept="image/*"
            {...register("image")}
            // onChange={handleFileChange}
          />
          <br />
          {/* 닉네임 적기 */}
          <div className={classes.mB}>
            <div className={classes.labelContainer}>
              <label htmlFor="nickname" className={classes.labelStyle}>
                닉네임
              </label>
              {errors.nickname && (
                <small role="alert" style={{ color: "red", fontSize: "10px" }}>
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
                  options={timeList.map(String)}
                />
              </div>
            </div>
          </div>
        </div>

        <br />

        <StyledButton
          type="submit"
          disabled={isSubmitting}
          width="40vw"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
          color="white"
          fontSize="1.25rem"
          background="#FE9D3A"
          radius="10px"
        >
          적용
        </StyledButton>
        <StyledButton
          type="button"
          disabled={isSubmitting}
          width="40vw"
          boxShadow="0px 4px 6px rgba(0, 0, 0, 0.1)"
          color="white"
          fontSize="1.25rem"
          background="#FE9D3A"
          radius="10px"
          onClick={() => navigate("/mypage")}
        >
          취소
        </StyledButton>
      </form>
    </div>
  );
};
