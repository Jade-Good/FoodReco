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

  const exitButtonClick = () => {
    exitModal(true);
    document.body.style.overflow = "hidden";
  };

  return (
    <div className={classes.header}>
      <div className={classes.leftContent}>
        <img
          src="/images/foodreco.png"
          alt="sds"
          width={"90rem"} // 단위를 제거하고 숫자만 넣습니다.
          height={"50rem"}
          onClick={() => navigate("/")}
        />
      </div>
      <div>
        <MdOutlineGroupAdd
          style={{
            color: "#525252",
            fontSize: "2.5rem",
            marginTop: "1rem",
            marginRight: "0.8rem",
          }}
          onClick={exitButtonClick}
        />
      </div>
    </div>
  );
};

const iconStyle = {
  width: "40px", // 단위를 추가합니다.
  height: "40px",
  marginRight: "12px",
};
export default HeaderFriendAdd;
