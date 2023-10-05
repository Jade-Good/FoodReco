import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styled, { css } from "styled-components";
import api from "../../utils/axios";

import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewDetail from "../../components/header/HeaderCrewDetail";

import CrewMemberProfile from "../../components/crewpage/CrewMemberProfile";

interface CrewDetailProps {
  memberSeq: number;
  crewSeq: number;
  crewName: string;
  crewImg: string;
  crewStatus: "투표전" | "분석중" | "투표중";
  memberStatus: "미응답" | "수락"; // 본인의 그룹 가입 여부
  crewMembers: crewMembers[];

  // 투표 중인경우만 존재(아닌경우 null)
  voteRecommend: {
    crewRecommendSeq: number;
    crewRecommendTime: Date;
    foodList: foodList[];
  } | null;

  //분석 중인 경우 []
  histories: history[];
}

interface foodList {
  foodSeq: number;
  foodName: string;
  foodImg: string;
  foodVoteCount: number;
}

interface crewMembers {
  memberSeq: number;
  memberName: string;
  memberImg: string;
  memberStatus: "미응답" | "수락"; // 해당 사용자의 가입 여부
}

interface history {
  crewRecommendSeq: number;
  crewRecommendTime: Date;
  foodList: foodList[];
}

export const CrewDetail = () => {
  const [crewDetailInfo, setCrewDetailInfo] = useState<CrewDetailProps | null>(
    null
  );
  const [btnState, setBtnState] = useState("메뉴 투표를 시작하세요!");
  const { crewSeq } = useParams();

  let eventSource: EventSource;

  useEffect(() => {
    async function getCrewInfo() {
      try {
        if (eventSource) eventSource.close();

        const crewInfoResponse = await api.get(
          `${process.env.REACT_APP_BASE_URL}/crew/detail/${crewSeq}`
        );
        const crewInfoData = crewInfoResponse.data;
        setCrewDetailInfo(crewInfoData);

        if (crewDetailInfo?.crewStatus) setBtnState(crewDetailInfo.crewStatus);

        console.log("크루 상세 정보 조회 완료:", crewInfoData);

        if (crewInfoData?.memberSeq) {
          connectSSE(crewInfoData.memberSeq);
          eventSource.addEventListener("connect", (e) => {
            console.log("connect");
          });
          eventSource.addEventListener("start", (e) => {
            console.log("start", e);
            setBtnState(">> 메뉴 투표중<<");
          });
          eventSource.addEventListener("end", (e) => {
            console.log("end", e);
            setBtnState("메뉴 투표를 시작하세요!");
          });

          eventSource.addEventListener("vote", (e) => {
            console.log("vote", JSON.parse(e.data));
            if (crewDetailInfo) {
              let copy = { ...crewDetailInfo };

              copy.voteRecommend = JSON.parse(e.data);

              setCrewDetailInfo(copy);
            }
          });

          eventSource.onerror = (error) => {
            console.error("SSE Error:", error);
            console.error("투표 SSE 종료");
            eventSource.close();

            setTimeout(() => {
              console.log("연결 재시도");
              getCrewInfo();
              // connectSSE(crewInfoData.memberSeq);
            }, 3000); // 5초 후 재시도
          };
        } else {
          console.log("회원 번호 없음 - SSE 연결 취소");
        }
      } catch (err) {
        console.error("그룹 상세 정보 못가져옴:", err);
      }
    }

    getCrewInfo();

    return () => {
      console.log("투표 SSE 종료");
      eventSource.close();
    };
  }, []);

  const connectSSE = (memberSeq: number) => {
    console.log("투표 SSE 연결 시도! / 멤버 시퀀스 : ", memberSeq);

    eventSource = new EventSource(
      `${process.env.REACT_APP_BASE_URL}/crew/sse/${crewSeq}/${memberSeq}`
      // `http://192.168.31.202:8080/api/crew/sse/${crewSeq}/${memberSeq}`
    );
  };

  return (
    <>
      <HeaderCrewDetail />
      <CrewFrame>
        {/* 크루 이미지와 이름 */}
        <div style={{ textAlign: "center" }}>
          <CrewImg
            src={
              `${crewDetailInfo?.crewImg}`
                ? `${crewDetailInfo?.crewImg}`
                : "/favicon.ico"
            }
          />
          <h1 style={{ margin: "0", fontSize: "4vmax" }}>
            {crewDetailInfo?.crewName}
          </h1>
        </div>

        {/* 그룹원 목록 */}
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 2vmin 0", fontSize: "1.3rem" }}>그룹원</h1>
          <CrewMemberList>
            {crewDetailInfo?.crewMembers.map((member, key) => {
              return (
                <CrewMemberProfile
                  name={member.memberName}
                  profileImg={member.memberImg}
                  memberStatus={member.memberStatus}
                  key={key}
                />
              );
            })}
          </CrewMemberList>
        </div>

        {/* 메뉴 투표 창 */}
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 5vmin 0", fontSize: "1.3rem" }}>
            메뉴투표
          </h1>
          <div style={{ textAlign: "center" }}>
            <VoteStartBtn>{btnState}</VoteStartBtn>
          </div>
        </div>

        {/* 투표 기록 */}
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 5vmin 0", fontSize: "1.3rem" }}>
            투표 기록
          </h1>
          <div>메뉴 버튼이나 투표 리스트~</div>
          <div>메뉴 버튼이나 투표 리스트~</div>
        </div>
      </CrewFrame>
      <FooterCrew />
    </>
  );
};

const VoteStartBtn = styled.button`
  font-size: 1.2rem;
  font-weight: bold;
  color: white;
  background-color: #fe9d3a;

  &:active {
    background-color: #cf7f2f;
  }
  user-select: none;

  width: 80vw;
  height: 8vh;
  border-radius: 1rem;
  border: 0;
`;

const CrewMemberList = styled.div`
  display: flex;
  gap: 5vmin;
  overflow-x: scroll;
  padding: 1rem 1rem 0 1rem;

  &::-webkit-scrollbar {
    display: none;
  }
`;

const CrewFrame = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 24vmin 0;
  /* height: 80vh; */
  gap: 3vh;

  overflow-y: scroll;
`;

const CrewImg = styled.img<{ src: string }>`
  width: 80vmin;
  height: 80vmin;

  max-width: 30vh;
  max-height: 30vh;

  border-radius: 100%;
  margin-bottom: 1vmax;

  /* border: solid orange 1px; */
`;
