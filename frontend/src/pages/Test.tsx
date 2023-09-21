import React from 'react';
import StyledBasicInput from '../components/inputs/StyledBasicInput';

export const Test = () => {
  const testclick = () => {
    console.log('test입니다');
  };
  return (
    <div className="check">
      <StyledBasicInput name="check" placeholder="이메일을 입력하세요" />
    </div>
  );
};

const check = {
  height: 'calc(var(--vh, 1vh) * 100',
};

// height: calc(var(--vh, 1vh) * 100);
// height: calc(var(--vh, 1vh) * 100 + [footer의 높이]);

// // 예: footer 높이가 66px인 경우
// height: calc(var(--vh, 1vh) * 100 + 66px);
