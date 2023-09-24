import React from "react";
import classes from "./foodButton.module.css";
import { ImBlocked } from "react-icons/im";
import { IoArrowRedoSharp } from "react-icons/io5";
import { MdFavorite } from "react-icons/md";
import { RiTimerFill } from "react-icons/ri";

export const FoodButton = () => {
  return (
    <div className={classes.buttons}>
      <div className={classes.circleTall}>
        <ImBlocked style={{ color: "#7D7B7B", fontSize: "2rem" }} />
      </div>
      <div className={classes.circleLarge}>
        <IoArrowRedoSharp style={{ color: "#4D7EFF", fontSize: "3.5rem" }} />
      </div>
      <div className={classes.circleLarge}>
        <MdFavorite style={{ color: "#FF4747", fontSize: "3.5rem" }} />
      </div>
      <div className={classes.circleTall}>
        <RiTimerFill style={{ color: "#C6C5C5", fontSize: "2rem" }} />
      </div>
    </div>
  );
};
