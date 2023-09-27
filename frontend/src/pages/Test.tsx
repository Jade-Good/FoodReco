import React from "react";
import StyledBasicInput from "../components/inputs/StyledBasicInput";
import StyledButton from "../styles/StyledButton";
import { useNavigate } from "react-router-dom";
import BasicSelect from "../components/option/BasicSelect";
import StyledFoodSelect from "../components/option/StyledFoodSelect";
import { useFieldArray, useForm } from "react-hook-form";
import { ChooseLikeFood } from "./singup/ChooseLikeFood";
import { ChooseUnlikeFood } from "./singup/ChooseUnlikeFood";
import { MemberInfo } from "../components/membercomponents/MemberInfo";

export const Test = () => {
  const foodList = ["aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg"];
  const { control, register } = useForm();

  const { fields, append, prepend, remove, swap, move, insert } = useFieldArray(
    {
      control, // FormContext를 사용한다면 생략가능
      name: "test", // 배열 필드의 이름을 입력
      // keyName: "id", 기본값은 "id" 이고 변경 가능하다
    }
  );
  return (
    <div className="check">
      <StyledButton
        onClick={() =>
          (window.location.href =
            "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://j9b102.p.ssafy.io/test&prompt=consent&response_type=code&client_id=195561660115-6gse0lsa1ggdm3t9jplps3sodm7e735n.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/fitness.activity.read https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/fitness.body.read https://www.googleapis.com/auth/fitness.nutrition.read https://www.googleapis.com/auth/fitness.sleep.read")
        }
      >
        구글인증
      </StyledButton>
      <br />
      <br />
      <MemberInfo leftValue="키" rightValue="190" unit="CM" />
    </div>
  );
};

const check = {
  height: "calc(var(--vh, 1vh) * 100",
};

// height: calc(var(--vh, 1vh) * 100);
// height: calc(var(--vh, 1vh) * 100 + [footer의 높이]);

// // 예: footer 높이가 66px인 경우
// height: calc(var(--vh, 1vh) * 100 + 66px);
