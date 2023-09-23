import React from 'react';
import { AiOutlineHome } from 'react-icons/ai';
import { BiGroup } from 'react-icons/bi';
import { BsPersonCircle } from 'react-icons/bs';
import { AiOutlineStar } from 'react-icons/ai';
import { BiWindows } from 'react-icons/bi';
import { Navigate, useNavigate } from 'react-router-dom';
import './Footer.css';

export const MemberFooterMypage = () => {
  const navigate = useNavigate();
  let memberId = 1;

  return (
    <div className="footer">
      <AiOutlineHome onClick={() => navigate('/')} style={iconStyle} />
      <BiGroup
        onClick={() => navigate(`/friend/${memberId}`)}
        style={iconStyle}
      />
      <div
        style={circleStyle}
        onClick={() => navigate(`/recommendation/${memberId}`)}
      >
        <AiOutlineStar style={starIconStyle} />
      </div>
      <BiWindows
        onClick={() => navigate(`/crew/${memberId}`)}
        style={iconStyle}
      />
      <BsPersonCircle
        onClick={() => navigate(`/mypage/${memberId}`)}
        style={{ color: '#FE9D3A', fontSize: '2em' }}
      />
    </div>
  );
};

// 스타일링
const footerStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: '10px',
  boxShadow: '0px -4px 6px rgba(0, 0, 0, 0.1)', // 위쪽에 그림자 효과를 줌 (y-offset을 음수로 설정)
};
const iconStyle = {
  cursor: 'pointer', // 아이콘에 커서 스타일 지정
  fontSize: '2em', // 아이콘 크기 조정
  color: '#C6C5C5',
};
const circleStyle = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  width: '3em',
  height: '3em',
  borderRadius: '50%', // 원 모양을 만듦
  backgroundColor: '#C6C5C5', // 배경색 지정
  boxShadow: '0px -2px 4px rgba(0, 0, 0, 0.2)', // 위쪽에 그림자 효과를 줌
};

const starIconStyle = {
  fontSize: '2.5em', // 아이콘 크기 조정
  color: 'white',
  cursor: 'pointer',
};
