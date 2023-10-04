import React from "react";
import styled from "styled-components";

export const BigDiv = styled.div`
  display: flex; /* Flex 컨테이너로 설정 */
  justify-content: center; /* 가로 방향 가운데 정렬 */
  align-items: center; /* 세로 방향 가운데 정렬 */
  border: 1px solid #fe9d3a;
  border-radius: 0.3125rem;
  width: 17.3125rem;
  height: 2rem;
  flex-shrink: 0;
`;

export const LeftDiv = styled.div`
  //   flex: 1; /* Flex 아이템 확장 */
  width: 5.875rem;
  height: 2rem;
  border-radius: 0.3125rem 0rem 0rem 0.3125rem;
  border: 1px solid #fe9d3a;
  background-color: #fe9d3a;
  color: white;
  display: flex; /* 가운데 정렬을 위한 Flex 컨테이너 */
  justify-content: center; /* 가로 방향 가운데 정렬 */
  align-items: center; /* 세로 방향 가운데 정렬 */
  font-size: 0.9375rem;

  font-weight: 700;
`;

export const RightDiv = styled.div`
  flex: 30%; /* Flex 아이템 확장 */
  height: 2rem;
  border-radius: 0.3125rem 0rem 0rem 0.3125rem;
  border: none;
  color: #fe9d3a;
  font-size: 0.9375rem;
  font-weight: 700;
  display: flex; /* 가운데 정렬을 위한 Flex 컨테이너 */
  justify-content: center; /* 가로 방향 가운데 정렬 */
  align-items: center; /* 세로 방향 가운데 정렬 */
`;

export type MemberInfoProps = {
  leftValue: string;
  rightValue: string | number;
  unit?: string;
};

export const MemberInfo: React.FC<MemberInfoProps> = ({
  leftValue,
  rightValue,
  unit,
}) => {
  return (
    <BigDiv>
      <LeftDiv>{leftValue}</LeftDiv>
      <RightDiv>{rightValue}</RightDiv>
      <span
        style={{
          color: "#fe9d3a",
          marginRight: "10px",
          fontWeight: "700",
          fontSize: "0.9375rem",
        }}
      >
        {unit}
      </span>
    </BigDiv>
  );
};
