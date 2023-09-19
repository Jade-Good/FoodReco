import React from 'react';
import StyledIdInputIcon from '../../components/inputs/StyledIdInputIcon';
import StyledPwInputIcon from '../../components/inputs/StyledPwInputIcon';
import StyledButton from '../../styles/StyledButton';

export const Login = () => {
  const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('로그인 axios');
  };

  return (
    <div>
      <div>
        <img
          src="images/foodreco.png"
          alt="sdsd"
          width={'200rem'}
          height={'150rem'}
        />
      </div>

      <form onSubmit={handleLogin}>
        <StyledIdInputIcon placeholder="이메일" />
        <StyledPwInputIcon
          style={{ borderTop: 'none' }}
          placeholder="비밀번호"
        />
        <div>
          <p>회원가입</p>
        </div>
        <StyledButton type="submit" width="18.8125rem" height="height: 5rem">
          등록
        </StyledButton>
      </form>
    </div>
  );
};
