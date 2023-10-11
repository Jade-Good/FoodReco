import React, { useState } from "react";
import styled, { css } from "styled-components";
import { useRecoilState } from "recoil";
import { foodDetailModal } from "../../recoil/atoms/modalState";

import { FoodFeedBackProps } from "../../pages/memberRecommendation/MemberRecommendation";

interface FoodCardInfo extends FoodFeedBackProps {
  foodImg: string;
  foodName: string;
}

export const FoodCard: React.FC<FoodCardInfo> = ({
  foodImg,
  foodName,
  foodFeedBack,
}) => {
  // 드래그 상태, 왼쪽|오른쪽|중앙 상태, x 좌표
  const [isDragging, setIsDragging] = useState(false);
  const [swipeDirection, setSwipeDirection] = useState<"left" | "right" | "">(
    ""
  );
  const [posX, setPosX] = useState(window.innerWidth / 2);
  const [startX, setStartX] = useState(0);

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
    setStartX(e.touches[0].clientX);
  };

  // 터치 드래그
  const handleTouchMove = (e: React.TouchEvent<HTMLDivElement>) => {
    if (!isDragging) return;

    const nowX = e.touches[0].clientX - startX;
    setPosX(nowX);

    // x좌표 : 최대 최소 판단, 상태 판단
    setPosX(Math.min(maxX, Math.max(minX, nowX)));
    setSwipeDirection(nowX > 0 ? "right" : "left");
  };

  // 터치 종료
  const handleTouchEnd = () => {
    setIsDragging(false);
    const card = document.querySelector("#card");
    if (card) {
      card.classList.remove("right");
      card.classList.remove("left");
    }
    setSwipeDirection("");

    // 카드 스와이프 판단
    if (posX >= lineX) {
      console.log("like!!");
      foodFeedBack(4);
    } else if (posX <= -lineX) {
      console.log("pass!!");
      foodFeedBack(2);
    }

    // x 좌표 이동 값 초기화
    setPosX(0);
  };

  // 회전도 계산
  const rotate = Math.min(
    Math.max((posX / (window.innerWidth / 2)) * maxRotate, -maxRotate),
    maxRotate
  );
  // 투명도 계산
  const opacity = Math.min(
    Math.max(
      minOpacity + (maxOpacity - minOpacity) * (1 - Math.abs(posX / maxX)),
      minOpacity
    ),
    maxOpacity
  );

  // 모달
  const [modalOpen, setModalOpen] = useRecoilState(foodDetailModal);
  const detail = () => {
    setModalOpen({ modalOpen: true });
    foodFeedBack(3);
  };

  return (
    <div style={{ display: "flex", justifyContent: "center" }}>
      <FoodCardBox
        id="card"
        posX={posX} // x좌표 위치
        swipeDirection={swipeDirection} // x좌표 상태(중앙, 좌, 우)
        rotate={rotate} // 회전도
        opacity={opacity} // 투명도
        onTouchStart={handleTouchStart} // 터치 시작
        onTouchMove={handleTouchMove} // 터치 드래그
        onTouchEnd={handleTouchEnd} // 터치 종료
        onClick={detail}
      >
        <FoodImg src={`${foodImg}`} />
        {/* <img className={classes.foodImg} src="../images/짜장면.png" alt="foodImg" /> */}
        <h1 style={{ margin: "0", fontSize: "4vmax" }}>{foodName}</h1>
      </FoodCardBox>
    </div>
  );
};
const FoodImg = styled.img<{ src: string }>`
  width: 80vmin;
  height: 80vmin;

  max-width: 40vh;
  max-height: 40vh;

  border-radius: 100%;
  margin-bottom: 2vmax;

  border: solid orange 1px;
`;

const FoodCardBox = styled.div<{
  swipeDirection: string;
  posX: number;
  rotate: number;
  opacity: number;
}>`
  /* layout */
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  /* style */
  width: 90vw;
  height: 100vmin;
  max-height: 50vh;

  padding-top: 4vmin;
  padding-bottom: 2vmax;
  margin-bottom: 5vmax;

  border-radius: 1rem;
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.25);

  /* animation */
  user-select: none;
  cursor: grab;

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
