import React from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/foodCard";
import classes from "./MemberRecommendation.module.css";
import { FoodButton } from "../../components/recommend/foodButton";

export const MemberRecommendation = () => {
  return (
    <>
      <HeaderQuestion />
      <div className={classes.foodCard}>
        <FoodCard />
      </div>
      <FoodButton />
      <FooterRecommendation />
    </>
  );
};
