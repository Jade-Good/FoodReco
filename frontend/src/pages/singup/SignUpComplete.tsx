import React from 'react';
import StyledButton from '../../styles/StyledButton';
import { useNavigate } from 'react-router-dom';

export const SignUpComplete = () => {
  const navigate = useNavigate();
  return (
    <div>
      <div>
        <img
          src="/images/foodreco.png"
          alt="dsf"
          style={{ width: '18.8125rem', height: '9.9375rem' }}
        />

        <div>
          <p>회원가입 완료</p>
          <p>
            푸드레코에 오신것을 환영합니다. 자신에게 맞는 메뉴를 추천받아보세요
          </p>
        </div>
        <StyledButton
          width="18.8125rem"
          height="2.8125rem"
          onClick={() => navigate('/login')}
        >
          로그인
        </StyledButton>
      </div>
    </div>
  );
};
