import React from 'react';
import classes from './Header.module.css';
import { FiArrowLeft } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';

interface HeaderCrewProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
}

const HeaderCrew: React.FC<HeaderCrewProps> = ({ onClick }) => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <div className={classes.leftContent}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <FiArrowLeft style={{ width: '30px', height: '30px' }} />
          <p style={{ width: '', fontWeight: 'bold' }}>그룹원</p>
        </div>
      </div>
    </div>
  );
};

export default HeaderCrew;
