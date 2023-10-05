import React, { useEffect } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import "./App.css";
import {
  BrowserRouter,
  Route,
  Routes,
  useNavigate,
  useParams,
} from "react-router-dom";
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

import { Provider } from "react-redux";
import { store } from "./redux/store/store";
import { CheckLogin } from "./pages/PrivateRoute";
import { FriendAdd } from "./pages/friend/FriendAdd";

const queryClient = new QueryClient();

const App = () => {
  const accessToken = localStorage.getItem("accessToken");
  console.log("sdf", accessToken);
  let isLogin = 0;
  if (accessToken) {
    isLogin = 1;
  } else {
    isLogin = 0;
  }

  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty("--vh", `${vh}px`);
  }
  useEffect(() => {
    setScreenSize();
  });

  return (
    <>
      <Provider store={store}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Routes>
              {/* 메인페이지 */}
              <Route path="/" element={isLogin ? <Main /> : <Login />} />
              {/* 로그인페이지 */}
              <Route path="/login" element={<Login />} />

              {/* 회원가입페이지 */}
              <Route path="/signup" element={<SignUp />} />
              {/* 회원가입완료페이지 */}
              <Route path="/signup/complete" element={<SignUpComplete />} />
              {/* 마이페이지 */}
              <Route
                path="/mypage"
                element={
                  <CheckLogin authenticated={isLogin} component={<MyPage />} />
                }
              />
              {/* 마이페이지 수정페이지 */}
              <Route
                path="/mypage/edit"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<MyPageEdit />}
                  />
                }
              />
              {/* 개인 메뉴 추천페이지 */}
              <Route
                path="/recommendation"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<MemberRecommendation />}
                  />
                }
              />
              {/* 자기 친구목록 페이지 */}
              <Route
                path="/friend"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<FriendList />}
                  />
                }
              />
              {/* 친구 초대QR 초대하는사람의 ID, 초대받는사람의 ID 두개가 필요할까 고민*/}
              {/* <Route path="/friend/invite/:memberId" element={<FriendInvite />} /> */}
              {/* 그룹목록 페이지 */}
              <Route
                path="/crew"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<CrewList />}
                  />
                }
              />
              {/* 친구초대하기 */}
              <Route
                path="/friend/add/:memberSeq"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<FriendAdd />}
                  />
                }
              />

              {/* 특정 그룹 상세보기 */}
              <Route
                path="/crew/detail/:crewSeq"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<CrewDetail />}
                  />
                }
              />
              {/* 그룹생성 - 그룹생성자의 친구목록 보여주기 */}
              <Route
                path="/crew/make"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<CrewMake />}
                  />
                }
              />
              {/* 그룹초대 QR */}
              <Route
                path="/crew/invite"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<CrewInvite />}
                  />
                }
              />
              {/* 그룹메뉴추천페이지 */}
              <Route
                path="/crewrecommendation"
                element={
                  <CheckLogin
                    authenticated={isLogin}
                    component={<CrewRecommendation />}
                  />
                }
              />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </BrowserRouter>
        </QueryClientProvider>
      </Provider>
    </>
  );
};

export default App;
