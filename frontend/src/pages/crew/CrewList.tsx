import React from "react";
import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrew from "../../components/header/HeaderCrew";
import CrewCard from "../../components/crewpage/CrewCard";

export const CrewList = () => {
  const crewNames = [
    "떡볶이 원정대",
    "밀가루 사랑 모임",
    "한국대 점심팟",
    "은수 커플",
    "밀가루 사랑 모임",
    "떡볶이 원정대",
    "은수 커플",
    "한국대 점심팟",
  ];

  return (
    <>
      <HeaderCrew />
      <div
        style={{
          margin: "5.5rem 0",
          padding: "5vmin",
          display: "grid",
          gridTemplateColumns: "repeat(2, 1fr)",
          gridGap: "5vmin",
        }}
      >
        {crewNames.map((name, i) => {
          return <CrewCard name={name} key={i} />;
        })}
        <CrewCard key={-1} />
      </div>
      <FooterCrew />
    </>
  );
};
