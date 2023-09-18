import React from 'react';
import { MemberFooterHome } from '../../components/MemberFooter/MemberFooterHome';
import { MemberFooterFriend } from '../../components/MemberFooter/MemberFooterFriend';
import { MemberRecommendation } from '../memberRecommendation/MemberRecommendation';
import { MemberFooterRecommendation } from '../../components/MemberFooter/MemberFooterRecommendation';
import { MemberFooterCrew } from '../../components/MemberFooter/MemberFooterCrew';
import { MemberFooterMypage } from '../../components/MemberFooter/MemberFooterMypage';

export const Main = () => {
  return (
    <div>
      <p>d아아아</p>
      <MemberFooterHome />
      <MemberFooterFriend />
      <MemberFooterRecommendation />
      <MemberFooterCrew />
      <MemberFooterMypage />
    </div>
  );
};
