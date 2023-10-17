import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styled, { css } from "styled-components";
import api from "../../utils/axios";

import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewDetail from "../../components/header/HeaderCrewDetail";

import CrewMemberProfile from "../../components/crewpage/CrewMemberProfile";

import CrewVoteModal from "../../components/crewpage/CrewVoteModal";
import VoteHistoryModal from "../../components/crewpage/VoteHistoryModal";
import { useRecoilState } from "recoil";
import {
  crewVoteModal,
  crewVoteHistorylModal,
} from "../../recoil/atoms/modalState";

import { useNavigate } from "react-router-dom";

interface CrewDetailProps {
  memberSeq: number;
  crewSeq: number;
  crewName: string;
  crewImg: string;
  crewStatus: "투표전" | "분석중" | "투표중";
  memberStatus: "미응답" | "수락"; // 본인의 그룹 가입 여부
  crewMembers: crewMembers[];

  // 투표 중인경우만 존재(아닌경우 null)
  voteRecommend: voteRecommend | null;

  //분석 중인 경우 []
  histories: history[];
}

export interface foodList {
  foodSeq: number;
  foodName: string;
  foodImg: string;
  foodVoteCount: number;
  vote: boolean;
}

interface crewMembers {
  memberSeq: number;
  memberName: string;
  memberImg: string;
  memberStatus: "미응답" | "수락"; // 해당 사용자의 가입 여부
}

interface history {
  crewRecommendSeq: number;
  crewRecommendTime: string;
  foodList: foodList[];
}

export interface voteRecommend {
  crewRecommendSeq?: number;
  crewRecommendTime?: string;
  foodList?: foodList[];
}

export const CrewDetail = () => {
  const [crewDetailInfo, setCrewDetailInfo] = useState<CrewDetailProps>();
  const [btnState, setBtnState] = useState("투표전");
  const [btnText, setBtnText] = useState("메뉴 투표를 시작하세요!");
  const [vote, setVote] = useState<voteRecommend>();
  const { crewSeq } = useParams();

  // 모달
  const [modalOpen, setModalOpen] = useRecoilState(crewVoteModal);
  const [openHistoryModal, setHistoryModal] = useRecoilState(
    crewVoteHistorylModal
  );

  let eventSource: EventSource;

  const getCrewDetailInfo = async () => {
    try {
      const res = await api.get(
        `${process.env.REACT_APP_BASE_URL}/crew/detail/${crewSeq}`
      );
      console.log("그룹 상세 정보 조회 성공 : ", res);
      return res.data;
    } catch (err) {
      console.error("그룹 상세 정보 못가져옴:", err);
      setTimeout(getCrewDetailInfo, 1000); // 1초 후 재시도
      throw err; // 에러를 다시 throw하여 오류 핸들링을 위임합니다.
    }
  };

  const connectSSE = async () => {
    try {
      const info = await getCrewDetailInfo();

      if (!info) {
        throw new Error("그룹 상세 정보 없음");
      }

      setCrewDetailInfo(info);
      setBtnState(info.crewStatus);
      setVote(info.voteRecommend);

      if (info.crewStatus === "투표전") setBtnText("메뉴 투표를 시작하세요!");
      else if (info.crewStatus === "분석중")
        setBtnText("그룹 메뉴 분석 중입니다");
      else if (info.crewStatus === "투표중")
        setBtnText(">> 메뉴 투표에 참여하세요 <<");

      console.log("투표 SSE 연결 시도! / 멤버 시퀀스 : ", info.memberSeq);

      eventSource = new EventSource(
        `${process.env.REACT_APP_BASE_URL}/crew/sse/${crewSeq}/${info.memberSeq}`
      );

      eventSource.addEventListener("connect", (e) => {
        console.log("connect");
      });
      eventSource.addEventListener("start", (e) => {
        console.log("start", e);

        let copy = { ...info };
        copy.voteRecommend = e.data;
        setCrewDetailInfo(copy);

        setBtnState("투표중");
        setBtnText(">> 메뉴 투표에 참여하세요 <<");
        console.log("투표시작 : ", btnState, btnText);
        console.log("투표목록 : ", copy.voteRecommend);
      });
      eventSource.addEventListener("end", (e) => {
        console.log("end", e);
        setBtnState("투표전");
        setBtnText("메뉴 투표를 시작하세요!");
        console.log("투표종료 : ", btnState, btnText);
        setModalOpen({ modalOpen: false });
      });

      eventSource.addEventListener("vote", (e) => {
        console.log("vote", JSON.parse(e.data));
        setVote(JSON.parse(e.data));
      });

      eventSource.onerror = (error) => {
        console.error("SSE Error:", error);
        eventSource.close();

        setTimeout(() => {
          console.log("연결 재시도");
          connectSSE();
        }, 3000); // 3초 후 재시도
      };
    } catch (err) {
      console.error(err);
      setTimeout(connectSSE, 2000); // 2초 후 재시도
    }
  };

  useEffect(() => {
    if (eventSource) eventSource.close();

    connectSSE();

    return () => {
      console.log("투표 SSE 종료");
      if (eventSource) eventSource.close();
    };
  }, []);

  const voteStartBtnHandler = () => {
    if (!crewDetailInfo) return;

    switch (btnState) {
      case "투표전":
        console.log("투표 시작 누름!!!");
        setBtnState("분석중");
        setBtnText("그룹 메뉴 분석 중입니다");
        api
          .get(`${process.env.REACT_APP_BASE_URL}/recommend/crew/${crewSeq}`)
          .then((res) => {
            console.log(res.data); // 그룹 투표 시작
          })
          .catch((err) => {
            console.error("투표 시작 실패", err);
          });
        break;
      case "분석중":
        console.log("분석중이라 무시~~");
        break;
      case "투표중":
        console.log("투표 모달창 떠야함!!");
        setModalOpen({ modalOpen: true });
        break;
    }
  };

  const [history, setHistory] = useState<history>();
  const openHistory = (history: history) => {
    setHistory(history);
    setHistoryModal({ modalOpen: true });
  };

  // 가입 승인 거절
  const navigate = useNavigate();
  const joinHandler = (req: number) => {
    if (!crewSeq) return;

    api
      .post(`${process.env.REACT_APP_BASE_URL}/crew/join`, {
        crewSeq: crewSeq,
        crewJoinType: req,
      })
      .then((res) => {
        console.log("가입 응답 성공 : ", res.data);
        if (req === -1) navigate(`/crew`);
        else window.location.reload();
      })
      .catch((err) => {
        console.error("가입 응답 실패", err);
      });
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

        {crewDetailInfo && crewDetailInfo.memberStatus === "미응답" ? (
          <div style={{ display: "flex", gap: "1rem" }}>
            <JoinBtn
              onClick={() => {
                joinHandler(1);
              }}
            >
              승인
            </JoinBtn>
            <RejectBtn
              onClick={() => {
                joinHandler(-1);
              }}
            >
              거절
            </RejectBtn>
          </div>
        ) : (
          <>
            {/* 그룹원 목록 */}
            <div style={{ width: "90vw" }}>
              <h1 style={{ margin: "0 0 2vmin 0", fontSize: "1.3rem" }}>
                그룹원
              </h1>
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
                <VoteStartBtn status={btnState} onClick={voteStartBtnHandler}>
                  {btnText}
                </VoteStartBtn>
              </div>
            </div>

            {/* 투표 기록 */}
            <div style={{ width: "90vw" }}>
              <h1 style={{ margin: "0 0 5vmin 0", fontSize: "1.3rem" }}>
                투표 기록
              </h1>
              {crewDetailInfo?.histories.map((history, key) => {
                const dateString = history.crewRecommendTime;
                const date = new Date(dateString);
                const formattedDate = `${date.getFullYear()}-${
                  date.getMonth() + 1
                }-${date.getDate()} ${date.getHours()}시 ${date.getMinutes()}분`;

                return (
                  <VoteHistory
                    key={key}
                    onClick={() => {
                      openHistory(history);
                    }}
                  >
                    {key + 1}번째 투표 : {formattedDate}
                  </VoteHistory>
                );
              })}
            </div>
          </>
        )}
      </CrewFrame>
      <FooterCrew />
      <CrewVoteModal
        crewRecommendSeq={vote?.crewRecommendSeq}
        crewRecommendTime={vote?.crewRecommendTime}
        foodList={vote?.foodList}
        memberCnt={crewDetailInfo?.crewMembers.length}
        crewSeq={crewDetailInfo?.crewSeq}
      />
      <VoteHistoryModal
        crewRecommendSeq={history?.crewRecommendSeq}
        crewRecommendTime={history?.crewRecommendTime}
        foodList={history?.foodList}
        memberCnt={crewDetailInfo?.crewMembers.length}
        crewSeq={crewDetailInfo?.crewSeq}
      />
    </>
  );
};

const JoinBtn = styled.div`
  /* width: 3rem; */
  /* height: 2rem; */
  padding: 0.5rem 2rem;
  border-radius: 2rem;
  background-color: #fe9d3a;
  color: white;
  font-size: 2rem;

  &:active {
    background-color: #cf7f2f;
  }
`;

const RejectBtn = styled.div`
  /* width: 3rem; */
  /* height: 2rem; */
  padding: 0.5rem 2rem;
  border-radius: 2rem;
  background-color: white;
  color: #fe9d3a;
  border: 1px solid #fe9d3a;
  font-size: 2rem;

  &:active {
    background-color: #dcdcdc;
  }
`;

const VoteHistory = styled.div`
  display: flex;
  gap: 5vmin;
  overflow-x: scroll;
  padding: 1rem;
  border-bottom: 1px solid #dcdcdc;
  &:active {
    background-color: #dcdcdc;
  }
`;

const VoteStartBtn = styled.button<{ status: string | undefined }>`
  font-size: 1.2rem;
  font-weight: bold;
  color: white;
  background-color: #fe9d3a;

  &:active {
    background-color: #cf7f2f;
  }
  user-select: none;

  /* width: 80vw; */
  padding: 0 1rem;
  height: 8vh;
  border-radius: 1rem;
  border: 0;

  ${(props) =>
    props.status === "분석중" &&
    css`
      background-color: white;
      border: solid #fe9d3a 1px;
      color: #fe9d3a;
    `}
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
