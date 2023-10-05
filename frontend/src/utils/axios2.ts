import axios from "axios";
import { userState } from "../recoil/atoms/userState";
import { toast } from "react-toastify";

const api = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
  withCredentials: true,
});

// Axios 요청 인터셉터: 모든 요청에 대해 헤더에 액세스 토큰을 추가합니다.
api.interceptors.request.use((request) => {
  const accessToken = localStorage.getItem("accessToken");

  if (accessToken) {
    request.headers["Authorization"] = `Bearer ${accessToken}`;
  }

  return request;
});

// Axios 응답 인터셉터: 모든 응답을 처리합니다.
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    console.log("error", error);

    const { config } = error;

    // 응답 상태 코드가 401인 경우, 리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.
    if (error.response && error.response.status === 401) {
      try {
        const refreshToken = localStorage.getItem("refreshToken");
        const res = await api.post(
          `${process.env.REACT_APP_BASE_URL}/member/login`,
          {
            headers: {
              AuthorizationRefresh: `Bearer ${refreshToken}`,
            },
          }
        );

        const accessToken = res.headers.authorization;
        const newRefreshToken = res.headers.authorizationRefresh;

        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", newRefreshToken);

        // Update user state here if needed

        // Retry the original request with the new access token
        config.headers["Authorization"] = `Bearer ${accessToken}`;
        return api(config);
      } catch (refreshError) {
        // Handle the refresh error appropriately
        return Promise.reject(refreshError);
      }
    } else if (error.response.status === 404) {
      toast.error("로그인이 필요합니다.", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
      });
      window.location.replace(`https://j9b102.p.ssafy.io/login`);
    }

    return Promise.reject(error);
  }
);
