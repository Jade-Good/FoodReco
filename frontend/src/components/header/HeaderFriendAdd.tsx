import React from "react";
import classes from "./Header.module.css";
import { MdOutlineGroupAdd } from "react-icons/md";
import { useNavigate } from "react-router-dom";

interface HeaderFriendAddProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
  exitModal: (newState: boolean) => void;
}

const HeaderFriendAdd = ({ onClick, exitModal }: HeaderFriendAddProps) => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <img
        className={`${classes.leftContent} ${classes.logo}`}
        src="/images/foodreco.png"
        alt="logo"
        onClick={() => navigate("/")}
      />
      <MdOutlineGroupAdd
        className={`${classes.rightContent} ${classes.friendAddBtn}`}
        onClick={() => exitModal(true)}
      />
    </div>
  );
};

export default HeaderFriendAdd;
