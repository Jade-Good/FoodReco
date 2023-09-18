import React from 'react';
import { AiOutlineHome } from 'react-icons/ai';
import { BiGroup } from 'react-icons/bi';
import { BsPersonCircle } from 'react-icons/bs';
import { AiOutlineStar } from 'react-icons/ai';
import { BiWindows } from 'react-icons/bi';
import { Navigate, useNavigate } from 'react-router-dom';

export const MemberFooter = () => {
  const navigate = useNavigate(); // 수정된 부분

  let memberId = 1; // api로 memberId get해오기 or redux store있는 member값 가져오기

  return (
    <>
      <AiOutlineHome onClick={() => navigate('/')} />
      <BiGroup onClick={() => navigate(`/friend/${memberId}`)} />
      <AiOutlineStar onClick={() => navigate(`/recommendation/${memberId}`)} />
      <BiWindows onClick={() => navigate(`/crew/${memberId}`)} />
      <BsPersonCircle onClick={() => navigate(`/mypage/${memberId}`)} />
    </>
  );
};
