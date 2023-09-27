import React, { useState, useEffect } from "react";
import styled from "styled-components";
import ToggleButton from "@mui/material/ToggleButton";

const TagDiv = styled.div`
  display: flex;
  background-color: white;
  border: 2px solid #fe9d3a;
  width: 5rem;
  height: 2rem;
  justify-content: center;
  align-items: center;
  border-radius: 1rem;
`;

interface ChooseLikeFoodProps {
  foodList: string[];
}

export const ChooseLikeFood: React.FC<ChooseLikeFoodProps> = ({ foodList }) => {
  const [likefood, setLikeFood] = useState<string[]>([]);
  const toggleLikeFood = (food: string) => {
    if (likefood.includes(food)) {
      // 음식이 이미 좋아요 목록에 있는 경우, 제거합니다.
      setLikeFood(likefood.filter((item) => item !== food));
      console.log(likefood);
    } else {
      // 음식이 좋아요 목록에 없는 경우, 추가합니다.
      setLikeFood([...likefood, food]);
    }
  };

  useEffect(() => {
    // useEffect 내에서 likefood 배열이 변경될 때마다 콘솔에 로그를 출력합니다.
    console.log("likefood 변경됨:", likefood);
  }, [likefood]); // likefood 배열이 변경될 때만 useEffect가 실행됩니다.

  return (
    <>
      {foodList.map((food, index) => (
        <ToggleButton
          value="check"
          selected={likefood.includes(food)} // 버튼 선택 상태는 좋아요 목록에 음식이 있는지 여부에 따라 결정됩니다.
          onClick={() => toggleLikeFood(food)} // 버튼을 클릭할 때 toggleFood 함수를 호출하여 음식을 추가하거나 제거합니다.
          key={index}
        >
          {food}
        </ToggleButton>
      ))}
    </>
  );
};
