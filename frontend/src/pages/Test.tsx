import React from 'react';
import { MemberFooterHome } from '../components/MemberFooter/MemberFooterHome';
import { MemberFooterFriend } from '../components/MemberFooter/MemberFooterFriend';
import { MemberFooterRecommendation } from '../components/MemberFooter/MemberFooterRecommendation';
import { MemberFooterCrew } from '../components/MemberFooter/MemberFooterCrew';
import { MemberFooterMypage } from '../components/MemberFooter/MemberFooterMypage';
import styled from 'styled-components';
import { SlideImage } from './../styles/Carousel';
import HeaderLogo from '../components/header/HeaderLogo';
import HeaderQuestion from '../components/header/HeaderQuestion';
import HeaderFriendAdd from '../components/header/HeaderFriendAdd';
import HeaderCrew from '../components/header/HeaderCrew';

export const Test = () => {
  const testclick = () => {
    console.log('test입니다');
  };
  return (
    <div className="check">
      <HeaderCrew />
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
