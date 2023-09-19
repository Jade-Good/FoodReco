import React from 'react';
import { MemberFooterHome } from '../components/MemberFooter/MemberFooterHome';
import { MemberFooterFriend } from '../components/MemberFooter/MemberFooterFriend';
import { MemberFooterRecommendation } from '../components/MemberFooter/MemberFooterRecommendation';
import { MemberFooterCrew } from '../components/MemberFooter/MemberFooterCrew';
import { MemberFooterMypage } from '../components/MemberFooter/MemberFooterMypage';

export const Test = () => {
  return (
    <div>
      <p>테스트 페이지</p>
      <MemberFooterHome />
      <MemberFooterFriend />
      <MemberFooterRecommendation />
      <MemberFooterCrew />
      <MemberFooterMypage />
    </div>
  );
};
