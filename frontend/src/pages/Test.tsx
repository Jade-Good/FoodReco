import React from 'react';
import StyledBasicInput from '../components/inputs/StyledBasicInput';
import StyledButton from '../styles/StyledButton';
import { useNavigate } from 'react-router-dom';

export const Test = () => {
  const navigate = useNavigate();
  const testclick = () => {
    console.log('test입니다');
  };
  return (
    <div className="check">
      <StyledButton
        onClick={() =>
          (window.location.href =
            'https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://j9b102.p.ssafy.io/test&prompt=consent&response_type=code&client_id=195561660115-6gse0lsa1ggdm3t9jplps3sodm7e735n.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/fitness.activity.read https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/fitness.body.read https://www.googleapis.com/auth/fitness.nutrition.read https://www.googleapis.com/auth/fitness.sleep.read')
        }
      >
        구글인증
      </StyledButton>
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
