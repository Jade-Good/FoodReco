import React from "react";
import { ImBlocked } from "react-icons/im";
import { IoArrowRedoSharp } from "react-icons/io5";
import { MdFavorite } from "react-icons/md";
import { RiTimerFill } from "react-icons/ri";
import styled from "styled-components";
import { FoodFeedBackProps } from "../../pages/memberRecommendation/MemberRecommendation";

export const FoodButton: React.FC<FoodFeedBackProps> = ({ foodFeedBack }) => {
  return (
    <Buttons>
      <CircleTall
        onClick={() => {
          foodFeedBack(0);
        }}
      >
        <ImBlocked style={{ color: "#7D7B7B", fontSize: "4vh" }} />
      </CircleTall>
      <CircleLarge
        onClick={() => {
          foodFeedBack(2);
        }}
      >
        <IoArrowRedoSharp style={{ color: "#4D7EFF", fontSize: "7vh" }} />
      </CircleLarge>
      <CircleLarge
        onClick={() => {
          foodFeedBack(4);
        }}
      >
        <MdFavorite style={{ color: "#FF4747", fontSize: "7vh" }} />
      </CircleLarge>
      <CircleTall
        onClick={() => {
          foodFeedBack(2);
        }}
      >
        <RiTimerFill style={{ color: "#C6C5C5", fontSize: "4vh" }} />
      </CircleTall>
    </Buttons>
  );
};

const CircleLarge = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 7vmax;
  height: 7vmax;
  border: #e7e7e7 0.5rem solid;
  border-radius: 100%;
  text-align: center;

  padding: 1.5vmax;
`;
const CircleTall = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;

  width: 4vmax;
  height: 4vmax;
  border: #e7e7e7 0.5rem solid;
  border-radius: 100%;
  /* line-height: 4.2vmax; */

  padding: 1vmax;
`;
const Buttons = styled.div`
  display: flex;
  justify-content: center;
  padding: 0 10vw;

  position: fixed;
  left: 0;
  right: 0;
  bottom: 15vh;
`;
