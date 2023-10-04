import axios from "axios";
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms/userState";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const api = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
});

// Axios 요청 인터셉터: 모든 요청에 대해 헤더에 액세스 토큰을 추가합니다.
api.interceptors.request.use(
  (request) => {
    // const [user, setUser] = useRecoilState(userState);
    const accessToken = localStorage.getItem("accessToken");

    // const accessToken = user.accessToken; // 액세스 토큰을 가져오는 함수를 구현해야 합니다.

    if (accessToken) {
      request.headers.Authorization = `Bearer ${accessToken}`;
      axios.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;
    }

    return request;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Axios 응답 인터셉터: 모든 응답을 처리합니다.
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    console.error("error", error);
    const originalRequest = error.config;

    // 응답 상태 코드가 401인 경우, 리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.
    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const [user, setUser] = useRecoilState(userState);
        // const email = user.email;
        const refreshToken = localStorage.getItem("refreshToken");
        axios
          .post(
            `${process.env.REACT_APP_BASE_URL}/member/login`,

            { refreshToken: refreshToken }
          )
          .then((res) => {
            const accessToken = res.headers.authorization;
            const refreshToken = res.headers.authorizationRefresh;

            // axios.defaults.headers.common[
            //   "Authorization"
            // ] = `Bearer ${accessToken}`;
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            axios.defaults.headers.common[
              "Authorization"
            ] = `Bearer ${accessToken}`;

            setUser((prevUser) => ({
              ...prevUser,
              refreshToken: refreshToken,
              accessToken: accessToken,
            }));
          })
          .catch((err) => {
            console.log(err);
          });

        // 이전 요청을 복원하고 새로운 액세스 토큰을 사용하여 다시 요청합니다.
        // originalRequest.headers.Authorization = `Bearer ${user.accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // 리프레시 토큰을 사용하여 액세스 토큰을 갱신하는 과정에서 오류가 발생한 경우, 처리할 내용을 추가로 구현합니다.
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

export default api;
