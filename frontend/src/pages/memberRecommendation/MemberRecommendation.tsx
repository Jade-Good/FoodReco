import React from "react";
import { MemberFooterRecommendation } from "../../components/MemberFooter/MemberFooterRecommendation";
import HeaderLogo from "../../components/header/HeaderLogo";
import { FoodCard } from "../../components/recommend/foodCard";
import classes from "./MemberRecommendation.module.css";
import { FoodButton } from "../../components/recommend/foodButton";

export const MemberRecommendation = () => {
  return (
    <>
      <HeaderLogo />
      <br />
      <br />
      <br />
      <div className={classes.foodCard}>
        <FoodCard />
      </div>
      <br />
      <FoodButton />
      <br />
      <MemberFooterRecommendation />
    </>
  );
};
