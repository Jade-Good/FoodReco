import React from "react";
import classes from "./Header.module.css";
import { MdOutlineGroupAdd } from "react-icons/md";
import { useNavigate } from "react-router-dom";

interface HeaderCrewProps {
  onClick?: () => void; // handleClick 함수의 타입 정의
}

const HeaderCrew: React.FC<HeaderCrewProps> = ({ onClick }) => {
  const navigate = useNavigate();

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
          // onClick={null}
        />
      </div>
    </div>
  );
};

export default HeaderCrew;
