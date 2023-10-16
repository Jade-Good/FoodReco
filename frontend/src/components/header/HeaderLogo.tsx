import React from "react";
import { useNavigate } from "react-router-dom";
import classes from "./Header.module.css";

const HeaderLogo = () => {
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <img
        className={`${classes.leftContent} ${classes.logo}`}
        src="/images/foodreco.png"
        alt="logo"
        onClick={() => navigate("/")}
      />
    </div>
  );
};

export default HeaderLogo;
