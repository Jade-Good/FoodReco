import React, { useState } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/foodCard";
import { FoodButton } from "../../components/recommend/foodButton";
import styled, { css } from "styled-components";

export const MemberRecommendation = () => {
  // 드래그 상태, 왼쪽|오른쪽|중앙 상태, x 좌표
  const [isDragging, setIsDragging] = useState(false);
  const [swipeDirection, setSwipeDirection] = useState<"left" | "right" | "">("");
  const [posX, setPosX] = useState(window.innerWidth / 2);

  // X 좌표, 투명도, 회전도의 최대/최소 값
  const maxX = window.innerWidth / 2;
  const minX = -maxX;
  const minOpacity = 0.3;
  const maxOpacity = 1;
  const maxRotate = 20;

  // 카드 이동 임계값
  const lineX = window.innerWidth * 0.3;

  // 터치 시작
  const handleTouchStart = (e: React.TouchEvent<HTMLDivElement>) => {
    setIsDragging(true);
    // setSwipeDirection("");
    // console.log("터치 시작");
  };

  // 터치 드래그
  const handleTouchMove = (e: React.TouchEvent<HTMLDivElement>) => {
    if (!isDragging) return;
    const nowX = e.touches[0].clientX - maxX;
    setPosX(nowX);

    // x좌표 : 최대 최소 판단, 상태 판단
    setPosX(Math.min(maxX, Math.max(minX, nowX)));
    setSwipeDirection(nowX > 0 ? "right" : "left");
  };

  // 터치 종료
  const handleTouchEnd = () => {
    setIsDragging(false);
    // console.log("터치 종료", swipeDirection);
    const card = document.querySelector("#card");
    if (card) {
      card.classList.remove("right");
      card.classList.remove("left");
    }
    setSwipeDirection("");

    // 카드 스와이프 판단
    if (posX >= lineX) {
      console.log("like!!");
    } else if (posX <= -lineX) {
      console.log("pass!!");
    }
  };

  // 회전도 계산
  const rotate = Math.min(
    Math.max((posX / (window.innerWidth / 2)) * maxRotate, -maxRotate),
    maxRotate
  );
  // 투명도 계산
  const opacity = Math.min(
    Math.max(minOpacity + (maxOpacity - minOpacity) * (1 - Math.abs(posX / maxX)), minOpacity),
    maxOpacity
  );

  return (
    <div style={{ overflow: "hidden" }}>
      <HeaderQuestion />
      <FoodCardBox
        id="card"
        posX={posX} // x좌표 위치
        swipeDirection={swipeDirection} // x좌표 상태(중앙, 좌, 우)
        rotate={rotate} // 회전도
        opacity={opacity} // 투명도
        onTouchStart={handleTouchStart} // 터치 시작
        onTouchMove={handleTouchMove} // 터치 드래그
        onTouchEnd={handleTouchEnd} // 터치 종료
      >
        <FoodCard />
      </FoodCardBox>
      <FoodButton />
      <FooterRecommendation />
    </div>
  );
};

// FoodCardBox 컴포넌트 내부
const FoodCardBox = styled.div<{
  swipeDirection: string;
  posX: number;
  rotate: number;
  opacity: number;
}>`
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 5vh;
  overflow: auto;

  // right
  ${(props) =>
    props.swipeDirection === "right" &&
    css`
      transform: translateX(${props.posX}px) rotate(${props.rotate}deg);
      opacity: ${props.opacity};
    `}

  // left
  ${(props) =>
    props.swipeDirection === "left" &&
    css`
      transform: translateX(${props.posX}px) rotate(${props.rotate}deg);
      opacity: ${props.opacity};
    `}

  // 중앙으로 돌아올 때 transition 제거하고 초기값으로 돌림
  ${(props) =>
    props.swipeDirection === "" &&
    css`
      transition: none;
      transform: translateX(0) rotate(0deg);
      opacity: 1;
    `}
`;
