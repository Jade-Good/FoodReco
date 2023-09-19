import React from 'react';
import { StyledBasicInput } from '../../styles/StyledInputs';
import StyledIdInput from '../../styles/StyledIdInput';
import StyledPwInput from '../../styles/StyledPwInput';

export const Login = () => {
  return (
    <div>
      <StyledIdInput placeholder="아이디" />
      <StyledPwInput style={{ borderTop: 'none' }} placeholder="비밀번호" />
    </div>
  );
};
