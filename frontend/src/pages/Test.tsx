import React from 'react';
import { MemberFooterHome } from '../components/MemberFooter/MemberFooterHome';
import { MemberFooterFriend } from '../components/MemberFooter/MemberFooterFriend';
import { MemberFooterRecommendation } from '../components/MemberFooter/MemberFooterRecommendation';
import { MemberFooterCrew } from '../components/MemberFooter/MemberFooterCrew';
import { MemberFooterMypage } from '../components/MemberFooter/MemberFooterMypage';
import styled from 'styled-components';
import { StyledBasicInput } from '../styles/StyledInputs';
import { SlideImage } from './../styles/Carousel';
import StyledBasicInputWithIcon from '../styles/StyledIdInput';
import StyledIdInput from '../styles/StyledIdInput';
import StyledPwInput from '../styles/StyledPwInput';

export const Test = () => {
  return (
    <div>
      <p>테스트 페이지</p>
      <StyledBasicInput placeholder="아이디" />
      <MemberFooterHome />
      <MemberFooterFriend />
      <MemberFooterRecommendation />
      <MemberFooterCrew />
      <MemberFooterMypage />
      <StyledIdInput placeholder="아이디" />
      <StyledPwInput style={{ borderTop: 'none' }} placeholder="비밀번호" />
      <img src="images/foodreco.png" alt="sdsd" width={'100rem'} />
    </div>
  );
};

const check = {
  height: 'calc(var(--vh, 1vh) * 100 + 66px)',
};
// height: calc(var(--vh, 1vh) * 100 + [footer의 높이]);

// // 예: footer 높이가 66px인 경우
// height: calc(var(--vh, 1vh) * 100 + 66px);
