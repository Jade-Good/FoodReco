import React from "react";
import classes from "./Header.module.css";
import { MdOutlineGroupAdd } from "react-icons/md";
import { AiOutlineArrowLeft } from "react-icons/ai";
import { useNavigate } from "react-router-dom";

interface HeaderCrewProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
  count: number | null;
}

const HeaderCrewInviteBasic: React.FC<HeaderCrewProps> = ({
  onClick,
  count,
}) => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <div>
        <AiOutlineArrowLeft
          className={`${classes.leftContent} ${classes.backIcnBtn}`}
          onClick={() => navigate("/crew")}
        />
        <p>그룹원 초대</p>
      </div>

      <div>
        <p>{count}</p>
        <p onClick={onClick}>확인</p>
      </div>
    </div>
  );
};

export default HeaderCrewInviteBasic;
