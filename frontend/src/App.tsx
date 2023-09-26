import React, { useEffect } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import "./App.css";
import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import { Main } from "./pages/main/Main";
import { Login } from "./pages/login/Login";
import { SignUp } from "./pages/singup/SignUp";
import { MyPage } from "./pages/member/MyPage";
import { MyPageEdit } from "./pages/member/MyPageEdit";
import { FriendList } from "./pages/friend/FriendList";
import { CrewList } from "./pages/crew/CrewList";
import { CrewDetail } from "./pages/crew/CrewDetail";
import { CrewMake } from "./pages/crew/CrewMake";
import { CrewInvite } from "./pages/crew/CrewInvite";
import { MemberRecommendation } from "./pages/memberRecommendation/MemberRecommendation";
import { CrewRecommendation } from "./pages/crewRecomendation/CrewRecommendation";
import NotFound from "./pages/notfound/NotFound";
import MediaQueryProvider from "react-responsive";
import { SignUpComplete } from "./pages/singup/SignUpComplete";

import { Test } from "./pages/Test";

const queryClient = new QueryClient();

const App = () => {
  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty("--vh", `${vh}px`);
  }
  useEffect(() => {
    setScreenSize();
  });

  return (
    <>
      <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <Routes>
            {/* 메인페이지 */}
            <Route path="/" element={<Main />} />
            {/* 로그인페이지 */}
            <Route path="/login" element={<Login />} />
            {/* 회원가입페이지 */}
            <Route path="/signup" element={<SignUp />} />
            {/* 회원가입완료페이지 */}
            <Route path="/signup/complete" element={<SignUpComplete />} />
            {/* 마이페이지 */}
            <Route path="/mypage/:memberId" element={<MyPage />} />
            {/* 마이페이지 수정페이지 */}
            <Route path="/mypage/edit/:memberId" element={<MyPageEdit />} />
            {/* 개인 메뉴 추천페이지 */}
            <Route
              path="/recommendation/:memberId"
              element={<MemberRecommendation />}
            />
            {/* 자기 친구목록 페이지 */}
            <Route path="/friend/:memberId" element={<FriendList />} />
            {/* 친구 초대QR 초대하는사람의 ID, 초대받는사람의 ID 두개가 필요할까 고민*/}
            {/* <Route path="/friend/invite/:memberId" element={<FriendInvite />} /> */}
            {/* 그룹목록 페이지 */}
            <Route path="/crew/:memberId" element={<CrewList />} />
            {/* 특정 그룹 상세보기 */}
            <Route path="/crew/:crewId" element={<CrewDetail />} />
            {/* 그룹생성 - 그룹생성자의 친구목록 보여주기 */}
            <Route path="/crew/make/:memberId" element={<CrewMake />} />
            {/* 그룹초대 QR */}
            <Route path="/crew/invite/:crewId" element={<CrewInvite />} />
            {/* 그룹메뉴추천페이지 */}
            <Route
              path="/crewrecommendation/:crewId"
              element={<CrewRecommendation />}
            />
            <Route path="/test" element={<Test />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </QueryClientProvider>
    </>
  );
};

export default App;
