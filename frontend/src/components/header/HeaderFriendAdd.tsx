import React from 'react';
import classes from './Header.module.css';
import { MdOutlineGroupAdd } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';

interface HeaderFriendAddProps {
  onClick: () => void; // handleClick 함수의 타입 정의
}

const HeaderFriendAdd: React.FC<HeaderFriendAddProps> = ({ onClick }) => {
  const navigate = useNavigate();
  return (
    <div className={classes.header}>
      <div className={classes.leftContent}>
        <img
          src="images/foodreco.png"
          alt="sds"
          width={'100rem'} // 단위를 제거하고 숫자만 넣습니다.
          height={'50rem'}
          onClick={() => navigate('/')}
        />
      </div>
      <div>
        <MdOutlineGroupAdd
          style={{ width: '25px', height: '25px', marginRight: '12px' }}
          onClick={onClick}
        />
      </div>
    </div>
  );
};

const iconStyle = {
  width: '40px', // 단위를 추가합니다.
  height: '40px',
  marginRight: '12px',
};
export default HeaderFriendAdd;
