import React from "react";
import classes from "./Header.module.css";
import { BiHelpCircle } from "react-icons/bi";
import { useNavigate } from "react-router-dom";

interface HeaderQuestionProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
}

const HeaderQuestion: React.FC<HeaderQuestionProps> = ({ onClick }) => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <img
        className={`${classes.leftContent} ${classes.logo}`}
        src="/images/foodreco.png"
        alt="logo"
        onClick={() => navigate("/")}
      />
      <BiHelpCircle
        className={`${classes.rightContent} ${classes.helpBtn}`}
        // style={{ width: "25px", height: "25px", marginRight: "12px" }}
        onClick={onClick}
      />
    </div>
  );
};

const iconStyle = {
  width: "25px", // 단위를 추가합니다.
  height: "25px",
  marginRight: "12px",
};

export default HeaderQuestion;
