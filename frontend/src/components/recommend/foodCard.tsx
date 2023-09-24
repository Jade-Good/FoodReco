import React from "react";
import classes from "./foodCard.module.css";

export const FoodCard = () => {
  return (
    <div className={classes.cardBox}>
      <img className={classes.foodImg} src="../images/짜장면.png" alt="foodImg" />
      <h1>짜장면</h1>
    </div>
  );
};
