import React from 'react';
import { useNavigate } from 'react-router-dom';
import classes from './Header.module.css';

const HeaderLogo = () => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <img
        className={classes.leftContent}
        src="/images/foodreco.png"
        alt="sds"
        width={'100rem'} // 단위를 제거하고 숫자만 넣습니다.
        height={'50rem'}
        onClick={() => navigate('/')}
      />
    </div>
  );
};

export default HeaderLogo;
