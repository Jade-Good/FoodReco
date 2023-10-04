import React, { useState, useEffect } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/FoodCard";
import { FoodButton } from "../../components/recommend/FoodButton";
import FoodDetail from "../../components/recommend/FoodDetail";
import api from "../../utils/axios";

export interface FoodList {
  recommendedFoodSeq: number;
  recommendedFoodName: string;
  cookingMethod: string;
  type: string;
  category: string;
  img: string;
  ingredientSimilarity: number;
  foodNameSimilarity: number;
  originalFoodName: string;
}

export const MemberRecommendation = () => {
  const [foodList, setFoodList] = useState<FoodList[]>([
    {
      recommendedFoodSeq: 3899,
      recommendedFoodName: "짜장면",
      cookingMethod: "굽기",
      type: "메인반찬",
      category: "중화요리",
      img: "../images/짜장면.png",
      ingredientSimilarity: 0.776180681744197,
      foodNameSimilarity: 0.15384615384615385,
      originalFoodName: "짬뽕",
    },
  ]);

  let foodIdx = 0;

  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    if ("geolocation" in navigator) {
      navigator.permissions
        .query({ name: "geolocation" })
        .then(function (permissionStatus) {
          if (permissionStatus.state === "granted") {
            // 위치 정보 권한이 이미 부여된 경우
            // 여기에 위치 정보를 요청하는 코드를 작성하세요.
          } else if (permissionStatus.state === "prompt") {
            // 위치 정보 권한을 사용자에게 요청
            navigator.geolocation.getCurrentPosition(
              function (position) {
                // 위치 정보를 사용하여 원하는 작업 수행
              },
              function (error) {
                console.error("Error getting geolocation:", error);
              }
            );
          } else {
            // 위치 정보 권한이 거부된 경우 또는 다른 상태인 경우
            console.log(
              "Geolocation permission is denied or in another state."
            );
          }
        });
    } else {
      // Geolocation API를 지원하지 않는 경우
      console.log("Geolocation is not supported in this browser.");
    }
  }, []);

  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    foodRecommend();
  }, []);

  const foodRecommend = () => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/recommend/personal`)
      .then((res) => {
        console.log(res);
        setFoodList(res.data);
      })
      .catch((err) => {
        console.log("추천 메뉴 못가져옴:", err);
      });
  };

  return (
    <div style={{ overflow: "hidden", paddingTop: "24vmin" }}>
      <HeaderQuestion />

      <FoodCard
        foodImg={foodList[foodIdx].img}
        foodName={foodList[foodIdx].recommendedFoodName}
      />

      <FoodDetail foodName={foodList[foodIdx].recommendedFoodName} />

      <FoodButton />

      <FooterRecommendation />
    </div>
  );
};
