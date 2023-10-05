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
    // console.log("error", error);

    const { config } = error;

    // 응답 상태 코드가 401인 경우, 리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.
    if (error.response && error.response.status === 401) {
      try {
        const refreshToken = localStorage.getItem("refreshToken");
        api
          .post(`${process.env.REACT_APP_BASE_URL}/jwt`, {
            refreshToken: refreshToken,
          })
          .then((res) => {
            const accessToken = res.headers.authorization;
            const refreshToken = res.headers.authorizationRefresh;

            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            // Update user state here if needed

            // Retry the original request with the new access token
            config.headers["Authorization"] = `Bearer ${accessToken}`;
            return api(config);
          })
          .catch((err) => {
            console.log(err);
            if (err.response.status === 404) {
              localStorage.removeItem("refreshToken");
              localStorage.removeItem("accessToken");
              localStorage.removeItem("email");
              localStorage.removeItem("memberSeq");

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
              window.location.replace(`${process.env.REACT_APP_BASE_URL2}`);
            }
            return;
          });
      } catch (refreshError) {
        // console.log("refresherror", refreshError);
        // Handle the refresh error appropriately
        return Promise.reject(refreshError);
      }
    } else if (error.response.status === 403) {
      try {
        const refreshToken = localStorage.getItem("refreshToken");
        const email = localStorage.getItem("email");
        console.log(email);
        api
          .post(`${process.env.REACT_APP_BASE_URL}/jwt`, {
            refreshToken: refreshToken,
            email: email,
          })
          .then((res) => {
            console.log("refresh token 받기", res);
            const accessToken = res.data.authorization;
            const refreshToken = res.data.authorizationRefresh;

            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            // Update user state here if needed

            // Retry the original request with the new access token
            config.headers["Authorization"] = `Bearer ${accessToken}`;

            return api(config);
          })
          .catch((err) => {
            console.log(err);
            if (err.response.status === 404) {
              localStorage.removeItem("refreshToken");
              localStorage.removeItem("accessToken");
              localStorage.removeItem("email");
              localStorage.removeItem("memberSeq");

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
              window.location.replace(`${process.env.REACT_APP_BASE_URL2}`);
            }
            return;
          });
      } catch (refreshError) {
        // console.log("refresherror", refreshError);
        // Handle the refresh error appropriately
        return Promise.reject(refreshError);
      }
    } else if (error.response.status === 404) {
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("email");
      localStorage.removeItem("memberSeq");
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
      window.location.replace(`${process.env.REACT_APP_BASE_URL2}`);
    }

    return Promise.reject(error);
  }
);

export default api;
