import React from "react";
import styled from "styled-components";

// styled-components를 사용하여 스타일링된 프로그래스 바 컴포넌트 생성
const ProgressBarWrapper = styled.div`
  width: 100%;
  height: 0.8rem;
  background-color: #ccc;
  margin-top: 0.5rem;
  /* border-radius: 4px; */
`;

const ProgressBarFill = styled.div<{ percentage?: number }>`
  width: ${(props) => props.percentage}%;
  height: 100%;
  background-color: #fe9d3a;
  /* border-radius: 1rem; */
  transition: width 0.3s ease-in-out;
`;

interface ProgressBarProps {
  percentage?: number;
}

const ProgressBar: React.FC<ProgressBarProps> = ({ percentage }) => {
  return (
    <ProgressBarWrapper>
      <ProgressBarFill percentage={percentage} />
    </ProgressBarWrapper>
  );
};

export default ProgressBar;
