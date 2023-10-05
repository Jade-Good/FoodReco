import React, { useState, useEffect } from "react";
import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrew from "../../components/header/HeaderCrew";
import CrewCard from "../../components/crewpage/CrewCard";
import api from "../../utils/axios";
import { useNavigate } from "react-router-dom";

export interface CrewProps {
  crewSeq: number;
  name: string;
  img: string;
  status: string;
  recentRecommend?: number;
  crewCnt?: number;
}

export const CrewList = () => {
  // const crewNames = [
  //   "떡볶이 원정대",
  //   "밀가루 사랑 모임",
  //   "한국대 점심팟",
  //   "은수 커플",
  //   "밀가루 사랑 모임",
  //   "떡볶이 원정대",
  //   "은수 커플",
  //   "한국대 점심팟",
  // ];
  const [crewList, setCrewList] = useState<CrewProps[]>([]);
  const navigate = useNavigate();
  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    getCrewList();
  }, []);

  const getCrewList = () => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/crew/list`)
      .then((res) => {
        console.log(res);
        setCrewList(res.data);
      })
      .catch((err) => {
        console.log("그룹 리스트 못가져옴:", err);
      });
  };

  return (
    <>
      <HeaderCrew onClick={() => navigate("/crew/make")} />
      <div
        style={{
          margin: "5.5rem 0",
          padding: "5vmin",
          display: "grid",
          gridTemplateColumns: "repeat(2, 1fr)",
          gridGap: "5vmin",
        }}
      >
        {crewList.map((crew, i) => {
          return <CrewCard crew={crew} key={i} />;
        })}
        <CrewCard key={-1} />
      </div>
      <FooterCrew />
    </>
  );
};
