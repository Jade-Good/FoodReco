import React, { useState, useEffect, ChangeEvent } from "react";
import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewInviteBasic from "../../components/header/HeaderCrewInviteBasic";
import HeaderCrewInviteProfile from "../../components/header/HeaderCrewInviteProfile";
import api from "../../utils/axios";
import { Friend } from "../../components/friend/Friend";
import classes from "./CrewMake.modules.css";
import { CrewAdd } from "../../components/crewpage/CrewAdd";
import axios from "axios";
import StyledButton from "../../styles/StyledButton";
import { useNavigate } from "react-router-dom";
import { count } from "console";
import { ToastContainer, toast } from "react-toastify";
import CrewMemberProfile from "../../components/crewpage/CrewMemberProfile";
import styled from "styled-components";
interface CrewData {
  crewName: string;
  crewMembers: number[];
  crewImg?: string | File | null;
}
export const CrewMake = () => {
  const navigate = useNavigate();
  const [friendCount, setFriendCount] = useState(0);
  const [friendList, setFriendList] = useState<
    { memberNickname: string; memberImg: string; memberSeq: number }[]
  >([]);
  // const [search, setSearch] = useState("");
  // const [isInputFocused, setIsInputFocused] = useState(false);
  const [crew, setCrew] = useState<number[]>([]);
  const [crewName, setCrewName] = useState("");
  const [crewImg, setCrewImg] = useState<File | null>(null);

  useEffect(() => {
    setFriendCount(crew.length);
  }, [crew]);

  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/friend/list`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        // console.log(res);
        setFriendList(res.data.friendList);
      })
      .catch((err) => console.log(err));
  }, []);

  const toggleFriend = (friendSeq: number) => {
    if (crew.includes(friendSeq)) {
      // 음식이 이미 좋아요 목록에 있는 경우, 제거합니다.
      setCrew(crew.filter((item) => item !== friendSeq));
    } else {
      // 음식이 좋아요 목록에 없는 경우, 추가합니다.
      setCrew([...crew, friendSeq]);
    }
  };

  const handleCrewImgUpload = () => {
    const input = document.getElementById(
      "crewUploadInput"
    ) as HTMLInputElement;
    input?.click();
  };

  const handleFileChange = (
    event: ChangeEvent<HTMLInputElement>,
    setter: React.Dispatch<React.SetStateAction<File | null>>
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      setter(file);
    }
  };

  const handleMakeCrew = (e: React.FormEvent) => {
    e.preventDefault();
    let crewData: CrewData | undefined;

    const formData = new FormData();

    crewData = {
      crewName: crewName,
      // crewImg: crewImg,
      crewMembers: crew,
    };

    // console.log(crewData);
    formData.append("crewName", crewName); // crewName 추가
    if (crewImg) {
      formData.append("crewImg", crewImg); // crewImg 추가
    }

    const crewMembersJSON = JSON.stringify(
      crew.map((memberSeq) => ({ memberSeq }))
    );
    formData.append("crewMembers", crewMembersJSON);

    // console.log("crewMembers", formData.get("crewMembers"));
    if (friendCount >= 1) {
      api
        .post(`${process.env.REACT_APP_BASE_URL}/crew/regist`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("accesstoken")}`,
          },
        })
        .then((res) => {
          // console.log(res);
          navigate("/crew");
        })
        .catch((err) => console.log(err));
    } else {
      toast.error("최소 1명이상의 친구를 골라주세요.", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
      });
    }
  };

  const handleCrewName = (e: ChangeEvent<HTMLInputElement>) => {
    setCrewName(e.target.value);
  };

  return (
    <>
      <HeaderCrewInviteBasic count={friendCount} />
      <div style={{ width: "90vw" }}>
        <h1 style={{ margin: "0 0 2vmin 0", fontSize: "1.3rem" }}>그룹원</h1>
      </div>
      <div style={{ margin: "5.5vh", overflow: "hidden" }}>
        <CrewMemberList>
          {friendList
            .filter((friend) => crew.includes(friend.memberSeq)) // Filter friends based on 'crew' array
            .map((friend, key) => {
              return (
                <CrewMemberProfile
                  name={friend.memberNickname}
                  profileImg={friend.memberImg}
                  key={friend.memberSeq}
                  memberStatus={friend.memberNickname}
                />
              );
            })}
        </CrewMemberList>

        <form onSubmit={handleMakeCrew}>
          <input
            style={inputStyle}
            value={crewName}
            placeholder="그룹명을 적어주세요"
            onChange={handleCrewName}
          />
          <div>
            {crewImg && (
              <img
                src={URL.createObjectURL(crewImg)}
                alt="Crew"
                width="100"
                height="100"
                style={{ marginTop: "20px" }}
              />
            )}
            <StyledButton
              type="button"
              width="25vw"
              fontSize="10px"
              onClick={handleCrewImgUpload}
              background="#FFF6EC"
              color="gray"
            >
              사진 업로드
            </StyledButton>

            <input
              accept="image/*"
              type="file"
              hidden
              id="crewUploadInput"
              onChange={(e) => handleFileChange(e, setCrewImg)}
            />
            <StyledButton fontSize="15px" type="submit">
              생성하기
            </StyledButton>
          </div>
        </form>

        {friendList.map((friend, i) => {
          return (
            <CrewAdd
              name={friend.memberNickname}
              imgUrl={friend.memberImg}
              key={friend.memberSeq}
              onClick={() => toggleFriend(friend.memberSeq)}
              selected={crew.includes(friend.memberSeq)}
            />
          );
        })}
      </div>
      <FooterCrew />
      <ToastContainer
        position="top-center"
        autoClose={1000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </>
  );
};

const inputStyle = {
  borderRadius: "20px",
  border: "1px solid #FE9D3A",
  display: "flex",
  justifyContent: "center",
  width: "70vw",
  height: "3vh",

  alignItems: "center", // 세로 중앙 정렬
  transition: "border-color 0.2s", // 테두리 색상 변화를 부드럽게 만들기 위한 트랜지션

  margin: "0 auto",
};

const CrewMemberList = styled.div`
  display: flex;
  gap: 5vmin;
  overflow-x: scroll;
  padding: 1rem 1rem 0 1rem;

  &::-webkit-scrollbar {
    display: none;
  }
`;
