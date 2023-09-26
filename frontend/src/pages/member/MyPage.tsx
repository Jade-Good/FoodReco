import React from 'react';
import { MemberFooterMypage } from '../../components/MemberFooter/MemberFooterMypage';
import { MemberInfo } from '../../components/membercomponents/MemberInfo';
import HeaderLogo from '../../components/header/HeaderLogo';
import classes from './MyPage.module.css';
import StyledButton from '../../styles/StyledButton';

export const MyPage = () => {
  return (
    <>
      <HeaderLogo />
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          flexDirection: 'column',
          marginTop: '10rem',
        }}
      >
        <div
          style={{
            borderRadius: '50%',
            overflow: 'hidden',
            width: '15rem',
            height: '15rem',
            marginBottom: '2rem',
          }}
        >
          <img
            src="/images/이민정.jpg"
            alt="sds"
            style={{ width: '15rem', height: '15rem' }}
          />
        </div>
        <StyledButton>프로필수정</StyledButton>
        <br />
        <MemberInfo leftValue="키" rightValue="190" unit="CM" />
        <MemberInfo leftValue="키" rightValue="190" unit="CM" />
        <MemberInfo leftValue="키" rightValue="190" unit="CM" />

        <MemberFooterMypage />
      </div>
    </>
  );
};
