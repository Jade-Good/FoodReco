import React from "react";
import classes from "./Header.module.css";
import { MdOutlineGroupAdd } from "react-icons/md";
import { AiOutlineArrowLeft } from "react-icons/ai";
import { useNavigate } from "react-router-dom";

interface HeaderCrewProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
}

const HeaderCrewDetail: React.FC<HeaderCrewProps> = ({ onClick }) => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <AiOutlineArrowLeft
        className={`${classes.leftContent} ${classes.backIcnBtn}`}
        onClick={() => navigate("/crew")}
      />
      <img
        className={` ${classes.logo}`}
        src="/images/foodreco.png"
        alt="logo"
        onClick={() => navigate("/")}
      />
      <MdOutlineGroupAdd
        className={`${classes.rightContent} ${classes.friendAddBtn}`}
        onClick={onClick}
      />
    </div>
  );
};

export default HeaderCrewDetail;
