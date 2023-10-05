import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../utils/axios";
import { ToastContainer, toast } from "react-toastify";

export const FriendAdd = () => {
  const navigate = useNavigate();
  const { memberSeq } = useParams();

  useEffect(() => {
    if (localStorage.getItem("memberSeq") !== memberSeq) {
      api
        .get(`${process.env.REACT_APP_BASE_URL}/member/friend/${memberSeq}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        })
        .then((res) => {
          console.log(res);
          toast.success("🦄 친구추가 성공!!", {
            position: "top-center",
            autoClose: 1000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "colored",
          });
          navigate("/");
        })
        .catch((err) => {
          console.log(err);
          toast.error("🦄 친구추가 실패!", {
            position: "top-center",
            autoClose: 1000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "colored",
          });
        });
    }
  });

  return (
    <>
      <p>친구 추가 중</p>
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
